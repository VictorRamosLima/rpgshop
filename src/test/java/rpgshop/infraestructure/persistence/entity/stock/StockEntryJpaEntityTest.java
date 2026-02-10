package rpgshop.infraestructure.persistence.entity.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;
import rpgshop.infraestructure.persistence.entity.supplier.SupplierJpaEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("StockEntryJpaEntity")
class StockEntryJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final ProductJpaEntity product = ProductJpaEntity.builder().build();
            final Integer quantity = 100;
            final BigDecimal costValue = new BigDecimal("1500.00");
            final SupplierJpaEntity supplier = SupplierJpaEntity.builder().build();
            final LocalDate entryDate = LocalDate.now();
            final boolean isReentry = false;

            final StockEntryJpaEntity entity = StockEntryJpaEntity.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .product(product)
                .quantity(quantity)
                .costValue(costValue)
                .supplier(supplier)
                .entryDate(entryDate)
                .isReentry(isReentry)
                .build();

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(product, entity.getProduct());
            assertEquals(quantity, entity.getQuantity());
            assertEquals(costValue, entity.getCostValue());
            assertEquals(supplier, entity.getSupplier());
            assertEquals(entryDate, entity.getEntryDate());
            assertFalse(entity.isReentry());
        }

        @Test
        @DisplayName("should create entity with reentry as true")
        void shouldCreateEntityWithReentryAsTrue() {
            final StockEntryJpaEntity entity = StockEntryJpaEntity.builder()
                .isReentry(true)
                .build();

            assertTrue(entity.isReentry());
        }

        @Test
        @DisplayName("should create entity with default isReentry as false")
        void shouldCreateEntityWithDefaultIsReentryAsFalse() {
            final StockEntryJpaEntity entity = StockEntryJpaEntity.builder().build();
            assertFalse(entity.isReentry());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final StockEntryJpaEntity entity = StockEntryJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getProduct());
            assertNull(entity.getQuantity());
            assertNull(entity.getCostValue());
            assertNull(entity.getSupplier());
            assertNull(entity.getEntryDate());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final StockEntryJpaEntity entity = new StockEntryJpaEntity();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getProduct());
            assertNull(entity.getQuantity());
            assertNull(entity.getCostValue());
            assertNull(entity.getSupplier());
            assertNull(entity.getEntryDate());
            assertFalse(entity.isReentry());
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
            final ProductJpaEntity product = ProductJpaEntity.builder().build();
            final Integer quantity = 50;
            final BigDecimal costValue = new BigDecimal("750.00");
            final SupplierJpaEntity supplier = SupplierJpaEntity.builder().build();
            final LocalDate entryDate = LocalDate.of(2024, 6, 15);
            final boolean isReentry = true;

            final StockEntryJpaEntity entity = new StockEntryJpaEntity(
                id, createdAt, updatedAt, product, quantity, costValue, supplier, entryDate, isReentry
            );

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(product, entity.getProduct());
            assertEquals(quantity, entity.getQuantity());
            assertEquals(costValue, entity.getCostValue());
            assertEquals(supplier, entity.getSupplier());
            assertEquals(entryDate, entity.getEntryDate());
            assertTrue(entity.isReentry());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final StockEntryJpaEntity entity = new StockEntryJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final ProductJpaEntity product = ProductJpaEntity.builder().build();
            final Integer quantity = 200;
            final BigDecimal costValue = new BigDecimal("3000.00");
            final SupplierJpaEntity supplier = SupplierJpaEntity.builder().build();
            final LocalDate entryDate = LocalDate.of(2024, 12, 1);

            entity.setId(id);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setProduct(product);
            entity.setQuantity(quantity);
            entity.setCostValue(costValue);
            entity.setSupplier(supplier);
            entity.setEntryDate(entryDate);
            entity.setReentry(true);

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(product, entity.getProduct());
            assertEquals(quantity, entity.getQuantity());
            assertEquals(costValue, entity.getCostValue());
            assertEquals(supplier, entity.getSupplier());
            assertEquals(entryDate, entity.getEntryDate());
            assertTrue(entity.isReentry());
        }
    }

    @Nested
    @DisplayName("Stock Entry Types")
    class StockEntryTypesTests {
        @Test
        @DisplayName("should create regular stock entry")
        void shouldCreateRegularStockEntry() {
            final StockEntryJpaEntity entity = StockEntryJpaEntity.builder()
                .quantity(100)
                .costValue(new BigDecimal("1000.00"))
                .entryDate(LocalDate.now())
                .isReentry(false)
                .build();

            assertFalse(entity.isReentry());
            assertEquals(100, entity.getQuantity());
        }

        @Test
        @DisplayName("should create reentry stock entry")
        void shouldCreateReentryStockEntry() {
            final StockEntryJpaEntity entity = StockEntryJpaEntity.builder()
                .quantity(25)
                .costValue(new BigDecimal("0.00"))
                .entryDate(LocalDate.now())
                .isReentry(true)
                .build();

            assertTrue(entity.isReentry());
            assertEquals(25, entity.getQuantity());
        }
    }
}
