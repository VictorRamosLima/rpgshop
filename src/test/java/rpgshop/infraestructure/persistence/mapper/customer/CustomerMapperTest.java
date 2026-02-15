package rpgshop.infraestructure.persistence.mapper.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.CardBrand;
import rpgshop.domain.entity.customer.CreditCard;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.Phone;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.domain.entity.customer.constant.PhoneType;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;
import rpgshop.infraestructure.persistence.entity.customer.AddressJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CardBrandJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CreditCardJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.PhoneJpaEntity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CustomerMapper")
class CustomerMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final Gender gender = Gender.MALE;
            final String name = "John Doe";
            final LocalDate dateOfBirth = LocalDate.of(1990, 5, 15);
            final String cpf = "12345678901";
            final String email = "john.doe@example.com";

            final BigDecimal ranking = new BigDecimal("4.50");
            final String customerCode = "CUST001";
            final boolean isActive = true;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final CustomerJpaEntity entity = CustomerJpaEntity.builder()
                .id(id)
                .gender(gender)
                .name(name)
                .dateOfBirth(dateOfBirth)
                .cpf(cpf)
                .email(email)

                .ranking(ranking)
                .customerCode(customerCode)
                .phones(Collections.emptyList())
                .addresses(Collections.emptyList())
                .creditCards(Collections.emptyList())
                .isActive(isActive)
                .deactivatedAt(null)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final Customer domain = CustomerMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertEquals(gender, domain.gender());
            assertEquals(name, domain.name());
            assertEquals(dateOfBirth, domain.dateOfBirth());
            assertEquals(cpf, domain.cpf());
            assertEquals(email, domain.email());

            assertEquals(ranking, domain.ranking());
            assertEquals(customerCode, domain.customerCode());
            assertTrue(domain.phones().isEmpty());
            assertTrue(domain.addresses().isEmpty());
            assertTrue(domain.creditCards().isEmpty());
            assertEquals(isActive, domain.isActive());
            assertNull(domain.deactivatedAt());
            assertEquals(createdAt, domain.createdAt());
            assertEquals(updatedAt, domain.updatedAt());
        }

        @Test
        @DisplayName("should map entity with phones to domain")
        void shouldMapEntityWithPhonesToDomain() {
            final PhoneJpaEntity phoneEntity = PhoneJpaEntity.builder()
                .id(UUID.randomUUID())
                .type(PhoneType.MOBILE)
                .areaCode("11")
                .number("999887766")
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final CustomerJpaEntity entity = createBasicCustomerEntity();
            entity.setPhones(List.of(phoneEntity));

            final Customer domain = CustomerMapper.toDomain(entity);

            assertEquals(1, domain.phones().size());
            assertEquals(phoneEntity.getNumber(), domain.phones().getFirst().number());
        }

        @Test
        @DisplayName("should map entity with addresses to domain")
        void shouldMapEntityWithAddressesToDomain() {
            final AddressJpaEntity addressEntity = AddressJpaEntity.builder()
                .id(UUID.randomUUID())
                .purpose(AddressPurpose.DELIVERY)
                .label("Home")
                .residenceType(ResidenceType.HOUSE)
                .streetType(StreetType.STREET)
                .street("Main Street")
                .number("123")
                .neighborhood("Downtown")
                .zipCode("12345678")
                .city("São Paulo")
                .state("SP")
                .country("Brazil")
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final CustomerJpaEntity entity = createBasicCustomerEntity();
            entity.setAddresses(List.of(addressEntity));

            final Customer domain = CustomerMapper.toDomain(entity);

            assertEquals(1, domain.addresses().size());
            assertEquals(addressEntity.getStreet(), domain.addresses().getFirst().street());
        }

        @Test
        @DisplayName("should map entity with credit cards to domain")
        void shouldMapEntityWithCreditCardsToDomain() {
            final CardBrandJpaEntity cardBrandEntity = CardBrandJpaEntity.builder()
                .id(UUID.randomUUID())
                .name("Visa")
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final CreditCardJpaEntity creditCardEntity = CreditCardJpaEntity.builder()
                .id(UUID.randomUUID())
                .cardNumber("4111111111111111")
                .printedName("JOHN DOE")
                .cardBrand(cardBrandEntity)
                .securityCode("123")
                .isPreferred(true)
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final CustomerJpaEntity entity = createBasicCustomerEntity();
            entity.setCreditCards(List.of(creditCardEntity));

            final Customer domain = CustomerMapper.toDomain(entity);

            assertEquals(1, domain.creditCards().size());
            assertEquals(creditCardEntity.getCardNumber(), domain.creditCards().getFirst().cardNumber());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> CustomerMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final Gender gender = Gender.FEMALE;
            final String name = "Jane Doe";
            final LocalDate dateOfBirth = LocalDate.of(1985, 10, 20);
            final String cpf = "98765432109";
            final String email = "jane.doe@example.com";

            final BigDecimal ranking = new BigDecimal("3.75");
            final String customerCode = "CUST002";
            final boolean isActive = false;
            final Instant deactivatedAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final Customer domain = Customer.builder()
                .id(id)
                .gender(gender)
                .name(name)
                .dateOfBirth(dateOfBirth)
                .cpf(cpf)
                .email(email)

                .ranking(ranking)
                .customerCode(customerCode)
                .phones(Collections.emptyList())
                .addresses(Collections.emptyList())
                .creditCards(Collections.emptyList())
                .isActive(isActive)
                .deactivatedAt(deactivatedAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final CustomerJpaEntity entity = CustomerMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertEquals(gender, entity.getGender());
            assertEquals(name, entity.getName());
            assertEquals(dateOfBirth, entity.getDateOfBirth());
            assertEquals(cpf, entity.getCpf());
            assertEquals(email, entity.getEmail());

            assertEquals(ranking, entity.getRanking());
            assertEquals(customerCode, entity.getCustomerCode());
            assertEquals(isActive, entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
        }

        @Test
        @DisplayName("should map domain with phones to entity")
        void shouldMapDomainWithPhonesToEntity() {
            final Phone phone = Phone.builder()
                .id(UUID.randomUUID())
                .type(PhoneType.MOBILE)
                .areaCode("21")
                .number("988776655")
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final Customer domain = createBasicCustomerDomain();
            final Customer customerWithPhones = domain.toBuilder()
                .phones(List.of(phone))
                .build();

            final CustomerJpaEntity entity = CustomerMapper.toEntity(customerWithPhones);

            assertEquals(1, entity.getPhones().size());
            assertEquals(phone.number(), entity.getPhones().getFirst().getNumber());
            assertEquals(entity, entity.getPhones().getFirst().getCustomer());
        }

        @Test
        @DisplayName("should map domain with addresses to entity")
        void shouldMapDomainWithAddressesToEntity() {
            final Address address = Address.builder()
                .id(UUID.randomUUID())
                .purpose(AddressPurpose.BILLING)
                .label("Work")
                .residenceType(ResidenceType.APARTMENT)
                .streetType(StreetType.AVENUE)
                .street("Business Ave")
                .number("456")
                .neighborhood("Commercial District")
                .zipCode("87654321")
                .city("Rio de Janeiro")
                .state("RJ")
                .country("Brazil")
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final Customer domain = createBasicCustomerDomain();
            final Customer customerWithAddresses = domain.toBuilder()
                .addresses(List.of(address))
                .build();

            final CustomerJpaEntity entity = CustomerMapper.toEntity(customerWithAddresses);

            assertEquals(1, entity.getAddresses().size());
            assertEquals(address.street(), entity.getAddresses().getFirst().getStreet());
            assertEquals(entity, entity.getAddresses().getFirst().getCustomer());
        }

        @Test
        @DisplayName("should map domain with credit cards to entity")
        void shouldMapDomainWithCreditCardsToEntity() {
            final CardBrand cardBrand = CardBrand.builder()
                .id(UUID.randomUUID())
                .name("MasterCard")
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final CreditCard creditCard = CreditCard.builder()
                .id(UUID.randomUUID())
                .cardNumber("5500000000000004")
                .printedName("JANE DOE")
                .cardBrand(cardBrand)
                .securityCode("456")
                .isPreferred(false)
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final Customer domain = createBasicCustomerDomain();
            final Customer customerWithCards = domain.toBuilder()
                .creditCards(List.of(creditCard))
                .build();

            final CustomerJpaEntity entity = CustomerMapper.toEntity(customerWithCards);

            assertEquals(1, entity.getCreditCards().size());
            assertEquals(creditCard.cardNumber(), entity.getCreditCards().getFirst().getCardNumber());
            assertEquals(entity, entity.getCreditCards().getFirst().getCustomer());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> CustomerMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = CustomerMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            final var exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
            assertInstanceOf(IllegalInstantiationException.class, exception.getCause());
        }
    }

    private CustomerJpaEntity createBasicCustomerEntity() {
        return CustomerJpaEntity.builder()
            .id(UUID.randomUUID())
            .gender(Gender.MALE)
            .name("Test User")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .cpf("11122233344")
            .email("test@example.com")

            .ranking(BigDecimal.ZERO)
            .customerCode("TEST001")
            .phones(Collections.emptyList())
            .addresses(Collections.emptyList())
            .creditCards(Collections.emptyList())
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private Customer createBasicCustomerDomain() {
        return Customer.builder()
            .id(UUID.randomUUID())
            .gender(Gender.MALE)
            .name("Test User")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .cpf("11122233344")
            .email("test@example.com")

            .ranking(BigDecimal.ZERO)
            .customerCode("TEST001")
            .phones(Collections.emptyList())
            .addresses(Collections.emptyList())
            .creditCards(Collections.emptyList())
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }
}
