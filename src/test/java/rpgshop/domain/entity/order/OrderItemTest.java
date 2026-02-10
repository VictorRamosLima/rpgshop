package rpgshop.domain.entity.order;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.product.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderItemTest {
    @Test
    void shouldCreateOrderItem() {
        final UUID id = UUID.randomUUID();
        final Product product = Product.builder().id(UUID.randomUUID()).name("RPG Book").build();
        final Integer quantity = 3;
        final BigDecimal unitPrice = new BigDecimal("50.00");
        final BigDecimal totalPrice = new BigDecimal("150.00");
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final OrderItem orderItem = OrderItem.builder()
            .id(id)
            .product(product)
            .quantity(quantity)
            .unitPrice(unitPrice)
            .totalPrice(totalPrice)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, orderItem.id());
        assertEquals(product, orderItem.product());
        assertEquals(quantity, orderItem.quantity());
        assertEquals(unitPrice, orderItem.unitPrice());
        assertEquals(totalPrice, orderItem.totalPrice());
        assertEquals(createdAt, orderItem.createdAt());
        assertEquals(updatedAt, orderItem.updatedAt());
    }

    @Test
    void shouldCreateOrderItemWithNullValues() {
        final OrderItem orderItem = OrderItem.builder().build();

        assertNull(orderItem.id());
        assertNull(orderItem.product());
        assertNull(orderItem.quantity());
        assertNull(orderItem.unitPrice());
        assertNull(orderItem.totalPrice());
        assertNull(orderItem.createdAt());
        assertNull(orderItem.updatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final Integer originalQuantity = 3;
        final Integer newQuantity = 5;

        final OrderItem original = OrderItem.builder().quantity(originalQuantity).build();
        final OrderItem modified = original.toBuilder().quantity(newQuantity).build();

        assertEquals(originalQuantity, original.quantity());
        assertEquals(newQuantity, modified.quantity());
    }
}

