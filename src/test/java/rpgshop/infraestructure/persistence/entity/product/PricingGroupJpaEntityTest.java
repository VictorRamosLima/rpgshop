package rpgshop.infraestructure.persistence.entity.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PricingGroupJpaEntity")
class PricingGroupJpaEntityTest {
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
            final String name = "Premium";
            final BigDecimal marginPercentage = new BigDecimal("35.00");

            final PricingGroupJpaEntity entity = PricingGroupJpaEntity.builder()
                .id(id)
                .isActive(isActive)
                .deactivatedAt(null)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .name(name)
                .marginPercentage(marginPercentage)
                .build();

            assertEquals(id, entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(name, entity.getName());
            assertEquals(marginPercentage, entity.getMarginPercentage());
        }

        @Test
        @DisplayName("should create entity with different margin percentages")
        void shouldCreateEntityWithDifferentMarginPercentages() {
            final BigDecimal[] margins = {
                new BigDecimal("10.00"),
                new BigDecimal("25.50"),
                new BigDecimal("50.00"),
                new BigDecimal("99.99")
            };

            for (BigDecimal margin : margins) {
                final PricingGroupJpaEntity entity = PricingGroupJpaEntity.builder()
                    .marginPercentage(margin)
                    .build();

                assertEquals(margin, entity.getMarginPercentage());
            }
        }

        @Test
        @DisplayName("should create entity with default isActive as true")
        void shouldCreateEntityWithDefaultIsActiveAsTrue() {
            final PricingGroupJpaEntity entity = PricingGroupJpaEntity.builder().build();
            assertTrue(entity.isActive());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final PricingGroupJpaEntity entity = PricingGroupJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getName());
            assertNull(entity.getMarginPercentage());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final PricingGroupJpaEntity entity = new PricingGroupJpaEntity();

            assertNull(entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getName());
            assertNull(entity.getMarginPercentage());
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
            final String name = "Standard";
            final BigDecimal marginPercentage = new BigDecimal("20.00");

            final PricingGroupJpaEntity entity = new PricingGroupJpaEntity(
                id, isActive, deactivatedAt, createdAt, updatedAt, name, marginPercentage
            );

            assertEquals(id, entity.getId());
            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(name, entity.getName());
            assertEquals(marginPercentage, entity.getMarginPercentage());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final PricingGroupJpaEntity entity = new PricingGroupJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant deactivatedAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final String name = "Economy";
            final BigDecimal marginPercentage = new BigDecimal("15.00");

            entity.setId(id);
            entity.setActive(true);
            entity.setDeactivatedAt(deactivatedAt);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setName(name);
            entity.setMarginPercentage(marginPercentage);

            assertEquals(id, entity.getId());
            assertTrue(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(name, entity.getName());
            assertEquals(marginPercentage, entity.getMarginPercentage());
        }
    }
}
