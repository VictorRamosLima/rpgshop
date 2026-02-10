package rpgshop.application.command.exchange;

import java.util.UUID;

public record RequestExchangeCommand(
    UUID orderId,
    UUID orderItemId,
    int quantity,
    String reason
) {}
