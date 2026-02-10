package rpgshop.application.command.customer;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateAddressCommandTest {

    @Test
    void shouldCreateCreateAddressCommand() {
        final UUID customerId = UUID.randomUUID();
        final AddressPurpose purpose = AddressPurpose.DELIVERY;
        final String label = "Home";
        final ResidenceType residenceType = ResidenceType.HOUSE;
        final StreetType streetType = StreetType.STREET;
        final String street = "Main Street";
        final String number = "123";
        final String neighborhood = "Downtown";
        final String zipCode = "12345-678";
        final String city = "São Paulo";
        final String state = "SP";
        final String country = "Brazil";
        final String observations = "Near the park";

        final CreateAddressCommand command = new CreateAddressCommand(
            customerId,
            purpose,
            label,
            residenceType,
            streetType,
            street,
            number,
            neighborhood,
            zipCode,
            city,
            state,
            country,
            observations
        );

        assertEquals(customerId, command.customerId());
        assertEquals(purpose, command.purpose());
        assertEquals(label, command.label());
        assertEquals(residenceType, command.residenceType());
        assertEquals(streetType, command.streetType());
        assertEquals(street, command.street());
        assertEquals(number, command.number());
        assertEquals(neighborhood, command.neighborhood());
        assertEquals(zipCode, command.zipCode());
        assertEquals(city, command.city());
        assertEquals(state, command.state());
        assertEquals(country, command.country());
        assertEquals(observations, command.observations());
    }
}

