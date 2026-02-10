package rpgshop.domain.entity.supplier;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record Supplier(
    UUID id,
    String name,
    String legalName,
    String cnpj,
    String email,
    String phone,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt,
    Instant deactivatedAt
) {}
