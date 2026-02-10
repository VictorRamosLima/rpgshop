package rpgshop.application.command.stock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateStockEntryCommand(
    UUID productId,
    Integer quantity,
    BigDecimal costValue,
    UUID supplierId,
    LocalDate entryDate
) {}
