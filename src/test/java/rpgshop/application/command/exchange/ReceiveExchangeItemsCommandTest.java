package rpgshop.application.command.exchange;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReceiveExchangeItemsCommandTest {
    @Test
    void shouldCreateReceiveExchangeItemsCommand() {
        final UUID exchangeRequestId = UUID.randomUUID();
        final boolean returnToStock = true;
        final UUID supplierId = UUID.randomUUID();
        final BigDecimal costValue = new BigDecimal("25.50");

        final ReceiveExchangeItemsCommand command = new ReceiveExchangeItemsCommand(
            exchangeRequestId,
            returnToStock,
            supplierId,
            costValue
        );

        assertEquals(exchangeRequestId, command.exchangeRequestId());
        assertTrue(command.returnToStock());
        assertEquals(supplierId, command.supplierId());
        assertEquals(costValue, command.costValue());
    }
}

