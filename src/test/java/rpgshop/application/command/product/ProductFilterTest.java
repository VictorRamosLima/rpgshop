package rpgshop.application.command.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductFilterTest {

    @Test
    void shouldCreateProductFilter() {
        final String name = "Sword";
        final UUID productTypeId = UUID.randomUUID();
        final UUID categoryId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final String sku = "SW-001";
        final String barcode = "1234567890123";
        final Boolean isActive = true;
        final BigDecimal minPrice = new BigDecimal("10.00");
        final BigDecimal maxPrice = new BigDecimal("100.00");

        final ProductFilter filter = new ProductFilter(
            name,
            productTypeId,
            categoryId,
            pricingGroupId,
            sku,
            barcode,
            isActive,
            minPrice,
            maxPrice
        );

        assertEquals(name, filter.name());
        assertEquals(productTypeId, filter.productTypeId());
        assertEquals(categoryId, filter.categoryId());
        assertEquals(pricingGroupId, filter.pricingGroupId());
        assertEquals(sku, filter.sku());
        assertEquals(barcode, filter.barcode());
        assertEquals(isActive, filter.isActive());
        assertEquals(minPrice, filter.minPrice());
        assertEquals(maxPrice, filter.maxPrice());
    }
}

