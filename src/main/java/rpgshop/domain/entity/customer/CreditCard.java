package rpgshop.domain.entity.customer;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record CreditCard(
    UUID id,
    String cardNumber,
    String printedName,
    CardBrand cardBrand,
    String securityCode,
    boolean isPreferred,
    boolean isActive,
    Instant deactivatedAt,
    Instant createdAt,
    Instant updatedAt
) {}
