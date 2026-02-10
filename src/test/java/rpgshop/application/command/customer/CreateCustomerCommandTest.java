package rpgshop.application.command.customer;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.domain.entity.customer.constant.PhoneType;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateCustomerCommandTest {

    @Test
    void shouldCreateCreateCustomerCommand() {
        final Gender gender = Gender.MALE;
        final String name = "John Doe";
        final LocalDate dateOfBirth = LocalDate.of(1990, 1, 15);
        final String cpf = "12345678901";
        final String email = "john@example.com";
        final String password = "password123";
        final String confirmPassword = "password123";
        final PhoneType phoneType = PhoneType.MOBILE;
        final String phoneAreaCode = "11";
        final String phoneNumber = "999999999";
        final ResidenceType residentialResidenceType = ResidenceType.HOUSE;
        final StreetType residentialStreetType = StreetType.AVENUE;
        final String residentialStreet = "Main Avenue";
        final String residentialNumber = "100";
        final String residentialNeighborhood = "Center";
        final String residentialZipCode = "01234-567";
        final String residentialCity = "São Paulo";
        final String residentialState = "SP";
        final String residentialCountry = "Brazil";
        final String residentialObservations = "Apartment 10";

        final CreateCustomerCommand command = new CreateCustomerCommand(
            gender,
            name,
            dateOfBirth,
            cpf,
            email,
            password,
            confirmPassword,
            phoneType,
            phoneAreaCode,
            phoneNumber,
            residentialResidenceType,
            residentialStreetType,
            residentialStreet,
            residentialNumber,
            residentialNeighborhood,
            residentialZipCode,
            residentialCity,
            residentialState,
            residentialCountry,
            residentialObservations
        );

        assertEquals(gender, command.gender());
        assertEquals(name, command.name());
        assertEquals(dateOfBirth, command.dateOfBirth());
        assertEquals(cpf, command.cpf());
        assertEquals(email, command.email());
        assertEquals(password, command.password());
        assertEquals(confirmPassword, command.confirmPassword());
        assertEquals(phoneType, command.phoneType());
        assertEquals(phoneAreaCode, command.phoneAreaCode());
        assertEquals(phoneNumber, command.phoneNumber());
        assertEquals(residentialResidenceType, command.residentialResidenceType());
        assertEquals(residentialStreetType, command.residentialStreetType());
        assertEquals(residentialStreet, command.residentialStreet());
        assertEquals(residentialNumber, command.residentialNumber());
        assertEquals(residentialNeighborhood, command.residentialNeighborhood());
        assertEquals(residentialZipCode, command.residentialZipCode());
        assertEquals(residentialCity, command.residentialCity());
        assertEquals(residentialState, command.residentialState());
        assertEquals(residentialCountry, command.residentialCountry());
        assertEquals(residentialObservations, command.residentialObservations());
    }
}

