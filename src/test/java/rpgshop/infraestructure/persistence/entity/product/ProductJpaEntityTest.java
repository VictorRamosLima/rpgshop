package rpgshop.infraestructure.persistence.entity.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ProductJpaEntity")
class ProductJpaEntityTest {
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
            final String name = "Epic Sword";
            final ProductTypeJpaEntity productType = ProductTypeJpaEntity.builder().build();
            final List<CategoryJpaEntity> categories = new ArrayList<>();
            final BigDecimal height = new BigDecimal("30.00");
            final BigDecimal width = new BigDecimal("10.00");
            final BigDecimal depth = new BigDecimal("5.00");
            final BigDecimal weight = new BigDecimal("2.500");
            final PricingGroupJpaEntity pricingGroup = PricingGroupJpaEntity.builder().build();
            final String barcode = "7891234567890";
            final String sku = "SWORD-001";
            final BigDecimal salePrice = new BigDecimal("199.99");
            final BigDecimal costPrice = new BigDecimal("99.99");
            final int stockQuantity = 50;
            final List<StatusChangeJpaEntity> statusChanges = new ArrayList<>();
            final BigDecimal minimumSaleThreshold = new BigDecimal("149.99");

            final ProductJpaEntity entity = ProductJpaEntity.builder()
                .id(id)
                .isActive(isActive)
                .deactivatedAt(null)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .name(name)
                .productType(productType)
                .categories(categories)
                .height(height)
                .width(width)
                .depth(depth)
                .weight(weight)
                .pricingGroup(pricingGroup)
                .barcode(barcode)
                .sku(sku)
                .salePrice(salePrice)
                .costPrice(costPrice)
                .stockQuantity(stockQuantity)
                .statusChanges(statusChanges)
                .minimumSaleThreshold(minimumSaleThreshold)
                .build();

            assertEquals(id, entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(name, entity.getName());
            assertEquals(productType, entity.getProductType());
            assertEquals(categories, entity.getCategories());
            assertEquals(height, entity.getHeight());
            assertEquals(width, entity.getWidth());
            assertEquals(depth, entity.getDepth());
            assertEquals(weight, entity.getWeight());
            assertEquals(pricingGroup, entity.getPricingGroup());
            assertEquals(barcode, entity.getBarcode());
            assertEquals(sku, entity.getSku());
            assertEquals(salePrice, entity.getSalePrice());
            assertEquals(costPrice, entity.getCostPrice());
            assertEquals(stockQuantity, entity.getStockQuantity());
            assertEquals(statusChanges, entity.getStatusChanges());
            assertEquals(minimumSaleThreshold, entity.getMinimumSaleThreshold());
        }

        @Test
        @DisplayName("should create entity with default isActive as true")
        void shouldCreateEntityWithDefaultIsActiveAsTrue() {
            final ProductJpaEntity entity = ProductJpaEntity.builder().build();
            assertTrue(entity.isActive());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final ProductJpaEntity entity = ProductJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getName());
            assertNull(entity.getProductType());
            assertNull(entity.getCategories());
            assertNull(entity.getHeight());
            assertNull(entity.getWidth());
            assertNull(entity.getDepth());
            assertNull(entity.getWeight());
            assertNull(entity.getPricingGroup());
            assertNull(entity.getBarcode());
            assertNull(entity.getSku());
            assertNull(entity.getSalePrice());
            assertNull(entity.getCostPrice());
            assertNull(entity.getStatusChanges());
            assertNull(entity.getMinimumSaleThreshold());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final ProductJpaEntity entity = new ProductJpaEntity();

            assertNull(entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getName());
            assertNull(entity.getProductType());
            assertNull(entity.getCategories());
            assertNull(entity.getHeight());
            assertNull(entity.getWidth());
            assertNull(entity.getDepth());
            assertNull(entity.getWeight());
            assertNull(entity.getPricingGroup());
            assertNull(entity.getBarcode());
            assertNull(entity.getSku());
            assertNull(entity.getSalePrice());
            assertNull(entity.getCostPrice());
            assertEquals(0, entity.getStockQuantity());
            assertNull(entity.getStatusChanges());
            assertNull(entity.getMinimumSaleThreshold());
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
            final String name = "Magic Shield";
            final ProductTypeJpaEntity productType = ProductTypeJpaEntity.builder().build();
            final List<CategoryJpaEntity> categories = new ArrayList<>();
            final BigDecimal height = new BigDecimal("50.00");
            final BigDecimal width = new BigDecimal("50.00");
            final BigDecimal depth = new BigDecimal("10.00");
            final BigDecimal weight = new BigDecimal("5.000");
            final PricingGroupJpaEntity pricingGroup = PricingGroupJpaEntity.builder().build();
            final String barcode = "7890987654321";
            final String sku = "SHIELD-001";
            final BigDecimal salePrice = new BigDecimal("299.99");
            final BigDecimal costPrice = new BigDecimal("149.99");
            final int stockQuantity = 25;
            final List<StatusChangeJpaEntity> statusChanges = new ArrayList<>();
            final BigDecimal minimumSaleThreshold = new BigDecimal("249.99");

            final ProductJpaEntity entity = new ProductJpaEntity(
                id,
                isActive,
                deactivatedAt,
                createdAt,
                updatedAt,
                name,
                productType,
                categories,
                height,
                width,
                depth,
                weight,
                pricingGroup,
                barcode,
                sku,
                salePrice,
                costPrice,
                stockQuantity,
                statusChanges,
                minimumSaleThreshold
            );

            assertEquals(id, entity.getId());
            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(name, entity.getName());
            assertEquals(productType, entity.getProductType());
            assertEquals(categories, entity.getCategories());
            assertEquals(height, entity.getHeight());
            assertEquals(width, entity.getWidth());
            assertEquals(depth, entity.getDepth());
            assertEquals(weight, entity.getWeight());
            assertEquals(pricingGroup, entity.getPricingGroup());
            assertEquals(barcode, entity.getBarcode());
            assertEquals(sku, entity.getSku());
            assertEquals(salePrice, entity.getSalePrice());
            assertEquals(costPrice, entity.getCostPrice());
            assertEquals(stockQuantity, entity.getStockQuantity());
            assertEquals(statusChanges, entity.getStatusChanges());
            assertEquals(minimumSaleThreshold, entity.getMinimumSaleThreshold());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final ProductJpaEntity entity = new ProductJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant deactivatedAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final String name = "Healing Potion";
            final ProductTypeJpaEntity productType = ProductTypeJpaEntity.builder().build();
            final List<CategoryJpaEntity> categories = new ArrayList<>();
            final BigDecimal height = new BigDecimal("15.00");
            final BigDecimal width = new BigDecimal("5.00");
            final BigDecimal depth = new BigDecimal("5.00");
            final BigDecimal weight = new BigDecimal("0.250");
            final PricingGroupJpaEntity pricingGroup = PricingGroupJpaEntity.builder().build();
            final String barcode = "7891112223334";
            final String sku = "POTION-001";
            final BigDecimal salePrice = new BigDecimal("49.99");
            final BigDecimal costPrice = new BigDecimal("19.99");
            final int stockQuantity = 100;
            final List<StatusChangeJpaEntity> statusChanges = new ArrayList<>();
            final BigDecimal minimumSaleThreshold = new BigDecimal("39.99");

            entity.setId(id);
            entity.setActive(true);
            entity.setDeactivatedAt(deactivatedAt);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setName(name);
            entity.setProductType(productType);
            entity.setCategories(categories);
            entity.setHeight(height);
            entity.setWidth(width);
            entity.setDepth(depth);
            entity.setWeight(weight);
            entity.setPricingGroup(pricingGroup);
            entity.setBarcode(barcode);
            entity.setSku(sku);
            entity.setSalePrice(salePrice);
            entity.setCostPrice(costPrice);
            entity.setStockQuantity(stockQuantity);
            entity.setStatusChanges(statusChanges);
            entity.setMinimumSaleThreshold(minimumSaleThreshold);

            assertEquals(id, entity.getId());
            assertTrue(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(name, entity.getName());
            assertEquals(productType, entity.getProductType());
            assertEquals(categories, entity.getCategories());
            assertEquals(height, entity.getHeight());
            assertEquals(width, entity.getWidth());
            assertEquals(depth, entity.getDepth());
            assertEquals(weight, entity.getWeight());
            assertEquals(pricingGroup, entity.getPricingGroup());
            assertEquals(barcode, entity.getBarcode());
            assertEquals(sku, entity.getSku());
            assertEquals(salePrice, entity.getSalePrice());
            assertEquals(costPrice, entity.getCostPrice());
            assertEquals(stockQuantity, entity.getStockQuantity());
            assertEquals(statusChanges, entity.getStatusChanges());
            assertEquals(minimumSaleThreshold, entity.getMinimumSaleThreshold());
        }
    }
}
