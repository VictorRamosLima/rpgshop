package rpgshop.application.command.stock;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateStockEntryCommandTest {
    @Test
    void shouldCreateCreateStockEntryCommand() {
        final UUID productId = UUID.randomUUID();
        final Integer quantity = 100;
        final BigDecimal costValue = new BigDecimal("500.00");
        final UUID supplierId = UUID.randomUUID();
        final LocalDate entryDate = LocalDate.now();

        final CreateStockEntryCommand command = new CreateStockEntryCommand(
            productId,
            quantity,
            costValue,
            supplierId,
            entryDate
        );

        assertEquals(productId, command.productId());
        assertEquals(quantity, command.quantity());
        assertEquals(costValue, command.costValue());
        assertEquals(supplierId, command.supplierId());
        assertEquals(entryDate, command.entryDate());
    }
}

