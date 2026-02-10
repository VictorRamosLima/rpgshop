package rpgshop.infraestructure.integration.exchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rpgshop.application.gateway.exchange.ExchangeNotificationGateway;

import java.util.UUID;

@Component
public class SimulatedExchangeNotificationGateway implements ExchangeNotificationGateway {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimulatedExchangeNotificationGateway.class);

    @Override
    public void notifyExchangeAuthorized(
        final UUID customerId,
        final UUID orderId,
        final UUID exchangeRequestId
    ) {
        LOGGER.info(
            "Notificacao enviada ao cliente {}: troca {} do pedido {} foi autorizada",
            customerId,
            exchangeRequestId,
            orderId
        );
    }
}
