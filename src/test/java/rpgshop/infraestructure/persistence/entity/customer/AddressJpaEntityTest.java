package rpgshop.infraestructure.persistence.entity.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

@DisplayName("AddressJpaEntity")
class AddressJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final boolean isActive = true;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final AddressPurpose purpose = AddressPurpose.RESIDENTIAL;
            final String label = "Home Address";
            final ResidenceType residenceType = ResidenceType.HOUSE;
            final StreetType streetType = StreetType.AVENUE;
            final String street = "Main Avenue";
            final String number = "123";
            final String neighborhood = "Downtown";
            final String zipCode = "12345-678";
            final String city = "São Paulo";
            final String state = "SP";
            final String country = "Brazil";
            final String observations = "Near the park";

            final AddressJpaEntity entity = AddressJpaEntity.builder()
                .id(id)
                .isActive(isActive)
                .deactivatedAt(null)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .customer(customer)
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
                .build();

            assertEquals(id, entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(customer, entity.getCustomer());
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
        }

        @Test
        @DisplayName("should create entity with all address purpose types")
        void shouldCreateEntityWithAllAddressPurposeTypes() {
            for (AddressPurpose purpose : AddressPurpose.values()) {
                final AddressJpaEntity entity = AddressJpaEntity.builder()
                    .purpose(purpose)
                    .build();

                assertEquals(purpose, entity.getPurpose());
            }
        }

        @Test
        @DisplayName("should create entity with all residence types")
        void shouldCreateEntityWithAllResidenceTypes() {
            for (ResidenceType residenceType : ResidenceType.values()) {
                final AddressJpaEntity entity = AddressJpaEntity.builder()
                    .residenceType(residenceType)
                    .build();

                assertEquals(residenceType, entity.getResidenceType());
            }
        }

        @Test
        @DisplayName("should create entity with all street types")
        void shouldCreateEntityWithAllStreetTypes() {
            for (StreetType streetType : StreetType.values()) {
                final AddressJpaEntity entity = AddressJpaEntity.builder()
                    .streetType(streetType)
                    .build();

                assertEquals(streetType, entity.getStreetType());
            }
        }

        @Test
        @DisplayName("should create entity with default isActive as true")
        void shouldCreateEntityWithDefaultIsActiveAsTrue() {
            final AddressJpaEntity entity = AddressJpaEntity.builder().build();
            assertTrue(entity.isActive());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final AddressJpaEntity entity = AddressJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getCustomer());
            assertNull(entity.getPurpose());
            assertNull(entity.getLabel());
            assertNull(entity.getResidenceType());
            assertNull(entity.getStreetType());
            assertNull(entity.getStreet());
            assertNull(entity.getNumber());
            assertNull(entity.getNeighborhood());
            assertNull(entity.getZipCode());
            assertNull(entity.getCity());
            assertNull(entity.getState());
            assertNull(entity.getCountry());
            assertNull(entity.getObservations());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final AddressJpaEntity entity = new AddressJpaEntity();

            assertNull(entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getCustomer());
            assertNull(entity.getPurpose());
            assertNull(entity.getLabel());
            assertNull(entity.getResidenceType());
            assertNull(entity.getStreetType());
            assertNull(entity.getStreet());
            assertNull(entity.getNumber());
            assertNull(entity.getNeighborhood());
            assertNull(entity.getZipCode());
            assertNull(entity.getCity());
            assertNull(entity.getState());
            assertNull(entity.getCountry());
            assertNull(entity.getObservations());
        }
    }

    @Nested
    @DisplayName("AllArgsConstructor")
    class AllArgsConstructorTests {
        @Test
        @DisplayName("should create entity with all args")
        void shouldCreateEntityWithAllArgs() {
            final UUID id = UUID.randomUUID();
            final boolean isActive = false;
            final Instant deactivatedAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final AddressPurpose purpose = AddressPurpose.BILLING;
            final String label = "Work Address";
            final ResidenceType residenceType = ResidenceType.COMMERCIAL;
            final StreetType streetType = StreetType.STREET;
            final String street = "Commerce Street";
            final String number = "456";
            final String neighborhood = "Business District";
            final String zipCode = "98765-432";
            final String city = "Rio de Janeiro";
            final String state = "RJ";
            final String country = "Brazil";
            final String observations = "Office building";

            final AddressJpaEntity entity = new AddressJpaEntity(
                id, isActive, deactivatedAt, createdAt, updatedAt, customer, purpose, label,
                residenceType, streetType, street, number, neighborhood, zipCode, city, state, country, observations
            );

            assertEquals(id, entity.getId());
            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(customer, entity.getCustomer());
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
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final AddressJpaEntity entity = new AddressJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant deactivatedAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final AddressPurpose purpose = AddressPurpose.DELIVERY;
            final String label = "Delivery Address";
            final ResidenceType residenceType = ResidenceType.APARTMENT;
            final StreetType streetType = StreetType.BOULEVARD;
            final String street = "Ocean Boulevard";
            final String number = "789";
            final String neighborhood = "Beachside";
            final String zipCode = "11111-222";
            final String city = "Santos";
            final String state = "SP";
            final String country = "Brazil";
            final String observations = "Apt 10B";

            entity.setId(id);
            entity.setActive(false);
            entity.setDeactivatedAt(deactivatedAt);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setCustomer(customer);
            entity.setPurpose(purpose);
            entity.setLabel(label);
            entity.setResidenceType(residenceType);
            entity.setStreetType(streetType);
            entity.setStreet(street);
            entity.setNumber(number);
            entity.setNeighborhood(neighborhood);
            entity.setZipCode(zipCode);
            entity.setCity(city);
            entity.setState(state);
            entity.setCountry(country);
            entity.setObservations(observations);

            assertEquals(id, entity.getId());
            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(customer, entity.getCustomer());
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
        }
    }
}
