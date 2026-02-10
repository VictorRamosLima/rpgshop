package rpgshop.application.command.customer;

import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.domain.entity.customer.constant.PhoneType;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;

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
    String phoneNumber,
    ResidenceType residentialResidenceType,
    StreetType residentialStreetType,
    String residentialStreet,
    String residentialNumber,
    String residentialNeighborhood,
    String residentialZipCode,
    String residentialCity,
    String residentialState,
    String residentialCountry,
    String residentialObservations
) {}
