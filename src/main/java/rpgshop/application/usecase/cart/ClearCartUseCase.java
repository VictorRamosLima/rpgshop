package rpgshop.application.usecase.cart;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.cart.CartGateway;
import rpgshop.application.gateway.cart.CartItemGateway;
import rpgshop.domain.entity.cart.Cart;

import java.util.UUID;

@Service
public class ClearCartUseCase {
    private final CartGateway cartGateway;
    private final CartItemGateway cartItemGateway;

    public ClearCartUseCase(final CartGateway cartGateway, final CartItemGateway cartItemGateway) {
        this.cartGateway = cartGateway;
        this.cartItemGateway = cartItemGateway;
    }

    @Transactional
    public void execute(@Nonnull final UUID customerId) {
        final Cart cart = cartGateway.findByCustomerId(customerId)
            .orElseThrow(() -> new BusinessRuleException("Cart not found for customer"));

        cartItemGateway.deleteAllByCartId(cart.id());
    }
}
