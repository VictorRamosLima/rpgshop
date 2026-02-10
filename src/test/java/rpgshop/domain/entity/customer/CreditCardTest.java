package rpgshop.domain.entity.customer;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreditCardTest {
    @Test
    void shouldCreateCreditCard() {
        final UUID id = UUID.randomUUID();
        final String cardNumber = "4111111111111111";
        final String printedName = "JOHN DOE";
        final CardBrand cardBrand = CardBrand.builder().name("Visa").build();
        final String securityCode = "123";
        final boolean isPreferred = true;
        final boolean isActive = true;
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final CreditCard creditCard = CreditCard.builder()
            .id(id)
            .cardNumber(cardNumber)
            .printedName(printedName)
            .cardBrand(cardBrand)
            .securityCode(securityCode)
            .isPreferred(isPreferred)
            .isActive(isActive)
            .deactivatedAt(null)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, creditCard.id());
        assertEquals(cardNumber, creditCard.cardNumber());
        assertEquals(printedName, creditCard.printedName());
        assertEquals(cardBrand, creditCard.cardBrand());
        assertEquals(securityCode, creditCard.securityCode());
        assertTrue(creditCard.isPreferred());
        assertTrue(creditCard.isActive());
        assertNull(creditCard.deactivatedAt());
        assertEquals(createdAt, creditCard.createdAt());
        assertEquals(updatedAt, creditCard.updatedAt());
    }

    @Test
    void shouldCreateCreditCardWithNullValues() {
        final CreditCard creditCard = CreditCard.builder().build();

        assertNull(creditCard.id());
        assertNull(creditCard.cardNumber());
        assertNull(creditCard.printedName());
        assertNull(creditCard.cardBrand());
        assertNull(creditCard.securityCode());
        assertFalse(creditCard.isPreferred());
        assertFalse(creditCard.isActive());
        assertNull(creditCard.deactivatedAt());
        assertNull(creditCard.createdAt());
        assertNull(creditCard.updatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final String originalPrintedName = "JOHN DOE";
        final String newPrintedName = "JANE DOE";

        final CreditCard original = CreditCard.builder().printedName(originalPrintedName).build();
        final CreditCard modified = original.toBuilder().printedName(newPrintedName).build();

        assertEquals(originalPrintedName, original.printedName());
        assertEquals(newPrintedName, modified.printedName());
    }
}

