package rpgshop.domain.entity.cart;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.product.Product;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartItemTest {
    @Test
    void shouldCreateCartItem() {
        final UUID id = UUID.randomUUID();
        final UUID cartId = UUID.randomUUID();
        final Product product = Product.builder().id(UUID.randomUUID()).build();
        final int quantity = 5;
        final boolean isBlocked = true;
        final Instant blockedAt = Instant.now();
        final Instant expiresAt = Instant.now().plusSeconds(3600);
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final CartItem cartItem = CartItem.builder()
            .id(id)
            .cartId(cartId)
            .product(product)
            .quantity(quantity)
            .isBlocked(isBlocked)
            .blockedAt(blockedAt)
            .expiresAt(expiresAt)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, cartItem.id());
        assertEquals(cartId, cartItem.cartId());
        assertEquals(product, cartItem.product());
        assertEquals(quantity, cartItem.quantity());
        assertTrue(cartItem.isBlocked());
        assertEquals(blockedAt, cartItem.blockedAt());
        assertEquals(expiresAt, cartItem.expiresAt());
        assertEquals(createdAt, cartItem.createdAt());
        assertEquals(updatedAt, cartItem.updatedAt());
    }

    @Test
    void shouldCreateCartItemWithNullValues() {
        final CartItem cartItem = CartItem.builder().build();

        assertNull(cartItem.id());
        assertNull(cartItem.cartId());
        assertNull(cartItem.product());
        assertEquals(0, cartItem.quantity());
        assertFalse(cartItem.isBlocked());
        assertNull(cartItem.blockedAt());
        assertNull(cartItem.expiresAt());
        assertNull(cartItem.createdAt());
        assertNull(cartItem.updatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final int originalQuantity = 3;
        final int newQuantity = 10;

        final CartItem original = CartItem.builder().quantity(originalQuantity).build();
        final CartItem modified = original.toBuilder().quantity(newQuantity).build();

        assertEquals(originalQuantity, original.quantity());
        assertEquals(newQuantity, modified.quantity());
    }
}

