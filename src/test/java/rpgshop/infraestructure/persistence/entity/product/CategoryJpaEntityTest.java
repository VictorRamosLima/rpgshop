package rpgshop.infraestructure.persistence.entity.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CategoryJpaEntity")
class CategoryJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final boolean isActive = true;
            final Instant deactivatedAt = null;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final String name = "Weapons";
            final String description = "All types of weapons for RPG";
            final List<ProductJpaEntity> products = new ArrayList<>();

            final CategoryJpaEntity entity = CategoryJpaEntity.builder()
                .id(id)
                .isActive(isActive)
                .deactivatedAt(deactivatedAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .name(name)
                .description(description)
                .products(products)
                .build();

            assertEquals(id, entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(name, entity.getName());
            assertEquals(description, entity.getDescription());
            assertEquals(products, entity.getProducts());
        }

        @Test
        @DisplayName("should create entity with default isActive as true")
        void shouldCreateEntityWithDefaultIsActiveAsTrue() {
            final CategoryJpaEntity entity = CategoryJpaEntity.builder().build();
            assertTrue(entity.isActive());
        }

        @Test
        @DisplayName("should create entity with default empty products list")
        void shouldCreateEntityWithDefaultEmptyProductsList() {
            final CategoryJpaEntity entity = CategoryJpaEntity.builder().build();

            assertNotNull(entity.getProducts());
            assertTrue(entity.getProducts().isEmpty());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final CategoryJpaEntity entity = CategoryJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getName());
            assertNull(entity.getDescription());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final CategoryJpaEntity entity = new CategoryJpaEntity();

            assertNull(entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getName());
            assertNull(entity.getDescription());
            assertNotNull(entity.getProducts());
        }
    }

    @Nested
    @DisplayName("AllArgsConstructor")
    class AllArgsConstructorTests {
        @Test
        @DisplayName("should create entity with all args")
        void shouldCreateEntityWithAllArgs() {
            final UUID id = UUID.randomUUID();
            final boolean isActive = false;
            final Instant deactivatedAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final String name = "Armor";
            final String description = "Protective gear for heroes";
            final List<ProductJpaEntity> products = new ArrayList<>();

            final CategoryJpaEntity entity = new CategoryJpaEntity(
                id, isActive, deactivatedAt, createdAt, updatedAt, name, description, products
            );

            assertEquals(id, entity.getId());
            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(name, entity.getName());
            assertEquals(description, entity.getDescription());
            assertEquals(products, entity.getProducts());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final CategoryJpaEntity entity = new CategoryJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant deactivatedAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final String name = "Potions";
            final String description = "Healing and buff potions";
            final List<ProductJpaEntity> products = new ArrayList<>();

            entity.setId(id);
            entity.setActive(true);
            entity.setDeactivatedAt(deactivatedAt);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setName(name);
            entity.setDescription(description);
            entity.setProducts(products);

            assertEquals(id, entity.getId());
            assertTrue(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(name, entity.getName());
            assertEquals(description, entity.getDescription());
            assertEquals(products, entity.getProducts());
        }
    }
}
