package rpgshop.infraestructure.persistence.entity.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("OrderItemJpaEntity")
class OrderItemJpaEntityTest {
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
            final ProductJpaEntity product = ProductJpaEntity.builder().build();
            final Integer quantity = 3;
            final BigDecimal unitPrice = new BigDecimal("29.99");
            final BigDecimal totalPrice = new BigDecimal("89.97");

            final OrderItemJpaEntity entity = OrderItemJpaEntity.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .order(order)
                .product(product)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .build();

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(order, entity.getOrder());
            assertEquals(product, entity.getProduct());
            assertEquals(quantity, entity.getQuantity());
            assertEquals(unitPrice, entity.getUnitPrice());
            assertEquals(totalPrice, entity.getTotalPrice());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final OrderItemJpaEntity entity = OrderItemJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getOrder());
            assertNull(entity.getProduct());
            assertNull(entity.getQuantity());
            assertNull(entity.getUnitPrice());
            assertNull(entity.getTotalPrice());
        }

        @Test
        @DisplayName("should calculate total price correctly")
        void shouldCalculateTotalPriceCorrectly() {
            final Integer quantity = 5;
            final BigDecimal unitPrice = new BigDecimal("10.00");
            final BigDecimal totalPrice = new BigDecimal("50.00");

            final OrderItemJpaEntity entity = OrderItemJpaEntity.builder()
                .quantity(quantity)
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .build();

            assertEquals(quantity, entity.getQuantity());
            assertEquals(unitPrice, entity.getUnitPrice());
            assertEquals(totalPrice, entity.getTotalPrice());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final OrderItemJpaEntity entity = new OrderItemJpaEntity();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getOrder());
            assertNull(entity.getProduct());
            assertNull(entity.getQuantity());
            assertNull(entity.getUnitPrice());
            assertNull(entity.getTotalPrice());
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
            final ProductJpaEntity product = ProductJpaEntity.builder().build();
            final Integer quantity = 2;
            final BigDecimal unitPrice = new BigDecimal("49.99");
            final BigDecimal totalPrice = new BigDecimal("99.98");

            final OrderItemJpaEntity entity = new OrderItemJpaEntity(
                id, createdAt, updatedAt, order, product, quantity, unitPrice, totalPrice
            );

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(order, entity.getOrder());
            assertEquals(product, entity.getProduct());
            assertEquals(quantity, entity.getQuantity());
            assertEquals(unitPrice, entity.getUnitPrice());
            assertEquals(totalPrice, entity.getTotalPrice());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final OrderItemJpaEntity entity = new OrderItemJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final OrderJpaEntity order = OrderJpaEntity.builder().build();
            final ProductJpaEntity product = ProductJpaEntity.builder().build();
            final Integer quantity = 4;
            final BigDecimal unitPrice = new BigDecimal("25.00");
            final BigDecimal totalPrice = new BigDecimal("100.00");

            entity.setId(id);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setOrder(order);
            entity.setProduct(product);
            entity.setQuantity(quantity);
            entity.setUnitPrice(unitPrice);
            entity.setTotalPrice(totalPrice);

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(order, entity.getOrder());
            assertEquals(product, entity.getProduct());
            assertEquals(quantity, entity.getQuantity());
            assertEquals(unitPrice, entity.getUnitPrice());
            assertEquals(totalPrice, entity.getTotalPrice());
        }
    }
}
