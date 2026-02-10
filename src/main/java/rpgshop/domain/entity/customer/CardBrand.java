package rpgshop.domain.entity.customer;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record CardBrand(
    UUID id,
    String name,
    boolean isActive,
    Instant deactivatedAt,
    Instant createdAt,
    Instant updatedAt
) {}
