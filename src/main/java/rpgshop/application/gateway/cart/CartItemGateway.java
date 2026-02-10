package rpgshop.application.gateway.cart;

import rpgshop.domain.entity.cart.CartItem;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemGateway {
    CartItem save(final CartItem cartItem, final UUID cartId);
    Optional<CartItem> findById(final UUID id);
    List<CartItem> findByCartId(final UUID cartId);
    Optional<CartItem> findByCartIdAndProductId(final UUID cartId, final UUID productId);
    int deleteByCartIdAndProductId(final UUID cartId, final UUID productId);
    int deleteAllByCartId(final UUID cartId);
    List<CartItem> findExpiredBlockedItems(final Instant now);
    int sumBlockedQuantityByProductId(final UUID productId);
}
