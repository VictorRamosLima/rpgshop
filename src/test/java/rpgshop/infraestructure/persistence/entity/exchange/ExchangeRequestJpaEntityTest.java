package rpgshop.infraestructure.persistence.entity.exchange;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.infraestructure.persistence.entity.coupon.CouponJpaEntity;
import rpgshop.infraestructure.persistence.entity.order.OrderItemJpaEntity;
import rpgshop.infraestructure.persistence.entity.order.OrderJpaEntity;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rpgshop.domain.entity.exchange.constant.ExchangeStatus.REQUESTED;

@DisplayName("ExchangeRequestJpaEntity")
class ExchangeRequestJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final OrderJpaEntity order = OrderJpaEntity.builder().build();
            final OrderItemJpaEntity orderItem = OrderItemJpaEntity.builder().build();
            final Integer quantity = 2;
            final ExchangeStatus status = ExchangeStatus.AUTHORIZED;
            final String reason = "Product defective";
            final Instant authorizedAt = Instant.now();
            final Instant receivedAt = Instant.now();
            final boolean returnToStock = true;
            final CouponJpaEntity coupon = CouponJpaEntity.builder().build();

            final ExchangeRequestJpaEntity entity = ExchangeRequestJpaEntity.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .order(order)
                .orderItem(orderItem)
                .quantity(quantity)
                .status(status)
                .reason(reason)
                .authorizedAt(authorizedAt)
                .receivedAt(receivedAt)
                .returnToStock(returnToStock)
                .coupon(coupon)
                .build();

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(order, entity.getOrder());
            assertEquals(orderItem, entity.getOrderItem());
            assertEquals(quantity, entity.getQuantity());
            assertEquals(status, entity.getStatus());
            assertEquals(reason, entity.getReason());
            assertEquals(authorizedAt, entity.getAuthorizedAt());
            assertEquals(receivedAt, entity.getReceivedAt());
            assertTrue(entity.isReturnToStock());
            assertEquals(coupon, entity.getCoupon());
        }

        @Test
        @DisplayName("should create entity with all exchange status types")
        void shouldCreateEntityWithAllExchangeStatusTypes() {
            for (ExchangeStatus status : ExchangeStatus.values()) {
                final ExchangeRequestJpaEntity entity = ExchangeRequestJpaEntity.builder()
                    .status(status)
                    .build();

                assertEquals(status, entity.getStatus());
            }
        }

        @Test
        @DisplayName("should create entity with default status as REQUESTED")
        void shouldCreateEntityWithDefaultStatusAsRequested() {
            final ExchangeRequestJpaEntity entity = ExchangeRequestJpaEntity.builder().build();
            assertEquals(REQUESTED, entity.getStatus());
        }

        @Test
        @DisplayName("should create entity with default returnToStock as false")
        void shouldCreateEntityWithDefaultReturnToStockAsFalse() {
            final ExchangeRequestJpaEntity entity = ExchangeRequestJpaEntity.builder().build();
            assertFalse(entity.isReturnToStock());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final ExchangeRequestJpaEntity entity = ExchangeRequestJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getOrder());
            assertNull(entity.getOrderItem());
            assertNull(entity.getQuantity());
            assertNull(entity.getReason());
            assertNull(entity.getAuthorizedAt());
            assertNull(entity.getReceivedAt());
            assertNull(entity.getCoupon());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final ExchangeRequestJpaEntity entity = new ExchangeRequestJpaEntity();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getOrder());
            assertNull(entity.getOrderItem());
            assertNull(entity.getQuantity());
            assertEquals(REQUESTED, entity.getStatus());
            assertNull(entity.getReason());
            assertNull(entity.getAuthorizedAt());
            assertNull(entity.getReceivedAt());
            assertFalse(entity.isReturnToStock());
            assertNull(entity.getCoupon());
        }
    }

    @Nested
    @DisplayName("AllArgsConstructor")
    class AllArgsConstructorTests {
        @Test
        @DisplayName("should create entity with all args")
        void shouldCreateEntityWithAllArgs() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final OrderJpaEntity order = OrderJpaEntity.builder().build();
            final OrderItemJpaEntity orderItem = OrderItemJpaEntity.builder().build();
            final Integer quantity = 3;
            final ExchangeStatus status = ExchangeStatus.COMPLETED;
            final String reason = "Wrong size";
            final Instant authorizedAt = Instant.now();
            final Instant receivedAt = Instant.now();
            final boolean returnToStock = false;
            final CouponJpaEntity coupon = CouponJpaEntity.builder().build();

            final ExchangeRequestJpaEntity entity = new ExchangeRequestJpaEntity(
                id, createdAt, updatedAt, order, orderItem, quantity, status, reason,
                authorizedAt, receivedAt, returnToStock, coupon
            );

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(order, entity.getOrder());
            assertEquals(orderItem, entity.getOrderItem());
            assertEquals(quantity, entity.getQuantity());
            assertEquals(status, entity.getStatus());
            assertEquals(reason, entity.getReason());
            assertEquals(authorizedAt, entity.getAuthorizedAt());
            assertEquals(receivedAt, entity.getReceivedAt());
            assertFalse(entity.isReturnToStock());
            assertEquals(coupon, entity.getCoupon());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final ExchangeRequestJpaEntity entity = new ExchangeRequestJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final OrderJpaEntity order = OrderJpaEntity.builder().build();
            final OrderItemJpaEntity orderItem = OrderItemJpaEntity.builder().build();
            final Integer quantity = 1;
            final ExchangeStatus status = ExchangeStatus.DENIED;
            final String reason = "Customer changed mind";
            final Instant authorizedAt = Instant.now();
            final Instant receivedAt = Instant.now();
            final CouponJpaEntity coupon = CouponJpaEntity.builder().build();

            entity.setId(id);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setOrder(order);
            entity.setOrderItem(orderItem);
            entity.setQuantity(quantity);
            entity.setStatus(status);
            entity.setReason(reason);
            entity.setAuthorizedAt(authorizedAt);
            entity.setReceivedAt(receivedAt);
            entity.setReturnToStock(true);
            entity.setCoupon(coupon);

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(order, entity.getOrder());
            assertEquals(orderItem, entity.getOrderItem());
            assertEquals(quantity, entity.getQuantity());
            assertEquals(status, entity.getStatus());
            assertEquals(reason, entity.getReason());
            assertEquals(authorizedAt, entity.getAuthorizedAt());
            assertEquals(receivedAt, entity.getReceivedAt());
            assertTrue(entity.isReturnToStock());
            assertEquals(coupon, entity.getCoupon());
        }
    }
}
