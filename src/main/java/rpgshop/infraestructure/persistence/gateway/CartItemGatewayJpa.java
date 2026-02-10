package rpgshop.infraestructure.persistence.gateway;

import org.springframework.stereotype.Component;
import rpgshop.application.gateway.cart.CartItemGateway;
import rpgshop.domain.entity.cart.CartItem;
import rpgshop.infraestructure.mapper.cart.CartItemMapper;
import rpgshop.infraestructure.persistence.entity.cart.CartItemJpaEntity;
import rpgshop.infraestructure.persistence.repository.cart.CartItemRepository;
import rpgshop.infraestructure.persistence.repository.cart.CartRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CartItemGatewayJpa implements CartItemGateway {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    public CartItemGatewayJpa(final CartItemRepository cartItemRepository, final CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public CartItem save(final CartItem cartItem, final UUID cartId) {
        final CartItemJpaEntity entity = CartItemMapper.toEntity(cartItem);
        cartRepository.findById(cartId).ifPresent(entity::setCart);
        final CartItemJpaEntity saved = cartItemRepository.save(entity);
        return CartItemMapper.toDomain(saved);
    }

    @Override
    public Optional<CartItem> findById(final UUID id) {
        return cartItemRepository.findById(id).map(CartItemMapper::toDomain);
    }

    @Override
    public List<CartItem> findByCartId(final UUID cartId) {
        return cartItemRepository.findByCartId(cartId)
            .stream().map(CartItemMapper::toDomain).toList();
    }

    @Override
    public Optional<CartItem> findByCartIdAndProductId(final UUID cartId, final UUID productId) {
        return cartItemRepository.findByCartIdAndProductId(cartId, productId)
            .map(CartItemMapper::toDomain);
    }

    @Override
    public int deleteByCartIdAndProductId(final UUID cartId, final UUID productId) {
        return cartItemRepository.deleteByCartIdAndProductId(cartId, productId);
    }

    @Override
    public int deleteAllByCartId(final UUID cartId) {
        return cartItemRepository.deleteAllByCartId(cartId);
    }

    @Override
    public List<CartItem> findExpiredBlockedItems(final Instant now) {
        return cartItemRepository.findExpiredBlockedItems(now)
            .stream().map(CartItemMapper::toDomain).toList();
    }

    @Override
    public int sumBlockedQuantityByProductId(final UUID productId) {
        return cartItemRepository.sumBlockedQuantityByProductId(productId);
    }
}
