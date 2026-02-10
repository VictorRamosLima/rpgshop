package rpgshop.infraestructure.persistence.mapper.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;
import rpgshop.infraestructure.persistence.entity.customer.AddressJpaEntity;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AddressMapper")
class AddressMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final AddressPurpose purpose = AddressPurpose.DELIVERY;
            final String label = "Home";
            final ResidenceType residenceType = ResidenceType.HOUSE;
            final StreetType streetType = StreetType.STREET;
            final String street = "Main Street";
            final String number = "123";
            final String neighborhood = "Downtown";
            final String zipCode = "12345678";
            final String city = "São Paulo";
            final String state = "SP";
            final String country = "Brazil";
            final String observations = "Near the park";
            final boolean isActive = true;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final AddressJpaEntity entity = AddressJpaEntity.builder()
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

            final Address domain = AddressMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertEquals(purpose, domain.purpose());
            assertEquals(label, domain.label());
            assertEquals(residenceType, domain.residenceType());
            assertEquals(streetType, domain.streetType());
            assertEquals(street, domain.street());
            assertEquals(number, domain.number());
            assertEquals(neighborhood, domain.neighborhood());
            assertEquals(zipCode, domain.zipCode());
            assertEquals(city, domain.city());
            assertEquals(state, domain.state());
            assertEquals(country, domain.country());
            assertEquals(observations, domain.observations());
            assertEquals(isActive, domain.isActive());
            assertEquals(createdAt, domain.createdAt());
            assertEquals(updatedAt, domain.updatedAt());
            assertNull(domain.deactivatedAt());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> AddressMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final AddressPurpose purpose = AddressPurpose.BILLING;
            final String label = "Office";
            final ResidenceType residenceType = ResidenceType.APARTMENT;
            final StreetType streetType = StreetType.AVENUE;
            final String street = "Business Avenue";
            final String number = "456";
            final String neighborhood = "Financial District";
            final String zipCode = "87654321";
            final String city = "Rio de Janeiro";
            final String state = "RJ";
            final String country = "Brazil";
            final String observations = "Building A, Floor 5";
            final boolean isActive = false;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final Instant deactivatedAt = Instant.now();

            final Address domain = Address.builder()
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
                .deactivatedAt(deactivatedAt)
                .build();

            final AddressJpaEntity entity = AddressMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertEquals(purpose, entity.getPurpose());
            assertEquals(label, entity.getLabel());
            assertEquals(residenceType, entity.getResidenceType());
            assertEquals(streetType, entity.getStreetType());
            assertEquals(street, entity.getStreet());
            assertEquals(number, entity.getNumber());
            assertEquals(neighborhood, entity.getNeighborhood());
            assertEquals(zipCode, entity.getZipCode());
            assertEquals(city, entity.getCity());
            assertEquals(state, entity.getState());
            assertEquals(country, entity.getCountry());
            assertEquals(observations, entity.getObservations());
            assertEquals(isActive, entity.isActive());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> AddressMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = AddressMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            final var exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
            assertInstanceOf(IllegalInstantiationException.class, exception.getCause());
        }
    }
}
