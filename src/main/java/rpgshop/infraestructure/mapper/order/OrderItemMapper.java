package rpgshop.infraestructure.mapper.order;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.order.OrderItem;
import rpgshop.infraestructure.mapper.product.ProductMapper;
import rpgshop.infraestructure.persistence.entity.order.OrderItemJpaEntity;

public final class OrderItemMapper {
    private OrderItemMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static OrderItem toDomain(final OrderItemJpaEntity entity) {
        Assert.notNull(entity, "'OrderItemJpaEntity' should not be null");

        return OrderItem.builder()
            .id(entity.getId())
            .product(ProductMapper.toDomain(entity.getProduct()))
            .quantity(entity.getQuantity())
            .unitPrice(entity.getUnitPrice())
            .totalPrice(entity.getTotalPrice())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static OrderItemJpaEntity toEntity(final OrderItem domain) {
        Assert.notNull(domain, "'OrderItem' should not be null");

        return OrderItemJpaEntity.builder()
            .id(domain.id())
            .product(ProductMapper.toEntity(domain.product()))
            .quantity(domain.quantity())
            .unitPrice(domain.unitPrice())
            .totalPrice(domain.totalPrice())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();
    }
}
