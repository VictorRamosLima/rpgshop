package rpgshop.infraestructure.persistence.entity.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ProductTypeJpaEntity")
class ProductTypeJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final boolean isActive = true;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final String name = "Physical";
            final String description = "Physical products that require shipping";

            final ProductTypeJpaEntity entity = ProductTypeJpaEntity.builder()
                .id(id)
                .isActive(isActive)
                .deactivatedAt(null)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .name(name)
                .description(description)
                .build();

            assertEquals(id, entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(name, entity.getName());
            assertEquals(description, entity.getDescription());
        }

        @Test
        @DisplayName("should create entity with different product types")
        void shouldCreateEntityWithDifferentProductTypes() {
            final String[] typeNames = {"Physical", "Digital", "Service", "Subscription"};

            for (String typeName : typeNames) {
                final ProductTypeJpaEntity entity = ProductTypeJpaEntity.builder()
                    .name(typeName)
                    .build();

                assertEquals(typeName, entity.getName());
            }
        }

        @Test
        @DisplayName("should create entity with default isActive as true")
        void shouldCreateEntityWithDefaultIsActiveAsTrue() {
            final ProductTypeJpaEntity entity = ProductTypeJpaEntity.builder().build();
            assertTrue(entity.isActive());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final ProductTypeJpaEntity entity = ProductTypeJpaEntity.builder().build();

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
            final ProductTypeJpaEntity entity = new ProductTypeJpaEntity();

            assertNull(entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getName());
            assertNull(entity.getDescription());
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
            final String name = "Digital";
            final String description = "Digital products delivered electronically";

            final ProductTypeJpaEntity entity = new ProductTypeJpaEntity(
                id, isActive, deactivatedAt, createdAt, updatedAt, name, description
            );

            assertEquals(id, entity.getId());
            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(name, entity.getName());
            assertEquals(description, entity.getDescription());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final ProductTypeJpaEntity entity = new ProductTypeJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant deactivatedAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final String name = "Service";
            final String description = "Service-based products";

            entity.setId(id);
            entity.setActive(true);
            entity.setDeactivatedAt(deactivatedAt);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setName(name);
            entity.setDescription(description);

            assertEquals(id, entity.getId());
            assertTrue(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(name, entity.getName());
            assertEquals(description, entity.getDescription());
        }
    }
}
