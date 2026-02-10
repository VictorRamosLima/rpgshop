package rpgshop.application.command.exchange;

import java.math.BigDecimal;
import java.util.UUID;

public record ReceiveExchangeItemsCommand(
    UUID exchangeRequestId,
    boolean returnToStock,
    UUID supplierId,
    BigDecimal costValue
) {}
