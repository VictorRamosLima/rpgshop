package rpgshop.application.usecase.cart;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.cart.UpdateCartItemCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.cart.CartGateway;
import rpgshop.application.gateway.cart.CartItemGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.cart.Cart;
import rpgshop.domain.entity.cart.CartItem;
import rpgshop.domain.entity.product.Product;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class UpdateCartItemQuantityUseCase {
    private static final int BLOCK_DURATION_MINUTES = 30;

    private final CartGateway cartGateway;
    private final CartItemGateway cartItemGateway;
    private final ProductGateway productGateway;

    public UpdateCartItemQuantityUseCase(
        final CartGateway cartGateway,
        final CartItemGateway cartItemGateway,
        final ProductGateway productGateway
    ) {
        this.cartGateway = cartGateway;
        this.cartItemGateway = cartItemGateway;
        this.productGateway = productGateway;
    }

    @Nonnull
    @Transactional
    public Cart execute(@Nonnull final UpdateCartItemCommand command) {
        if (command.quantity() <= 0) {
            throw new BusinessRuleException("Quantity must be greater than zero");
        }

        final Cart cart = cartGateway.findByCustomerId(command.customerId())
            .orElseThrow(() -> new BusinessRuleException("Cart not found for customer"));

        final CartItem item = cartItemGateway.findByCartIdAndProductId(cart.id(), command.productId())
            .orElseThrow(() -> new BusinessRuleException("Product not found in cart"));

        final Product product = productGateway.findById(command.productId())
            .orElseThrow(() -> new EntityNotFoundException("Product", command.productId()));

        final int blockedByOthers = cartItemGateway.sumBlockedQuantityByProductId(product.id()) - item.quantity();
        final int availableStock = product.stockQuantity() - blockedByOthers;

        if (command.quantity() > availableStock) {
            throw new BusinessRuleException(
                "Insufficient stock. Available: %d, Requested: %d".formatted(availableStock, command.quantity())
            );
        }

        final Instant now = Instant.now();
        final CartItem updated = item.toBuilder()
            .quantity(command.quantity())
            .isBlocked(true)
            .blockedAt(now)
            .expiresAt(now.plus(BLOCK_DURATION_MINUTES, ChronoUnit.MINUTES))
            .build();

        cartItemGateway.save(updated, cart.id());

        return cartGateway.findById(cart.id()).orElse(cart);
    }
}
