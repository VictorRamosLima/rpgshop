package rpgshop.application.usecase.order;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.coupon.CouponGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.command.order.CardOperatorDecision;
import rpgshop.application.gateway.order.CardOperatorGateway;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;
import rpgshop.domain.entity.order.OrderPayment;
import rpgshop.domain.entity.order.constant.OrderStatus;
import rpgshop.domain.entity.product.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class ApproveOrderUseCase {
    private static final BigDecimal MAX_RANKING = new BigDecimal("999.99");

    private final OrderGateway orderGateway;
    private final CardOperatorGateway cardOperatorGateway;
    private final ProductGateway productGateway;
    private final CouponGateway couponGateway;
    private final CustomerGateway customerGateway;

    public ApproveOrderUseCase(
        final OrderGateway orderGateway,
        final CardOperatorGateway cardOperatorGateway,
        final ProductGateway productGateway,
        final CouponGateway couponGateway,
        final CustomerGateway customerGateway
    ) {
        this.orderGateway = orderGateway;
        this.cardOperatorGateway = cardOperatorGateway;
        this.productGateway = productGateway;
        this.couponGateway = couponGateway;
        this.customerGateway = customerGateway;
    }

    @Nonnull
    @Transactional
    public Order execute(@Nonnull final UUID orderId) {
        final Order order = orderGateway.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order", orderId));

        if (order.status() != OrderStatus.PROCESSING) {
            throw new BusinessRuleException("Somente pedidos com status EM PROCESSAMENTO podem ser aprovados");
        }

        final Instant approvalInstant = Instant.now();
        validateCouponsForApproval(order, approvalInstant);

        final CardOperatorDecision authorization = cardOperatorGateway.authorize(
            order.customer().id(),
            order.total(),
            order.payments()
        );

        if (!authorization.approved()) {
            final Order rejected = order.toBuilder()
                .status(OrderStatus.REJECTED)
                .build();
            return orderGateway.save(rejected);
        }

        for (final OrderItem item : order.items()) {
            final Product product = productGateway.findById(item.product().id())
                .orElseThrow(() -> new EntityNotFoundException("Product", item.product().id()));

            if (product.stockQuantity() < item.quantity()) {
                throw new BusinessRuleException(
                    "Insufficient stock for product '%s'. Available: %d, Requested: %d"
                        .formatted(product.name(), product.stockQuantity(), item.quantity())
                );
            }

            final Product updated = product.toBuilder()
                .stockQuantity(product.stockQuantity() - item.quantity())
                .build();
            productGateway.save(updated);
        }

        markCouponsAsUsed(order.payments(), approvalInstant);
        generateChangeCouponIfNeeded(order, approvalInstant);
        updateCustomerRanking(order.customer().id(), order);

        final Order approved = order.toBuilder()
            .status(OrderStatus.APPROVED)
            .build();

        return orderGateway.save(approved);
    }

    private void validateCouponsForApproval(final Order order, final Instant referenceInstant) {
        final Set<UUID> processedCoupons = new HashSet<>();

        for (final OrderPayment payment : order.payments()) {
            if (payment.coupon() == null || payment.coupon().id() == null) {
                continue;
            }

            if (!processedCoupons.add(payment.coupon().id())) {
                continue;
            }

            final Coupon coupon = couponGateway.findById(payment.coupon().id())
                .orElseThrow(() -> new EntityNotFoundException("Coupon", payment.coupon().id()));

            if (coupon.customer() == null || coupon.customer().id() == null
                || !order.customer().id().equals(coupon.customer().id())
            ) {
                throw new BusinessRuleException("O cupom '%s' nao pertence ao cliente do pedido".formatted(coupon.code()));
            }
            if (coupon.isUsed()) {
                throw new BusinessRuleException("O cupom '%s' ja foi utilizado".formatted(coupon.code()));
            }
            if (coupon.expiresAt() != null && coupon.expiresAt().isBefore(referenceInstant)) {
                throw new BusinessRuleException("O cupom '%s' expirou".formatted(coupon.code()));
            }
        }
    }

    private void markCouponsAsUsed(final java.util.List<OrderPayment> payments, final Instant usedAt) {
        final Set<UUID> processedCoupons = new HashSet<>();

        for (final OrderPayment payment : payments) {
            if (payment.coupon() == null || payment.coupon().id() == null) {
                continue;
            }

            if (!processedCoupons.add(payment.coupon().id())) {
                continue;
            }

            final Coupon coupon = couponGateway.findById(payment.coupon().id())
                .orElseThrow(() -> new EntityNotFoundException("Coupon", payment.coupon().id()));

            final Coupon updatedCoupon = coupon.toBuilder()
                .isUsed(true)
                .usedAt(usedAt)
                .build();
            couponGateway.save(updatedCoupon);
        }
    }

    private void generateChangeCouponIfNeeded(final Order order, final Instant referenceInstant) {
        final BigDecimal couponPaid = order.payments().stream()
            .filter(payment -> payment.coupon() != null)
            .map(OrderPayment::amount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (couponPaid.compareTo(order.total()) <= 0) {
            return;
        }

        final BigDecimal changeValue = couponPaid.subtract(order.total()).setScale(2, RoundingMode.HALF_UP);
        if (changeValue.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        final Coupon changeCoupon = Coupon.builder()
            .id(UUID.randomUUID())
            .code(generateUniqueChangeCouponCode())
            .type(CouponType.EXCHANGE)
            .value(changeValue)
            .customer(order.customer())
            .isUsed(false)
            .expiresAt(referenceInstant.plus(90, ChronoUnit.DAYS))
            .build();

        couponGateway.save(changeCoupon);
    }

    private String generateUniqueChangeCouponCode() {
        String candidateCode;
        do {
            candidateCode = "TROCO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (couponGateway.existsByCode(candidateCode));
        return candidateCode;
    }

    private void updateCustomerRanking(final UUID customerId, final Order approvedOrder) {
        final Customer currentCustomer = customerGateway.findById(customerId)
            .orElseThrow(() -> new EntityNotFoundException("Customer", customerId));

        final BigDecimal currentRanking = currentCustomer.ranking() == null
            ? BigDecimal.ZERO
            : currentCustomer.ranking();
        final BigDecimal increment = calculateRankingIncrement(approvedOrder);

        BigDecimal newRanking = currentRanking.add(increment);
        if (newRanking.compareTo(MAX_RANKING) > 0) {
            newRanking = MAX_RANKING;
        }

        customerGateway.save(currentCustomer.toBuilder()
            .ranking(newRanking.setScale(2, RoundingMode.HALF_UP))
            .build());
    }

    private BigDecimal calculateRankingIncrement(final Order approvedOrder) {
        final int purchasedQuantity = approvedOrder.items().stream()
            .mapToInt(OrderItem::quantity)
            .sum();

        final long paymentSources = approvedOrder.payments().stream()
            .filter(payment -> payment.creditCard() != null || payment.coupon() != null)
            .count();

        final BigDecimal orderValueScore = approvedOrder.total()
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        final BigDecimal quantityScore = BigDecimal.valueOf(purchasedQuantity).multiply(new BigDecimal("0.10"));
        final BigDecimal paymentScore = BigDecimal.valueOf(paymentSources).multiply(new BigDecimal("0.05"));

        return orderValueScore
            .add(quantityScore)
            .add(paymentScore)
            .setScale(2, RoundingMode.HALF_UP);
    }
}
