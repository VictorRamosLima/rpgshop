package rpgshop.infraestructure.persistence.mapper.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.customer.CardBrand;
import rpgshop.domain.entity.customer.CreditCard;
import rpgshop.infraestructure.persistence.entity.customer.CardBrandJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CreditCardJpaEntity;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CreditCardMapper")
class CreditCardMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final String cardNumber = "4111111111111111";
            final String printedName = "JOHN DOE";
            final String securityCode = "123";
            final boolean isPreferred = true;
            final boolean isActive = true;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final CardBrandJpaEntity cardBrandEntity = CardBrandJpaEntity.builder()
                .id(UUID.randomUUID())
                .name("Visa")
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final CreditCardJpaEntity entity = CreditCardJpaEntity.builder()
                .id(id)
                .cardNumber(cardNumber)
                .printedName(printedName)
                .cardBrand(cardBrandEntity)
                .securityCode(securityCode)
                .isPreferred(isPreferred)
                .isActive(isActive)
                .deactivatedAt(null)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final CreditCard domain = CreditCardMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertEquals(cardNumber, domain.cardNumber());
            assertEquals(printedName, domain.printedName());
            assertNotNull(domain.cardBrand());
            assertEquals(cardBrandEntity.getName(), domain.cardBrand().name());
            assertEquals(securityCode, domain.securityCode());
            assertEquals(isPreferred, domain.isPreferred());
            assertEquals(isActive, domain.isActive());
            assertNull(domain.deactivatedAt());
            assertEquals(createdAt, domain.createdAt());
            assertEquals(updatedAt, domain.updatedAt());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> CreditCardMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final String cardNumber = "5500000000000004";
            final String printedName = "JANE DOE";
            final String securityCode = "456";
            final boolean isPreferred = false;
            final boolean isActive = false;
            final Instant deactivatedAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final CardBrand cardBrand = CardBrand.builder()
                .id(UUID.randomUUID())
                .name("MasterCard")
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final CreditCard domain = CreditCard.builder()
                .id(id)
                .cardNumber(cardNumber)
                .printedName(printedName)
                .cardBrand(cardBrand)
                .securityCode(securityCode)
                .isPreferred(isPreferred)
                .isActive(isActive)
                .deactivatedAt(deactivatedAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final CreditCardJpaEntity entity = CreditCardMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertEquals(cardNumber, entity.getCardNumber());
            assertEquals(printedName, entity.getPrintedName());
            assertNotNull(entity.getCardBrand());
            assertEquals(cardBrand.name(), entity.getCardBrand().getName());
            assertEquals(securityCode, entity.getSecurityCode());
            assertEquals(isPreferred, entity.isPreferred());
            assertEquals(isActive, entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> CreditCardMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = CreditCardMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            final var exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
            assertInstanceOf(IllegalInstantiationException.class, exception.getCause());
        }
    }
}
