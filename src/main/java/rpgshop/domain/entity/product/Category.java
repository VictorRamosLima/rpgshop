package rpgshop.domain.entity.product;

import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
public record Category(
    UUID id,
    String name,
    String description,
    List<Product> products,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt,
    Instant deactivatedAt
) {}
