package rpgshop.application.command.customer;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.customer.constant.PhoneType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreatePhoneCommandTest {

    @Test
    void shouldCreateCreatePhoneCommand() {
        final UUID customerId = UUID.randomUUID();
        final PhoneType type = PhoneType.MOBILE;
        final String areaCode = "11";
        final String number = "999999999";

        final CreatePhoneCommand command = new CreatePhoneCommand(
            customerId,
            type,
            areaCode,
            number
        );

        assertEquals(customerId, command.customerId());
        assertEquals(type, command.type());
        assertEquals(areaCode, command.areaCode());
        assertEquals(number, command.number());
    }
}

