package rpgshop.application.command.customer;

import rpgshop.domain.entity.customer.constant.PhoneType;

import java.util.UUID;

public record CreatePhoneCommand(
    UUID customerId,
    PhoneType type,
    String areaCode,
    String number
) {}
