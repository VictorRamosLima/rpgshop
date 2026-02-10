package rpgshop.infraestructure.persistence.entity.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;
import rpgshop.domain.entity.product.constant.StatusChangeType;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("StatusChangeJpaEntity")
class StatusChangeJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final String reason = "Product out of stock";
            final StatusChangeCategory category = StatusChangeCategory.OUT_OF_MARKET;
            final StatusChangeType type = StatusChangeType.DEACTIVATE;
            final ProductJpaEntity product = ProductJpaEntity.builder().build();

            final StatusChangeJpaEntity entity = StatusChangeJpaEntity.builder()
                .id(id)
                .createdAt(createdAt)
                .reason(reason)
                .category(category)
                .type(type)
                .product(product)
                .build();

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(reason, entity.getReason());
            assertEquals(category, entity.getCategory());
            assertEquals(type, entity.getType());
            assertEquals(product, entity.getProduct());
        }

        @Test
        @DisplayName("should create entity with all status change categories")
        void shouldCreateEntityWithAllStatusChangeCategories() {
            for (StatusChangeCategory category : StatusChangeCategory.values()) {
                final StatusChangeJpaEntity entity = StatusChangeJpaEntity.builder()
                    .category(category)
                    .build();

                assertEquals(category, entity.getCategory());
            }
        }

        @Test
        @DisplayName("should create entity with all status change types")
        void shouldCreateEntityWithAllStatusChangeTypes() {
            for (StatusChangeType type : StatusChangeType.values()) {
                final StatusChangeJpaEntity entity = StatusChangeJpaEntity.builder()
                    .type(type)
                    .build();

                assertEquals(type, entity.getType());
            }
        }

        @Test
        @DisplayName("should create entity with ACTIVATE type")
        void shouldCreateEntityWithActivateType() {
            final StatusChangeJpaEntity entity = StatusChangeJpaEntity.builder()
                .type(StatusChangeType.ACTIVATE)
                .category(StatusChangeCategory.RESTOCKED)
                .reason("Product restocked from supplier")
                .build();

            assertEquals(StatusChangeType.ACTIVATE, entity.getType());
            assertEquals(StatusChangeCategory.RESTOCKED, entity.getCategory());
        }

        @Test
        @DisplayName("should create entity with DEACTIVATE type")
        void shouldCreateEntityWithDeactivateType() {
            final StatusChangeJpaEntity entity = StatusChangeJpaEntity.builder()
                .type(StatusChangeType.DEACTIVATE)
                .category(StatusChangeCategory.DISCONTINUED)
                .reason("Product discontinued by manufacturer")
                .build();

            assertEquals(StatusChangeType.DEACTIVATE, entity.getType());
            assertEquals(StatusChangeCategory.DISCONTINUED, entity.getCategory());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final StatusChangeJpaEntity entity = StatusChangeJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getReason());
            assertNull(entity.getCategory());
            assertNull(entity.getType());
            assertNull(entity.getProduct());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final StatusChangeJpaEntity entity = new StatusChangeJpaEntity();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getReason());
            assertNull(entity.getCategory());
            assertNull(entity.getType());
            assertNull(entity.getProduct());
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
            final String reason = "Defective batch recalled";
            final StatusChangeCategory category = StatusChangeCategory.DEFECTIVE;
            final StatusChangeType type = StatusChangeType.DEACTIVATE;
            final ProductJpaEntity product = ProductJpaEntity.builder().build();

            final StatusChangeJpaEntity entity = new StatusChangeJpaEntity(
                id, createdAt, reason, category, type, product
            );

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(reason, entity.getReason());
            assertEquals(category, entity.getCategory());
            assertEquals(type, entity.getType());
            assertEquals(product, entity.getProduct());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final StatusChangeJpaEntity entity = new StatusChangeJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final String reason = "Back in demand after marketing campaign";
            final StatusChangeCategory category = StatusChangeCategory.BACK_IN_DEMAND;
            final StatusChangeType type = StatusChangeType.ACTIVATE;
            final ProductJpaEntity product = ProductJpaEntity.builder().build();

            entity.setId(id);
            entity.setCreatedAt(createdAt);
            entity.setReason(reason);
            entity.setCategory(category);
            entity.setType(type);
            entity.setProduct(product);

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(reason, entity.getReason());
            assertEquals(category, entity.getCategory());
            assertEquals(type, entity.getType());
            assertEquals(product, entity.getProduct());
        }
    }

    @Nested
    @DisplayName("Category and Type Combinations")
    class CategoryTypeCombinationsTests {
        @Test
        @DisplayName("should create deactivation with OUT_OF_MARKET category")
        void shouldCreateDeactivationWithOutOfMarketCategory() {
            final StatusChangeJpaEntity entity = StatusChangeJpaEntity.builder()
                .type(StatusChangeType.DEACTIVATE)
                .category(StatusChangeCategory.OUT_OF_MARKET)
                .reason("No longer available in market")
                .build();

            assertEquals(StatusChangeType.DEACTIVATE, entity.getType());
            assertEquals(StatusChangeCategory.OUT_OF_MARKET, entity.getCategory());
        }

        @Test
        @DisplayName("should create activation with SUPPLIER_RESUMED category")
        void shouldCreateActivationWithSupplierResumedCategory() {
            final StatusChangeJpaEntity entity = StatusChangeJpaEntity.builder()
                .type(StatusChangeType.ACTIVATE)
                .category(StatusChangeCategory.SUPPLIER_RESUMED)
                .reason("Supplier resumed production")
                .build();

            assertEquals(StatusChangeType.ACTIVATE, entity.getType());
            assertEquals(StatusChangeCategory.SUPPLIER_RESUMED, entity.getCategory());
        }
    }
}
