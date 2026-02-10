package rpgshop.application.command.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateProductCommandTest {
    @Test
    void shouldCreateUpdateProductCommand() {
        final UUID id = UUID.randomUUID();
        final String name = "Updated Sword";
        final UUID productTypeId = UUID.randomUUID();
        final List<UUID> categoryIds = List.of(UUID.randomUUID());
        final BigDecimal height = new BigDecimal("110.00");
        final BigDecimal width = new BigDecimal("12.00");
        final BigDecimal depth = new BigDecimal("6.00");
        final BigDecimal weight = new BigDecimal("3.00");
        final UUID pricingGroupId = UUID.randomUUID();
        final String barcode = "1234567890124";
        final String sku = "MS-002";
        final BigDecimal costPrice = new BigDecimal("55.00");
        final BigDecimal salePrice = new BigDecimal("99.99");
        final boolean managerAuthorized = true;

        final UpdateProductCommand command = new UpdateProductCommand(
            id,
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
            costPrice,
            salePrice,
            managerAuthorized
        );

        assertEquals(id, command.id());
        assertEquals(name, command.name());
        assertEquals(productTypeId, command.productTypeId());
        assertEquals(1, command.categoryIds().size());
        assertEquals(height, command.height());
        assertEquals(width, command.width());
        assertEquals(depth, command.depth());
        assertEquals(weight, command.weight());
        assertEquals(pricingGroupId, command.pricingGroupId());
        assertEquals(barcode, command.barcode());
        assertEquals(sku, command.sku());
        assertEquals(costPrice, command.costPrice());
        assertEquals(salePrice, command.salePrice());
        assertTrue(command.managerAuthorized());
    }
}

