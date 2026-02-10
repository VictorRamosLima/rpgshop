package rpgshop.infraestructure.persistence.entity.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CreditCardJpaEntity")
class CreditCardJpaEntityTest {
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
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final String cardNumber = "4111111111111111";
            final String printedName = "JOHN DOE";
            final CardBrandJpaEntity cardBrand = CardBrandJpaEntity.builder().name("Visa").build();
            final String securityCode = "123";
            final boolean isPreferred = true;

            final CreditCardJpaEntity entity = CreditCardJpaEntity.builder()
                .id(id)
                .isActive(isActive)
                .deactivatedAt(deactivatedAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .customer(customer)
                .cardNumber(cardNumber)
                .printedName(printedName)
                .cardBrand(cardBrand)
                .securityCode(securityCode)
                .isPreferred(isPreferred)
                .build();

            assertEquals(id, entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(customer, entity.getCustomer());
            assertEquals(cardNumber, entity.getCardNumber());
            assertEquals(printedName, entity.getPrintedName());
            assertEquals(cardBrand, entity.getCardBrand());
            assertEquals(securityCode, entity.getSecurityCode());
            assertTrue(entity.isPreferred());
        }

        @Test
        @DisplayName("should create entity with default isActive as true")
        void shouldCreateEntityWithDefaultIsActiveAsTrue() {
            final CreditCardJpaEntity entity = CreditCardJpaEntity.builder().build();

            assertTrue(entity.isActive());
        }

        @Test
        @DisplayName("should create entity with default isPreferred as false")
        void shouldCreateEntityWithDefaultIsPreferredAsFalse() {
            final CreditCardJpaEntity entity = CreditCardJpaEntity.builder().build();

            assertFalse(entity.isPreferred());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final CreditCardJpaEntity entity = CreditCardJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getCustomer());
            assertNull(entity.getCardNumber());
            assertNull(entity.getPrintedName());
            assertNull(entity.getCardBrand());
            assertNull(entity.getSecurityCode());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final CreditCardJpaEntity entity = new CreditCardJpaEntity();

            assertNull(entity.getId());
            assertTrue(entity.isActive());
            assertNull(entity.getDeactivatedAt());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getCustomer());
            assertNull(entity.getCardNumber());
            assertNull(entity.getPrintedName());
            assertNull(entity.getCardBrand());
            assertNull(entity.getSecurityCode());
            assertFalse(entity.isPreferred());
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
            final String cardNumber = "5500000000000004";
            final String printedName = "JANE DOE";
            final CardBrandJpaEntity cardBrand = CardBrandJpaEntity.builder().name("MasterCard").build();
            final String securityCode = "456";
            final boolean isPreferred = false;

            final CreditCardJpaEntity entity = new CreditCardJpaEntity(
                id, isActive, deactivatedAt, createdAt, updatedAt, customer,
                cardNumber, printedName, cardBrand, securityCode, isPreferred
            );

            assertEquals(id, entity.getId());
            assertFalse(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(customer, entity.getCustomer());
            assertEquals(cardNumber, entity.getCardNumber());
            assertEquals(printedName, entity.getPrintedName());
            assertEquals(cardBrand, entity.getCardBrand());
            assertEquals(securityCode, entity.getSecurityCode());
            assertFalse(entity.isPreferred());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {

        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final CreditCardJpaEntity entity = new CreditCardJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant deactivatedAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final String cardNumber = "378282246310005";
            final String printedName = "ALEX SMITH";
            final CardBrandJpaEntity cardBrand = CardBrandJpaEntity.builder().name("Amex").build();
            final String securityCode = "7890";

            entity.setId(id);
            entity.setActive(true);
            entity.setDeactivatedAt(deactivatedAt);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setCustomer(customer);
            entity.setCardNumber(cardNumber);
            entity.setPrintedName(printedName);
            entity.setCardBrand(cardBrand);
            entity.setSecurityCode(securityCode);
            entity.setPreferred(true);

            assertEquals(id, entity.getId());
            assertTrue(entity.isActive());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(customer, entity.getCustomer());
            assertEquals(cardNumber, entity.getCardNumber());
            assertEquals(printedName, entity.getPrintedName());
            assertEquals(cardBrand, entity.getCardBrand());
            assertEquals(securityCode, entity.getSecurityCode());
            assertTrue(entity.isPreferred());
        }
    }
}
