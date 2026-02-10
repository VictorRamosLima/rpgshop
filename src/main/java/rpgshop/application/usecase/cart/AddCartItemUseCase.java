package rpgshop.application.usecase.cart;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.cart.AddCartItemCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.cart.CartGateway;
import rpgshop.application.gateway.cart.CartItemGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.cart.Cart;
import rpgshop.domain.entity.cart.CartItem;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.product.Product;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddCartItemUseCase {
    private static final int BLOCK_DURATION_MINUTES = 30;

    private final CartGateway cartGateway;
    private final CartItemGateway cartItemGateway;
    private final ProductGateway productGateway;
    private final CustomerGateway customerGateway;

    public AddCartItemUseCase(
        final CartGateway cartGateway,
        final CartItemGateway cartItemGateway,
        final ProductGateway productGateway,
        final CustomerGateway customerGateway
    ) {
        this.cartGateway = cartGateway;
        this.cartItemGateway = cartItemGateway;
        this.productGateway = productGateway;
        this.customerGateway = customerGateway;
    }

    @Nonnull
    @Transactional
    public Cart execute(@Nonnull final AddCartItemCommand command) {
        if (command.quantity() <= 0) {
            throw new BusinessRuleException("Quantity must be greater than zero");
        }

        final Customer customer = customerGateway.findById(command.customerId())
            .orElseThrow(() -> new EntityNotFoundException("Customer", command.customerId()));

        final Product product = productGateway.findById(command.productId())
            .orElseThrow(() -> new EntityNotFoundException("Product", command.productId()));

        if (!product.isActive()) {
            throw new BusinessRuleException("Cannot add inactive product to cart");
        }

        final int blockedQuantity = cartItemGateway.sumBlockedQuantityByProductId(product.id());
        final int availableStock = product.stockQuantity() - blockedQuantity;

        if (command.quantity() > availableStock) {
            throw new BusinessRuleException(
                "Insufficient stock. Available: %d, Requested: %d".formatted(availableStock, command.quantity())
            );
        }

        Cart cart = cartGateway.findByCustomerId(command.customerId())
            .orElseGet(() -> {
                final Cart newCart = Cart.builder()
                    .id(UUID.randomUUID())
                    .customer(customer)
                    .items(Collections.emptyList())
                    .build();
                return cartGateway.save(newCart);
            });

        final Optional<CartItem> existingItem = cartItemGateway
            .findByCartIdAndProductId(cart.id(), product.id());

        final Instant now = Instant.now();

        if (existingItem.isPresent()) {
            final CartItem updated = existingItem.get().toBuilder()
                .quantity(existingItem.get().quantity() + command.quantity())
                .isBlocked(true)
                .blockedAt(now)
                .expiresAt(now.plus(BLOCK_DURATION_MINUTES, ChronoUnit.MINUTES))
                .build();
            cartItemGateway.save(updated, cart.id());
        } else {
            final CartItem newItem = CartItem.builder()
                .id(UUID.randomUUID())
                .product(product)
                .quantity(command.quantity())
                .isBlocked(true)
                .blockedAt(now)
                .expiresAt(now.plus(BLOCK_DURATION_MINUTES, ChronoUnit.MINUTES))
                .build();
            cartItemGateway.save(newItem, cart.id());
        }

        return cartGateway.findById(cart.id()).orElse(cart);
    }
}
