package rpgshop.application.command.customer;

import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;

import java.util.UUID;

public record CreateAddressCommand(
    UUID customerId,
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
    String observations
) {}
