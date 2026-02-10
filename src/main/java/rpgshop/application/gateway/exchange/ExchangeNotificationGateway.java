package rpgshop.application.gateway.exchange;

import java.util.UUID;

public interface ExchangeNotificationGateway {
    void notifyExchangeAuthorized(
        final UUID customerId,
        final UUID orderId,
        final UUID exchangeRequestId
    );
}
