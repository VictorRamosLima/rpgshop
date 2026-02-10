package rpgshop.domain.entity.customer;

import lombok.Builder;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record Address(
    UUID id,
    AddressPurpose purpose,
    String label,
    ResidenceType residenceType,
    StreetType streetType,
    String street,
    String number,
    String neighborhood,
    String zipCode,
    String city,
    String state,
    String country,
    String observations,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt,
    Instant deactivatedAt
) {}
