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
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static java.math.BigDecimal.ZERO;

@Service
public class CreateOrderUseCase {
    private static final BigDecimal MIN_CARD_AMOUNT = new BigDecimal("10.00");
    private static final BigDecimal FREIGHT_PER_KG = new BigDecimal("2.50");
    private static final BigDecimal FREIGHT_BASE_HANDLING = new BigDecimal("3.00");
    private static final BigDecimal INTERSTATE_MULTIPLIER = new BigDecimal("1.25");
    private static final BigDecimal INTERNATIONAL_MULTIPLIER = new BigDecimal("1.80");

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

        if (!addressGateway.existsByIdAndCustomerIdOrWithoutCustomer(command.deliveryAddressId(), command.customerId())) {
            throw new BusinessRuleException("O endereco de entrega informado nao pertence ao cliente");
        }

        final Address deliveryAddress = addressGateway.findById(command.deliveryAddressId())
            .orElseThrow(() -> new EntityNotFoundException("Address", command.deliveryAddressId()));

        final Cart cart = cartGateway.findByCustomerId(command.customerId())
            .orElseThrow(() -> new BusinessRuleException("O carrinho esta vazio"));

        final List<CartItem> cartItems = cartItemGateway.findByCartId(cart.id());
        if (cartItems.isEmpty()) {
            throw new BusinessRuleException("O carrinho esta vazio");
        }

        validateStockAvailability(cartItems);

        final BigDecimal subtotal = calculateSubtotal(cartItems);
        final BigDecimal freightCost = calculateFreight(cartItems, deliveryAddress);
        final BigDecimal total = subtotal.add(freightCost);

        final List<OrderItem> orderItems = buildOrderItems(cartItems);

        final List<OrderPayment> payments = buildPayments(command.payments(), command.customerId(), total);
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
                throw new BusinessRuleException("O produto '%s' nao esta mais disponivel".formatted(product.name()));
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

    private BigDecimal calculateFreight(final List<CartItem> cartItems, final Address deliveryAddress) {
        final BigDecimal totalWeight = cartItems.stream()
            .map(item -> item.product().weight().multiply(BigDecimal.valueOf(item.quantity())))
            .reduce(ZERO, BigDecimal::add);

        BigDecimal freight = totalWeight.multiply(FREIGHT_PER_KG)
            .add(FREIGHT_BASE_HANDLING);

        if (deliveryAddress.country() != null) {
            final String country = deliveryAddress.country().trim().toUpperCase(Locale.ROOT);
            if (!country.equals("BRASIL") && !country.equals("BRAZIL")) {
                return freight.multiply(INTERNATIONAL_MULTIPLIER).setScale(2, RoundingMode.HALF_UP);
            }
        }

        if (deliveryAddress.state() != null) {
            final String state = deliveryAddress.state().trim().toUpperCase(Locale.ROOT);
            if (!state.equals("SP") && !state.equals("SAO PAULO")) {
                freight = freight.multiply(INTERSTATE_MULTIPLIER);
            }
        }

        return freight.setScale(2, RoundingMode.HALF_UP);
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
        final UUID customerId,
        final BigDecimal orderTotal
    ) {
        if (paymentInfos == null || paymentInfos.isEmpty()) {
            throw new BusinessRuleException("Pelo menos uma forma de pagamento e obrigatoria");
        }

        final Map<UUID, Coupon> selectedCoupons = new LinkedHashMap<>();
        final Map<UUID, CardAllocation> selectedCards = new LinkedHashMap<>();
        boolean hasPromotionalCoupon = false;

        for (final PaymentInfo info : paymentInfos) {
            if (info == null) {
                continue;
            }

            CreditCard creditCard = null;
            Coupon coupon = null;

            if (info.couponId() != null) {
                coupon = couponGateway.findById(info.couponId())
                    .orElseThrow(() -> new EntityNotFoundException("Coupon", info.couponId()));

                if (coupon.customer() == null || !customerId.equals(coupon.customer().id())) {
                    throw new BusinessRuleException("O cupom '%s' nao pertence ao cliente informado".formatted(coupon.code()));
                }
                if (coupon.isUsed()) {
                    throw new BusinessRuleException("O cupom '%s' ja foi utilizado".formatted(coupon.code()));
                }
                if (coupon.expiresAt() != null && coupon.expiresAt().isBefore(Instant.now())) {
                    throw new BusinessRuleException("O cupom '%s' expirou".formatted(coupon.code()));
                }

                if (selectedCoupons.containsKey(coupon.id())) {
                    throw new BusinessRuleException("O cupom '%s' foi informado mais de uma vez".formatted(coupon.code()));
                }

                if (coupon.type() == rpgshop.domain.entity.coupon.constant.CouponType.PROMOTIONAL) {
                    if (hasPromotionalCoupon) {
                        throw new BusinessRuleException("Apenas um cupom promocional e permitido por pedido");
                    }
                    hasPromotionalCoupon = true;
                }

                selectedCoupons.put(coupon.id(), coupon);
            }

            if (info.creditCardId() != null) {
                if (!creditCardGateway.existsByIdAndCustomerId(info.creditCardId(), customerId)) {
                    throw new BusinessRuleException("O cartao selecionado nao pertence ao cliente informado");
                }
                creditCard = creditCardGateway.findById(info.creditCardId())
                    .orElseThrow(() -> new EntityNotFoundException("CreditCard", info.creditCardId()));
            }

            if (creditCard == null && coupon == null) {
                throw new BusinessRuleException("Cada pagamento deve informar cartao, cupom ou ambos");
            }

            if (creditCard != null) {
                if (info.amount() == null || info.amount().compareTo(ZERO) <= 0) {
                    throw new BusinessRuleException("O valor de cada pagamento com cartao deve ser maior que zero");
                }

                final CreditCard resolvedCard = creditCard;
                final BigDecimal resolvedAmount = info.amount();
                selectedCards.compute(
                    creditCard.id(),
                    (id, current) -> current == null
                        ? new CardAllocation(resolvedCard, resolvedAmount)
                        : new CardAllocation(current.card(), current.requestedAmount().add(resolvedAmount))
                );
            }
        }

        if (selectedCards.isEmpty() && selectedCoupons.isEmpty()) {
            throw new BusinessRuleException("Pelo menos uma forma de pagamento e obrigatoria");
        }

        final List<OrderPayment> payments = new ArrayList<>();
        BigDecimal remaining = orderTotal;

        final List<Coupon> prioritizedCoupons = selectedCoupons.values().stream()
            .sorted(Comparator.comparing(Coupon::value).reversed())
            .toList();

        for (final Coupon coupon : prioritizedCoupons) {
            if (remaining.compareTo(ZERO) <= 0) {
                break;
            }

            payments.add(OrderPayment.builder()
                .id(UUID.randomUUID())
                .coupon(coupon)
                .amount(coupon.value())
                .build());

            remaining = remaining.subtract(coupon.value());
        }

        if (remaining.compareTo(ZERO) > 0) {
            final boolean hasCoupon = !prioritizedCoupons.isEmpty();
            for (final CardAllocation cardAllocation : selectedCards.values()) {
                if (remaining.compareTo(ZERO) <= 0) {
                    break;
                }

                final BigDecimal cardAmount = remaining.min(cardAllocation.requestedAmount());
                if (cardAmount.compareTo(ZERO) <= 0) {
                    continue;
                }

                if (!hasCoupon && cardAmount.compareTo(MIN_CARD_AMOUNT) < 0) {
                    throw new BusinessRuleException("Cada cartao deve receber no minimo R$ 10,00");
                }

                payments.add(OrderPayment.builder()
                    .id(UUID.randomUUID())
                    .creditCard(cardAllocation.card())
                    .amount(cardAmount)
                    .build());

                remaining = remaining.subtract(cardAmount);
            }
        }

        if (remaining.compareTo(ZERO) > 0) {
            throw new BusinessRuleException(
                "O valor informado nos cartoes nao cobre o total restante de R$ %s".formatted(remaining)
            );
        }

        return payments;
    }

    private record CardAllocation(
        CreditCard card,
        BigDecimal requestedAmount
    ) {}

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
