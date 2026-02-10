package rpgshop.domain.entity.product;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record ProductType(
    UUID id,
    String name,
    String description,
    boolean isActive,
    Instant deactivatedAt,
    Instant createdAt,
    Instant updatedAt
) {}
