package rpgshop.infraestructure.integration.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class SimulatedExchangeNotificationGatewayTest {
    @InjectMocks
    private SimulatedExchangeNotificationGateway gateway;

    @Test
    void shouldNotifyExchangeAuthorizedWithoutException() {
        final UUID customerId = UUID.randomUUID();
        final UUID orderId = UUID.randomUUID();
        final UUID exchangeRequestId = UUID.randomUUID();

        assertDoesNotThrow(() -> gateway.notifyExchangeAuthorized(customerId, orderId, exchangeRequestId));
    }

    @Test
    void shouldNotifyExchangeAuthorizedWithNullValues() {
        assertDoesNotThrow(() -> gateway.notifyExchangeAuthorized(null, null, null));
    }
}

