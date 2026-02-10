package rpgshop.domain.entity.order;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderTest {
    @Test
    void shouldCreateOrder() {
        final UUID id = UUID.randomUUID();
        final Customer customer = Customer.builder().id(UUID.randomUUID()).build();
        final OrderStatus status = OrderStatus.PROCESSING;
        final Address deliveryAddress = Address.builder().city("São Paulo").build();
        final BigDecimal freightCost = new BigDecimal("15.00");
        final BigDecimal subtotal = new BigDecimal("100.00");
        final BigDecimal total = new BigDecimal("115.00");
        final Instant purchasedAt = Instant.now();
        final List<OrderItem> items = List.of();
        final List<OrderPayment> payments = List.of();
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final Order order = Order.builder()
            .id(id)
            .customer(customer)
            .status(status)
            .deliveryAddress(deliveryAddress)
            .freightCost(freightCost)
            .subtotal(subtotal)
            .total(total)
            .purchasedAt(purchasedAt)
            .dispatchedAt(null)
            .deliveredAt(null)
            .items(items)
            .payments(payments)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, order.id());
        assertEquals(customer, order.customer());
        assertEquals(status, order.status());
        assertEquals(deliveryAddress, order.deliveryAddress());
        assertEquals(freightCost, order.freightCost());
        assertEquals(subtotal, order.subtotal());
        assertEquals(total, order.total());
        assertEquals(purchasedAt, order.purchasedAt());
        assertNull(order.dispatchedAt());
        assertNull(order.deliveredAt());
        assertEquals(items, order.items());
        assertEquals(payments, order.payments());
        assertEquals(createdAt, order.createdAt());
        assertEquals(updatedAt, order.updatedAt());
    }

    @Test
    void shouldCreateOrderWithAllStatuses() {
        for (OrderStatus status : OrderStatus.values()) {
            final Order order = Order.builder().status(status).build();
            assertEquals(status, order.status());
        }
    }

    @Test
    void shouldCreateOrderWithNullValues() {
        final Order order = Order.builder().build();

        assertNull(order.id());
        assertNull(order.customer());
        assertNull(order.status());
        assertNull(order.deliveryAddress());
        assertNull(order.freightCost());
        assertNull(order.subtotal());
        assertNull(order.total());
        assertNull(order.purchasedAt());
        assertNull(order.dispatchedAt());
        assertNull(order.deliveredAt());
        assertNull(order.items());
        assertNull(order.payments());
        assertNull(order.createdAt());
        assertNull(order.updatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final OrderStatus originalStatus = OrderStatus.PROCESSING;
        final OrderStatus newStatus = OrderStatus.APPROVED;

        final Order original = Order.builder().status(originalStatus).build();
        final Order modified = original.toBuilder().status(newStatus).build();

        assertEquals(originalStatus, original.status());
        assertEquals(newStatus, modified.status());
    }
}

