package rpgshop.application.usecase.order;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.order.CreateOrderCommand;
import rpgshop.application.command.order.PaymentInfo;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.cart.CartGateway;
import rpgshop.application.gateway.cart.CartItemGateway;
import rpgshop.application.gateway.coupon.CouponGateway;
import rpgshop.application.gateway.customer.AddressGateway;
import rpgshop.application.gateway.customer.CreditCardGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.cart.Cart;
import rpgshop.domain.entity.cart.CartItem;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.CreditCard;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;
import rpgshop.domain.entity.order.OrderPayment;
import rpgshop.domain.entity.order.constant.OrderStatus;
import rpgshop.domain.entity.product.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.math.BigDecimal.ZERO;

@Service
public class CreateOrderUseCase {
    private static final BigDecimal MIN_CARD_AMOUNT = new BigDecimal("10.00");
    private static final BigDecimal FREIGHT_PER_KG = new BigDecimal("2.50");

    private final OrderGateway orderGateway;
    private final CartGateway cartGateway;
    private final CartItemGateway cartItemGateway;
    private final CustomerGateway customerGateway;
    private final AddressGateway addressGateway;
    private final CreditCardGateway creditCardGateway;
    private final CouponGateway couponGateway;
    private final ProductGateway productGateway;

    public CreateOrderUseCase(
        final OrderGateway orderGateway,
        final CartGateway cartGateway,
        final CartItemGateway cartItemGateway,
        final CustomerGateway customerGateway,
        final AddressGateway addressGateway,
        final CreditCardGateway creditCardGateway,
        final CouponGateway couponGateway,
        final ProductGateway productGateway
    ) {
        this.orderGateway = orderGateway;
        this.cartGateway = cartGateway;
        this.cartItemGateway = cartItemGateway;
        this.customerGateway = customerGateway;
        this.addressGateway = addressGateway;
        this.creditCardGateway = creditCardGateway;
        this.couponGateway = couponGateway;
        this.productGateway = productGateway;
    }

    @Nonnull
    @Transactional
    public Order execute(@Nonnull final CreateOrderCommand command) {
        final Customer customer = customerGateway.findById(command.customerId())
            .orElseThrow(() -> new EntityNotFoundException("Customer", command.customerId()));

        final Address deliveryAddress = addressGateway.findById(command.deliveryAddressId())
            .orElseThrow(() -> new EntityNotFoundException("Address", command.deliveryAddressId()));

        final Cart cart = cartGateway.findByCustomerId(command.customerId())
            .orElseThrow(() -> new BusinessRuleException("Cart is empty"));

        final List<CartItem> cartItems = cartItemGateway.findByCartId(cart.id());
        if (cartItems.isEmpty()) {
            throw new BusinessRuleException("Cart is empty");
        }

        validateStockAvailability(cartItems);

        final BigDecimal subtotal = calculateSubtotal(cartItems);
        final BigDecimal freightCost = calculateFreight(cartItems);
        final BigDecimal total = subtotal.add(freightCost);

        final List<OrderItem> orderItems = buildOrderItems(cartItems);

        final List<OrderPayment> payments = buildPayments(command.payments(), total, command.customerId());
        validatePaymentCoversTotal(payments, total);

        final Instant now = Instant.now();
        final Order order = Order.builder()
            .id(UUID.randomUUID())
            .customer(customer)
            .status(OrderStatus.PROCESSING)
            .deliveryAddress(deliveryAddress)
            .freightCost(freightCost)
            .subtotal(subtotal)
            .total(total)
            .purchasedAt(now)
            .items(orderItems)
            .payments(payments)
            .build();

        final Order saved = orderGateway.save(order);

        cartItemGateway.deleteAllByCartId(cart.id());

        return saved;
    }

    private void validateStockAvailability(final List<CartItem> cartItems) {
        for (final CartItem item : cartItems) {
            final Product product = productGateway.findById(item.product().id())
                .orElseThrow(() -> new EntityNotFoundException("Product", item.product().id()));

            if (!product.isActive()) {
                throw new BusinessRuleException("Product '%s' is no longer available".formatted(product.name()));
            }
            if (product.stockQuantity() < item.quantity()) {
                throw new BusinessRuleException(
                    "Insufficient stock for product '%s'. Available: %d, Requested: %d"
                        .formatted(product.name(), product.stockQuantity(), item.quantity())
                );
            }
        }
    }

    private BigDecimal calculateSubtotal(final List<CartItem> cartItems) {
        return cartItems.stream()
            .map(item -> item.product().salePrice().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(ZERO, BigDecimal::add);
    }

    private BigDecimal calculateFreight(final List<CartItem> cartItems) {
        final BigDecimal totalWeight = cartItems.stream()
            .map(item -> item.product().weight().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(ZERO, BigDecimal::add);

        return totalWeight.multiply(FREIGHT_PER_KG).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private List<OrderItem> buildOrderItems(final List<CartItem> cartItems) {
        final List<OrderItem> items = new ArrayList<>();
        for (final CartItem cartItem : cartItems) {
            final BigDecimal unitPrice = cartItem.product().salePrice();
            final BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(cartItem.quantity()));

            items.add(OrderItem.builder()
                .id(UUID.randomUUID())
                .product(cartItem.product())
                .quantity(cartItem.quantity())
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .build());
        }
        return items;
    }

    private List<OrderPayment> buildPayments(
        final List<PaymentInfo> paymentInfos,
        final BigDecimal total,
        final UUID customerId
    ) {
        if (paymentInfos == null || paymentInfos.isEmpty()) {
            throw new BusinessRuleException("At least one payment method is required");
        }

        final List<OrderPayment> payments = new ArrayList<>();
        boolean hasPromotionalCoupon = false;

        for (final PaymentInfo info : paymentInfos) {
            CreditCard creditCard = null;
            Coupon coupon = null;

            if (info.couponId() != null) {
                coupon = couponGateway.findById(info.couponId())
                    .orElseThrow(() -> new EntityNotFoundException("Coupon", info.couponId()));

                if (coupon.isUsed()) {
                    throw new BusinessRuleException("Coupon '%s' has already been used".formatted(coupon.code()));
                }
                if (coupon.expiresAt() != null && coupon.expiresAt().isBefore(Instant.now())) {
                    throw new BusinessRuleException("Coupon '%s' has expired".formatted(coupon.code()));
                }
                if (coupon.type() == rpgshop.domain.entity.coupon.constant.CouponType.PROMOTIONAL) {
                    if (hasPromotionalCoupon) {
                        throw new BusinessRuleException("Only one promotional coupon is allowed per order");
                    }
                    hasPromotionalCoupon = true;
                }
            }

            if (info.creditCardId() != null) {
                creditCard = creditCardGateway.findById(info.creditCardId())
                    .orElseThrow(() -> new EntityNotFoundException("CreditCard", info.creditCardId()));
            }

            payments.add(OrderPayment.builder()
                .id(UUID.randomUUID())
                .creditCard(creditCard)
                .coupon(coupon)
                .amount(info.amount())
                .build());
        }

        return payments;
    }

    private void validatePaymentCoversTotal(final List<OrderPayment> payments, final BigDecimal total) {
        final BigDecimal totalPaid = payments.stream()
            .map(OrderPayment::amount)
            .reduce(ZERO, BigDecimal::add);

        if (totalPaid.compareTo(total) < 0) {
            throw new BusinessRuleException(
                "Payment total R$ %s does not cover order total R$ %s".formatted(totalPaid, total)
            );
        }
    }
}
