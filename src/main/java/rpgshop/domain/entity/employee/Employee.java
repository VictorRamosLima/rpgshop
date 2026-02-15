package rpgshop.domain.entity.employee;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record Employee(
    UUID id,
    String name,
    String cpf,
    Instant createdAt,
    Instant updatedAt
) {}
