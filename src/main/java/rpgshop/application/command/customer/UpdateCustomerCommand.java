package rpgshop.application.command.customer;

import rpgshop.domain.entity.customer.constant.Gender;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateCustomerCommand(
    UUID id,
    Gender gender,
    String name,
    LocalDate dateOfBirth,
    String email
) {}
