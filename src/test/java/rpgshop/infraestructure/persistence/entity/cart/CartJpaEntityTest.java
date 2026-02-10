package rpgshop.infraestructure.persistence.entity.cart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CartJpaEntity")
class CartJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final List<CartItemJpaEntity> items = new ArrayList<>();

            final CartJpaEntity entity = CartJpaEntity.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .customer(customer)
                .items(items)
                .build();

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(customer, entity.getCustomer());
            assertEquals(items, entity.getItems());
        }

        @Test
        @DisplayName("should create entity with default items list")
        void shouldCreateEntityWithDefaultItemsList() {
            final CartJpaEntity entity = CartJpaEntity.builder().build();

            assertNotNull(entity.getItems());
            assertTrue(entity.getItems().isEmpty());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final CartJpaEntity entity = CartJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getCustomer());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final CartJpaEntity entity = new CartJpaEntity();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getCustomer());
            assertNotNull(entity.getItems());
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
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final List<CartItemJpaEntity> items = new ArrayList<>();

            final CartJpaEntity entity = new CartJpaEntity(id, createdAt, updatedAt, customer, items);

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(customer, entity.getCustomer());
            assertEquals(items, entity.getItems());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final CartJpaEntity entity = new CartJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final List<CartItemJpaEntity> items = new ArrayList<>();

            entity.setId(id);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setCustomer(customer);
            entity.setItems(items);

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(customer, entity.getCustomer());
            assertEquals(items, entity.getItems());
        }
    }
}
