package rpgshop.application.command.customer;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateCreditCardCommandTest {

    @Test
    void shouldCreateCreateCreditCardCommand() {
        final UUID customerId = UUID.randomUUID();
        final String cardNumber = "1234567890123456";
        final String printedName = "JOHN DOE";
        final UUID cardBrandId = UUID.randomUUID();
        final String securityCode = "123";
        final boolean isPreferred = true;

        final CreateCreditCardCommand command = new CreateCreditCardCommand(
            customerId,
            cardNumber,
            printedName,
            cardBrandId,
            securityCode,
            isPreferred
        );

        assertEquals(customerId, command.customerId());
        assertEquals(cardNumber, command.cardNumber());
        assertEquals(printedName, command.printedName());
        assertEquals(cardBrandId, command.cardBrandId());
        assertEquals(securityCode, command.securityCode());
        assertTrue(command.isPreferred());
    }
}

