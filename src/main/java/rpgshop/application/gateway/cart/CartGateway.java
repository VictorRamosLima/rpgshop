package rpgshop.application.gateway.cart;

import rpgshop.domain.entity.cart.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartGateway {
    Cart save(final Cart cart);
    Optional<Cart> findById(final UUID id);
    Optional<Cart> findByCustomerId(final UUID customerId);
    boolean existsByCustomerId(final UUID customerId);
}
