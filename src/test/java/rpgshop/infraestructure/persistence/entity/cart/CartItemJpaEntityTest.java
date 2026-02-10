package rpgshop.infraestructure.persistence.entity.cart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CartItemJpaEntity")
class CartItemJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final CartJpaEntity cart = CartJpaEntity.builder().build();
            final ProductJpaEntity product = ProductJpaEntity.builder().build();
            final Integer quantity = 5;
            final boolean isBlocked = true;
            final Instant blockedAt = Instant.now();
            final Instant expiresAt = Instant.now().plusSeconds(3600);

            final CartItemJpaEntity entity = CartItemJpaEntity.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .isBlocked(isBlocked)
                .blockedAt(blockedAt)
                .expiresAt(expiresAt)
                .build();

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(cart, entity.getCart());
            assertEquals(product, entity.getProduct());
            assertEquals(quantity, entity.getQuantity());
            assertTrue(entity.isBlocked());
            assertEquals(blockedAt, entity.getBlockedAt());
            assertEquals(expiresAt, entity.getExpiresAt());
        }

        @Test
        @DisplayName("should create entity with default isBlocked as true")
        void shouldCreateEntityWithDefaultIsBlockedAsTrue() {
            final CartItemJpaEntity entity = CartItemJpaEntity.builder().build();
            assertTrue(entity.isBlocked());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final CartItemJpaEntity entity = CartItemJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getCart());
            assertNull(entity.getProduct());
            assertNull(entity.getQuantity());
            assertNull(entity.getBlockedAt());
            assertNull(entity.getExpiresAt());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final CartItemJpaEntity entity = new CartItemJpaEntity();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getCart());
            assertNull(entity.getProduct());
            assertNull(entity.getQuantity());
            assertTrue(entity.isBlocked());
            assertNull(entity.getBlockedAt());
            assertNull(entity.getExpiresAt());
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
            final CartJpaEntity cart = CartJpaEntity.builder().build();
            final ProductJpaEntity product = ProductJpaEntity.builder().build();
            final Integer quantity = 10;
            final boolean isBlocked = false;
            final Instant blockedAt = Instant.now();
            final Instant expiresAt = Instant.now().plusSeconds(7200);

            final CartItemJpaEntity entity = new CartItemJpaEntity(
                id,
                createdAt,
                updatedAt,
                cart,
                product,
                quantity,
                isBlocked,
                blockedAt,
                expiresAt
            );

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(cart, entity.getCart());
            assertEquals(product, entity.getProduct());
            assertEquals(quantity, entity.getQuantity());
            assertFalse(entity.isBlocked());
            assertEquals(blockedAt, entity.getBlockedAt());
            assertEquals(expiresAt, entity.getExpiresAt());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final CartItemJpaEntity entity = new CartItemJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final CartJpaEntity cart = CartJpaEntity.builder().build();
            final ProductJpaEntity product = ProductJpaEntity.builder().build();
            final Integer quantity = 3;
            final Instant blockedAt = Instant.now();
            final Instant expiresAt = Instant.now().plusSeconds(1800);

            entity.setId(id);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setCart(cart);
            entity.setProduct(product);
            entity.setQuantity(quantity);
            entity.setBlocked(true);
            entity.setBlockedAt(blockedAt);
            entity.setExpiresAt(expiresAt);

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(cart, entity.getCart());
            assertEquals(product, entity.getProduct());
            assertEquals(quantity, entity.getQuantity());
            assertTrue(entity.isBlocked());
            assertEquals(blockedAt, entity.getBlockedAt());
            assertEquals(expiresAt, entity.getExpiresAt());
        }
    }
}
