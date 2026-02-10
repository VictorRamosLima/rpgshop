package rpgshop.domain.entity.exchange;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExchangeRequestTest {
    @Test
    void shouldCreateExchangeRequest() {
        final UUID id = UUID.randomUUID();
        final Order order = Order.builder().id(UUID.randomUUID()).build();
        final OrderItem orderItem = OrderItem.builder().id(UUID.randomUUID()).build();
        final int quantity = 2;
        final ExchangeStatus status = ExchangeStatus.REQUESTED;
        final String reason = "Product defective";
        final boolean returnToStock = false;
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final ExchangeRequest exchangeRequest = ExchangeRequest.builder()
            .id(id)
            .order(order)
            .orderItem(orderItem)
            .quantity(quantity)
            .status(status)
            .reason(reason)
            .authorizedAt(null)
            .receivedAt(null)
            .returnToStock(returnToStock)
            .coupon(null)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, exchangeRequest.id());
        assertEquals(order, exchangeRequest.order());
        assertEquals(orderItem, exchangeRequest.orderItem());
        assertEquals(quantity, exchangeRequest.quantity());
        assertEquals(status, exchangeRequest.status());
        assertEquals(reason, exchangeRequest.reason());
        assertNull(exchangeRequest.authorizedAt());
        assertNull(exchangeRequest.receivedAt());
        assertFalse(exchangeRequest.returnToStock());
        assertNull(exchangeRequest.coupon());
        assertEquals(createdAt, exchangeRequest.createdAt());
        assertEquals(updatedAt, exchangeRequest.updatedAt());
    }

    @Test
    void shouldCreateExchangeRequestWithAllStatuses() {
        for (ExchangeStatus status : ExchangeStatus.values()) {
            final ExchangeRequest exchangeRequest = ExchangeRequest.builder().status(status).build();
            assertEquals(status, exchangeRequest.status());
        }
    }

    @Test
    void shouldCreateExchangeRequestWithNullValues() {
        final ExchangeRequest exchangeRequest = ExchangeRequest.builder().build();

        assertNull(exchangeRequest.id());
        assertNull(exchangeRequest.order());
        assertNull(exchangeRequest.orderItem());
        assertEquals(0, exchangeRequest.quantity());
        assertNull(exchangeRequest.status());
        assertNull(exchangeRequest.reason());
        assertNull(exchangeRequest.authorizedAt());
        assertNull(exchangeRequest.receivedAt());
        assertFalse(exchangeRequest.returnToStock());
        assertNull(exchangeRequest.coupon());
        assertNull(exchangeRequest.createdAt());
        assertNull(exchangeRequest.updatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final ExchangeStatus originalStatus = ExchangeStatus.REQUESTED;
        final ExchangeStatus newStatus = ExchangeStatus.AUTHORIZED;

        final ExchangeRequest original = ExchangeRequest.builder().status(originalStatus).build();
        final ExchangeRequest modified = original.toBuilder().status(newStatus).build();

        assertEquals(originalStatus, original.status());
        assertEquals(newStatus, modified.status());
    }
}

