package rpgshop.domain.entity.cart;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.customer.Customer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CartTest {
    @Test
    void shouldCreateCart() {
        final UUID id = UUID.randomUUID();
        final Customer customer = Customer.builder().id(UUID.randomUUID()).build();
        final List<CartItem> items = List.of();
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final Cart cart = Cart.builder()
            .id(id)
            .customer(customer)
            .items(items)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, cart.id());
        assertEquals(customer, cart.customer());
        assertEquals(items, cart.items());
        assertEquals(createdAt, cart.createdAt());
        assertEquals(updatedAt, cart.updatedAt());
    }

    @Test
    void shouldCreateCartWithNullValues() {
        final Cart cart = Cart.builder().build();

        assertNull(cart.id());
        assertNull(cart.customer());
        assertNull(cart.items());
        assertNull(cart.createdAt());
        assertNull(cart.updatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final UUID originalId = UUID.randomUUID();
        final UUID newId = UUID.randomUUID();

        final Cart original = Cart.builder().id(originalId).build();
        final Cart modified = original.toBuilder().id(newId).build();

        assertEquals(originalId, original.id());
        assertEquals(newId, modified.id());
    }
}

