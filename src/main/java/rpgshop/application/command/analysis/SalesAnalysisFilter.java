package rpgshop.application.command.analysis;

import java.time.Instant;
import java.util.UUID;

public record SalesAnalysisFilter(
    UUID productId,
    UUID categoryId,
    Instant startDate,
    Instant endDate
) {}
