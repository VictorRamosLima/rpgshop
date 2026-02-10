package rpgshop.domain.entity.customer;

import lombok.Builder;
import rpgshop.domain.entity.customer.constant.PhoneType;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record Phone(
    UUID id,
    PhoneType type,
    String areaCode,
    String number,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt,
    Instant deactivatedAt
) {}
