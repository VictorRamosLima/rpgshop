package rpgshop.infraestructure.mapper.exchange;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.infraestructure.mapper.coupon.CouponMapper;
import rpgshop.infraestructure.mapper.order.OrderItemMapper;
import rpgshop.infraestructure.mapper.order.OrderMapper;
import rpgshop.infraestructure.persistence.entity.exchange.ExchangeRequestJpaEntity;

public final class ExchangeRequestMapper {
    private ExchangeRequestMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static ExchangeRequest toDomain(final ExchangeRequestJpaEntity entity) {
        Assert.notNull(entity, "'ExchangeRequestJpaEntity' should not be null");

        return ExchangeRequest.builder()
            .id(entity.getId())
            .order(OrderMapper.toDomain(entity.getOrder()))
            .orderItem(OrderItemMapper.toDomain(entity.getOrderItem()))
            .quantity(entity.getQuantity())
            .status(entity.getStatus())
            .reason(entity.getReason())
            .authorizedAt(entity.getAuthorizedAt())
            .receivedAt(entity.getReceivedAt())
            .returnToStock(entity.isReturnToStock())
            .coupon(CouponMapper.toDomain(entity.getCoupon()))
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static ExchangeRequestJpaEntity toEntity(final ExchangeRequest domain) {
        Assert.notNull(domain, "'ExchangeRequest' should not be null");

        return ExchangeRequestJpaEntity.builder()
            .id(domain.id())
            .order(OrderMapper.toEntity(domain.order()))
            .orderItem(OrderItemMapper.toEntity(domain.orderItem()))
            .quantity(domain.quantity())
            .status(domain.status())
            .reason(domain.reason())
            .authorizedAt(domain.authorizedAt())
            .receivedAt(domain.receivedAt())
            .returnToStock(domain.returnToStock())
            .coupon(CouponMapper.toEntity(domain.coupon()))
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();
    }
}
