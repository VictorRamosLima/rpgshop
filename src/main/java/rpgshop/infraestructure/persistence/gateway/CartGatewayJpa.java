package rpgshop.infraestructure.persistence.gateway;

import org.springframework.stereotype.Component;
import rpgshop.application.gateway.cart.CartGateway;
import rpgshop.domain.entity.cart.Cart;
import rpgshop.infraestructure.mapper.cart.CartMapper;
import rpgshop.infraestructure.persistence.entity.cart.CartJpaEntity;
import rpgshop.infraestructure.persistence.repository.cart.CartRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class CartGatewayJpa implements CartGateway {
    private final CartRepository cartRepository;

    public CartGatewayJpa(final CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Cart save(final Cart cart) {
        final CartJpaEntity entity = CartMapper.toEntity(cart);
        final CartJpaEntity saved = cartRepository.save(entity);
        return CartMapper.toDomain(saved);
    }

    @Override
    public Optional<Cart> findById(final UUID id) {
        return cartRepository.findById(id).map(CartMapper::toDomain);
    }

    @Override
    public Optional<Cart> findByCustomerId(final UUID customerId) {
        return cartRepository.findByCustomerId(customerId).map(CartMapper::toDomain);
    }

    @Override
    public boolean existsByCustomerId(final UUID customerId) {
        return cartRepository.existsByCustomerId(customerId);
    }
}
