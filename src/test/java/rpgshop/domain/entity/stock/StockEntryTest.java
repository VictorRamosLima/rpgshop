package rpgshop.domain.entity.stock;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.supplier.Supplier;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockEntryTest {
    @Test
    void shouldCreateStockEntry() {
        final UUID id = UUID.randomUUID();
        final Product product = Product.builder().id(UUID.randomUUID()).name("RPG Book").build();
        final Integer quantity = 100;
        final BigDecimal costValue = new BigDecimal("5000.00");
        final Supplier supplier = Supplier.builder().name("RPG Supplier").build();
        final LocalDate entryDate = LocalDate.now();
        final boolean isReentry = false;
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final StockEntry stockEntry = StockEntry.builder()
            .id(id)
            .product(product)
            .quantity(quantity)
            .costValue(costValue)
            .supplier(supplier)
            .entryDate(entryDate)
            .isReentry(isReentry)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, stockEntry.id());
        assertEquals(product, stockEntry.product());
        assertEquals(quantity, stockEntry.quantity());
        assertEquals(costValue, stockEntry.costValue());
        assertEquals(supplier, stockEntry.supplier());
        assertEquals(entryDate, stockEntry.entryDate());
        assertFalse(stockEntry.isReentry());
        assertEquals(createdAt, stockEntry.createdAt());
        assertEquals(updatedAt, stockEntry.updatedAt());
    }

    @Test
    void shouldCreateStockEntryAsReentry() {
        final StockEntry stockEntry = StockEntry.builder()
            .isReentry(true)
            .build();

        assertTrue(stockEntry.isReentry());
    }

    @Test
    void shouldCreateStockEntryWithNullValues() {
        final StockEntry stockEntry = StockEntry.builder().build();

        assertNull(stockEntry.id());
        assertNull(stockEntry.product());
        assertNull(stockEntry.quantity());
        assertNull(stockEntry.costValue());
        assertNull(stockEntry.supplier());
        assertNull(stockEntry.entryDate());
        assertFalse(stockEntry.isReentry());
        assertNull(stockEntry.createdAt());
        assertNull(stockEntry.updatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final Integer originalQuantity = 100;
        final Integer newQuantity = 200;

        final StockEntry original = StockEntry.builder().quantity(originalQuantity).build();
        final StockEntry modified = original.toBuilder().quantity(newQuantity).build();

        assertEquals(originalQuantity, original.quantity());
        assertEquals(newQuantity, modified.quantity());
    }
}
