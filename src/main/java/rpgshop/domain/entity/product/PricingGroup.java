package rpgshop.domain.entity.product;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record PricingGroup(
    UUID id,
    String name,
    BigDecimal marginPercentage,
    boolean isActive,
    Instant deactivatedAt,
    Instant createdAt,
    Instant updatedAt
) {}
