package rpgshop.application.command.customer;

import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.domain.entity.customer.constant.PhoneType;

import java.time.LocalDate;

public record CreateCustomerCommand(
    Gender gender,
    String name,
    LocalDate dateOfBirth,
    String cpf,
    String email,
    String password,
    String confirmPassword,
    PhoneType phoneType,
    String phoneAreaCode,
    String phoneNumber
) {}
