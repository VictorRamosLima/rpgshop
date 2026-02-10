package rpgshop.application.command.exchange;

import java.util.UUID;

public record ReceiveExchangeItemsCommand(
    UUID exchangeRequestId,
    boolean returnToStock
) {}
