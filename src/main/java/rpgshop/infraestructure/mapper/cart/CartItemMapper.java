package rpgshop.infraestructure.mapper.cart;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.cart.CartItem;
import rpgshop.infraestructure.mapper.product.ProductMapper;
import rpgshop.infraestructure.persistence.entity.cart.CartItemJpaEntity;

public final class CartItemMapper {
    private CartItemMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static CartItem toDomain(final CartItemJpaEntity entity) {
        Assert.notNull(entity, "'CartItemJpaEntity' should not be null");

        return CartItem.builder()
            .id(entity.getId())
            .cartId(entity.getCart() != null ? entity.getCart().getId() : null)
            .product(ProductMapper.toDomain(entity.getProduct()))
            .quantity(entity.getQuantity())
            .isBlocked(entity.isBlocked())
            .blockedAt(entity.getBlockedAt())
            .expiresAt(entity.getExpiresAt())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static CartItemJpaEntity toEntity(final CartItem domain) {
        Assert.notNull(domain, "'CartItem' should not be null");

        return CartItemJpaEntity.builder()
            .id(domain.id())
            .product(ProductMapper.toEntity(domain.product()))
            .quantity(domain.quantity())
            .isBlocked(domain.isBlocked())
            .blockedAt(domain.blockedAt())
            .expiresAt(domain.expiresAt())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();
    }
}
