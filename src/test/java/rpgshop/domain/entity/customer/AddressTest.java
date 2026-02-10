package rpgshop.domain.entity.customer;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressTest {
    @Test
    void shouldCreateAddress() {
        final UUID id = UUID.randomUUID();
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
        final boolean isActive = true;
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final Address address = Address.builder()
            .id(id)
            .purpose(purpose)
            .label(label)
            .residenceType(residenceType)
            .streetType(streetType)
            .street(street)
            .number(number)
            .neighborhood(neighborhood)
            .zipCode(zipCode)
            .city(city)
            .state(state)
            .country(country)
            .observations(observations)
            .isActive(isActive)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deactivatedAt(null)
            .build();

        assertEquals(id, address.id());
        assertEquals(purpose, address.purpose());
        assertEquals(label, address.label());
        assertEquals(residenceType, address.residenceType());
        assertEquals(streetType, address.streetType());
        assertEquals(street, address.street());
        assertEquals(number, address.number());
        assertEquals(neighborhood, address.neighborhood());
        assertEquals(zipCode, address.zipCode());
        assertEquals(city, address.city());
        assertEquals(state, address.state());
        assertEquals(country, address.country());
        assertEquals(observations, address.observations());
        assertTrue(address.isActive());
        assertEquals(createdAt, address.createdAt());
        assertEquals(updatedAt, address.updatedAt());
        assertNull(address.deactivatedAt());
    }

    @Test
    void shouldCreateAddressWithAllPurposeTypes() {
        for (AddressPurpose purpose : AddressPurpose.values()) {
            final Address address = Address.builder().purpose(purpose).build();
            assertEquals(purpose, address.purpose());
        }
    }

    @Test
    void shouldCreateAddressWithAllResidenceTypes() {
        for (ResidenceType type : ResidenceType.values()) {
            final Address address = Address.builder().residenceType(type).build();
            assertEquals(type, address.residenceType());
        }
    }

    @Test
    void shouldCreateAddressWithAllStreetTypes() {
        for (StreetType type : StreetType.values()) {
            final Address address = Address.builder().streetType(type).build();
            assertEquals(type, address.streetType());
        }
    }

    @Test
    void shouldCreateAddressWithNullValues() {
        final Address address = Address.builder().build();

        assertNull(address.id());
        assertNull(address.purpose());
        assertNull(address.label());
        assertNull(address.residenceType());
        assertNull(address.streetType());
        assertNull(address.street());
        assertNull(address.number());
        assertNull(address.neighborhood());
        assertNull(address.zipCode());
        assertNull(address.city());
        assertNull(address.state());
        assertNull(address.country());
        assertNull(address.observations());
        assertFalse(address.isActive());
        assertNull(address.createdAt());
        assertNull(address.updatedAt());
        assertNull(address.deactivatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final String originalCity = "São Paulo";
        final String newCity = "Rio de Janeiro";

        final Address original = Address.builder().city(originalCity).build();
        final Address modified = original.toBuilder().city(newCity).build();

        assertEquals(originalCity, original.city());
        assertEquals(newCity, modified.city());
    }
}

