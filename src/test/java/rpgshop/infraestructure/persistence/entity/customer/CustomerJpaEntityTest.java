package rpgshop.infraestructure.persistence.entity.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.customer.constant.Gender;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CustomerJpaEntity")
class CustomerJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final boolean isActive = true;
            final Instant deactivatedAt = null;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final Gender gender = Gender.MALE;
            final String name = "John Doe";
            final LocalDate dateOfBirth = LocalDate.of(1990, 5, 15);
            final String cpf = "12345678901";
            final String email = "john.doe@email.com";

            final BigDecimal ranking = new BigDecimal("4.50");
            final String customerCode = "CUST-001";
            final List<PhoneJpaEntity> phones = new ArrayList<>();
            final List<AddressJpaEntity> addresses = new ArrayList<>();
            final List<CreditCardJpaEntity> creditCards = new ArrayList<>();

            final CustomerJpaEntity entity = CustomerJpaEntity.builder()
                .id(id)
                .isActive(isActive)
                .deactivatedAt(deactivatedAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .gender(gender)
                .name(name)
                .dateOfBirth(dateOfBirth)
                .cpf(cpf)
                .email(email)

                .ranking(ranking)
                .customerCode(customerCode)
                .phones(phones)
                .addresses(addresses)
                .creditCards(creditCards)
                .build();

            assertEquals(id, entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(gender, entity.getGender());
            assertEquals(name, entity.getName());
            assertEquals(dateOfBirth, entity.getDateOfBirth());
            assertEquals(cpf, entity.getCpf());
            assertEquals(email, entity.getEmail());

            assertEquals(ranking, entity.getRanking());
            assertEquals(customerCode, entity.getCustomerCode());
            assertEquals(phones, entity.getPhones());
            assertEquals(addresses, entity.getAddresses());
            assertEquals(creditCards, entity.getCreditCards());
        }

        @Test
        @DisplayName("should create entity with all gender types")
        void shouldCreateEntityWithAllGenderTypes() {
            for (Gender gender : Gender.values()) {
                final CustomerJpaEntity entity = CustomerJpaEntity.builder()
                    .gender(gender)
                    .build();

                assertEquals(gender, entity.getGender());
            }
        }

        @Test
        @DisplayName("should create entity with default values")
        void shouldCreateEntityWithDefaultValues() {
            final CustomerJpaEntity entity = CustomerJpaEntity.builder().build();

            assertTrue(entity.isActive());
            assertEquals(BigDecimal.ZERO, entity.getRanking());
            assertNotNull(entity.getPhones());
            assertNotNull(entity.getAddresses());
            assertNotNull(entity.getCreditCards());
            assertTrue(entity.getPhones().isEmpty());
            assertTrue(entity.getAddresses().isEmpty());
            assertTrue(entity.getCreditCards().isEmpty());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final CustomerJpaEntity entity = CustomerJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getGender());
            assertNull(entity.getName());
            assertNull(entity.getDateOfBirth());
            assertNull(entity.getCpf());
            assertNull(entity.getEmail());

            assertNull(entity.getCustomerCode());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {

        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final CustomerJpaEntity entity = new CustomerJpaEntity();

            assertNull(entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getGender());
            assertNull(entity.getName());
            assertNull(entity.getDateOfBirth());
            assertNull(entity.getCpf());
            assertNull(entity.getEmail());

            assertEquals(BigDecimal.ZERO, entity.getRanking());
            assertNull(entity.getCustomerCode());
            assertNotNull(entity.getPhones());
            assertNotNull(entity.getAddresses());
            assertNotNull(entity.getCreditCards());
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
            final Gender gender = Gender.FEMALE;
            final String name = "Jane Doe";
            final LocalDate dateOfBirth = LocalDate.of(1985, 3, 20);
            final String cpf = "98765432100";
            final String email = "jane.doe@email.com";
            final BigDecimal ranking = new BigDecimal("3.75");
            final String customerCode = "CUST-002";
            final List<PhoneJpaEntity> phones = new ArrayList<>();
            final List<AddressJpaEntity> addresses = new ArrayList<>();
            final List<CreditCardJpaEntity> creditCards = new ArrayList<>();

            final CustomerJpaEntity entity = new CustomerJpaEntity(
                id, isActive, deactivatedAt, createdAt, updatedAt, gender, name, dateOfBirth,
                cpf, email, ranking, customerCode, phones, addresses, creditCards
            );

            assertEquals(id, entity.getId());
            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(gender, entity.getGender());
            assertEquals(name, entity.getName());
            assertEquals(dateOfBirth, entity.getDateOfBirth());
            assertEquals(cpf, entity.getCpf());
            assertEquals(email, entity.getEmail());

            assertEquals(ranking, entity.getRanking());
            assertEquals(customerCode, entity.getCustomerCode());
            assertEquals(phones, entity.getPhones());
            assertEquals(addresses, entity.getAddresses());
            assertEquals(creditCards, entity.getCreditCards());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {

        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final CustomerJpaEntity entity = new CustomerJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant deactivatedAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final Gender gender = Gender.NON_BINARY;
            final String name = "Alex Smith";
            final LocalDate dateOfBirth = LocalDate.of(1995, 8, 10);
            final String cpf = "11122233344";
            final String email = "alex.smith@email.com";
            final BigDecimal ranking = new BigDecimal("5.00");
            final String customerCode = "CUST-003";
            final List<PhoneJpaEntity> phones = new ArrayList<>();
            final List<AddressJpaEntity> addresses = new ArrayList<>();
            final List<CreditCardJpaEntity> creditCards = new ArrayList<>();

            entity.setId(id);
            entity.setActive(false);
            entity.setDeactivatedAt(deactivatedAt);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setGender(gender);
            entity.setName(name);
            entity.setDateOfBirth(dateOfBirth);
            entity.setCpf(cpf);
            entity.setEmail(email);

            entity.setRanking(ranking);
            entity.setCustomerCode(customerCode);
            entity.setPhones(phones);
            entity.setAddresses(addresses);
            entity.setCreditCards(creditCards);

            assertEquals(id, entity.getId());
            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(gender, entity.getGender());
            assertEquals(name, entity.getName());
            assertEquals(dateOfBirth, entity.getDateOfBirth());
            assertEquals(cpf, entity.getCpf());
            assertEquals(email, entity.getEmail());

            assertEquals(ranking, entity.getRanking());
            assertEquals(customerCode, entity.getCustomerCode());
            assertEquals(phones, entity.getPhones());
            assertEquals(addresses, entity.getAddresses());
            assertEquals(creditCards, entity.getCreditCards());
        }
    }
}
