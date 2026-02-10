package rpgshop.application.command.order;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateOrderCommandTest {
    @Test
    void shouldCreateCreateOrderCommand() {
        final UUID customerId = UUID.randomUUID();
        final UUID deliveryAddressId = UUID.randomUUID();
        final List<PaymentInfo> payments = List.of(
            new PaymentInfo(UUID.randomUUID(), null, new BigDecimal("100.00"))
        );

        final CreateOrderCommand command = new CreateOrderCommand(customerId, deliveryAddressId, payments);

        assertEquals(customerId, command.customerId());
        assertEquals(deliveryAddressId, command.deliveryAddressId());
        assertEquals(1, command.payments().size());
    }
}

