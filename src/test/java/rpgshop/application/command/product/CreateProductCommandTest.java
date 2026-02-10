package rpgshop.application.command.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateProductCommandTest {

    @Test
    void shouldCreateCreateProductCommand() {
        final String name = "Magic Sword";
        final UUID productTypeId = UUID.randomUUID();
        final List<UUID> categoryIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        final BigDecimal height = new BigDecimal("100.00");
        final BigDecimal width = new BigDecimal("10.00");
        final BigDecimal depth = new BigDecimal("5.00");
        final BigDecimal weight = new BigDecimal("2.50");
        final UUID pricingGroupId = UUID.randomUUID();
        final String barcode = "1234567890123";
        final String sku = "MS-001";
        final BigDecimal costPrice = new BigDecimal("50.00");

        final CreateProductCommand command = new CreateProductCommand(
            name,
            productTypeId,
            categoryIds,
            height,
            width,
            depth,
            weight,
            pricingGroupId,
            barcode,
            sku,
            costPrice
        );

        assertEquals(name, command.name());
        assertEquals(productTypeId, command.productTypeId());
        assertEquals(2, command.categoryIds().size());
        assertEquals(height, command.height());
        assertEquals(width, command.width());
        assertEquals(depth, command.depth());
        assertEquals(weight, command.weight());
        assertEquals(pricingGroupId, command.pricingGroupId());
        assertEquals(barcode, command.barcode());
        assertEquals(sku, command.sku());
        assertEquals(costPrice, command.costPrice());
    }
}

