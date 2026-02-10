package rpgshop.infraestructure.mapper.cart;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.cart.Cart;
import rpgshop.domain.entity.cart.CartItem;
import rpgshop.infraestructure.mapper.customer.CustomerMapper;
import rpgshop.infraestructure.persistence.entity.cart.CartItemJpaEntity;
import rpgshop.infraestructure.persistence.entity.cart.CartJpaEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class CartMapper {
    private CartMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static Cart toDomain(final CartJpaEntity entity) {
        Assert.notNull(entity, "'CartJpaEntity' should not be null");

        final List<CartItem> items = entity.getItems() != null
            ? entity.getItems().stream().map(CartItemMapper::toDomain).toList()
            : List.of();

        return Cart.builder()
            .id(entity.getId())
            .customer(CustomerMapper.toDomain(entity.getCustomer()))
            .items(items)
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static CartJpaEntity toEntity(final Cart domain) {
        Assert.notNull(domain, "'Cart' should not be null");

        final CartJpaEntity entity = CartJpaEntity.builder()
            .id(domain.id())
            .customer(CustomerMapper.toEntity(domain.customer()))
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();

        if (domain.items() != null) {
            final List<CartItemJpaEntity> items = domain
                .items()
                .stream()
                .map(itemDomain -> {
                    final CartItemJpaEntity itemEntity = CartItemMapper.toEntity(itemDomain);
                    itemEntity.setCart(entity);
                    return itemEntity;
                })
                .toList();
            entity.setItems(items);
        }

        return entity;
    }
}
