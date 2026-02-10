package rpgshop.infraestructure.mapper.order;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.order.OrderPayment;
import rpgshop.infraestructure.mapper.coupon.CouponMapper;
import rpgshop.infraestructure.mapper.customer.CreditCardMapper;
import rpgshop.infraestructure.persistence.entity.order.OrderPaymentJpaEntity;

public final class OrderPaymentMapper {
    private OrderPaymentMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static OrderPayment toDomain(final OrderPaymentJpaEntity entity) {
        Assert.notNull(entity, "'OrderPaymentJpaEntity' should not be null");

        return OrderPayment.builder()
            .id(entity.getId())
            .creditCard(entity.getCreditCard() != null ? CreditCardMapper.toDomain(entity.getCreditCard()) : null)
            .coupon(entity.getCoupon() != null ? CouponMapper.toDomain(entity.getCoupon()) : null)
            .amount(entity.getAmount())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static OrderPaymentJpaEntity toEntity(final OrderPayment domain) {
        Assert.notNull(domain, "'OrderPayment' should not be null");

        return OrderPaymentJpaEntity.builder()
            .id(domain.id())
            .creditCard(domain.creditCard() != null ? CreditCardMapper.toEntity(domain.creditCard()) : null)
            .coupon(domain.coupon() != null ? CouponMapper.toEntity(domain.coupon()) : null)
            .amount(domain.amount())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();
    }
}
