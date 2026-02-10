package rpgshop.application.command.exchange;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RequestExchangeCommandTest {

    @Test
    void shouldCreateRequestExchangeCommand() {
        final UUID orderId = UUID.randomUUID();
        final UUID orderItemId = UUID.randomUUID();
        final int quantity = 2;
        final String reason = "Product damaged";

        final RequestExchangeCommand command = new RequestExchangeCommand(
            orderId,
            orderItemId,
            quantity,
            reason
        );

        assertEquals(orderId, command.orderId());
        assertEquals(orderItemId, command.orderItemId());
        assertEquals(quantity, command.quantity());
        assertEquals(reason, command.reason());
    }
}

