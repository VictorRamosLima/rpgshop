package rpgshop.application.usecase.cart;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.gateway.cart.CartGateway;
import rpgshop.domain.entity.cart.Cart;

import java.util.Optional;
import java.util.UUID;

@Service
public class ViewCartUseCase {
    private final CartGateway cartGateway;

    public ViewCartUseCase(final CartGateway cartGateway) {
        this.cartGateway = cartGateway;
    }

    @Transactional(readOnly = true)
    public Optional<Cart> execute(@Nonnull final UUID customerId) {
        return cartGateway.findByCustomerId(customerId);
    }
}
