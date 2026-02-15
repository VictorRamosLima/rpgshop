package rpgshop.domain.entity.user;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record User(
    UUID id,
    String name,
    String email,
    Instant createdAt,
    Instant updatedAt
) {}
