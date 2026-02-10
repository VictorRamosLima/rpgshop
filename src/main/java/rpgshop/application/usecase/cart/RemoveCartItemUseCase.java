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
public class RemoveCartItemUseCase {
    private final CartGateway cartGateway;
    private final CartItemGateway cartItemGateway;

    public RemoveCartItemUseCase(final CartGateway cartGateway, final CartItemGateway cartItemGateway) {
        this.cartGateway = cartGateway;
        this.cartItemGateway = cartItemGateway;
    }

    @Nonnull
    @Transactional
    public Cart execute(@Nonnull final UUID customerId, @Nonnull final UUID productId) {
        final Cart cart = cartGateway.findByCustomerId(customerId)
            .orElseThrow(() -> new BusinessRuleException("Cart not found for customer"));

        cartItemGateway.deleteByCartIdAndProductId(cart.id(), productId);

        return cartGateway.findById(cart.id()).orElse(cart);
    }
}
