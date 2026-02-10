package rpgshop.application.command.customer;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangePasswordCommandTest {
    @Test
    void shouldCreateChangePasswordCommand() {
        final UUID customerId = UUID.randomUUID();
        final String newPassword = "newPassword123";
        final String confirmPassword = "newPassword123";

        final ChangePasswordCommand command = new ChangePasswordCommand(
            customerId,
            newPassword,
            confirmPassword
        );

        assertEquals(customerId, command.customerId());
        assertEquals(newPassword, command.newPassword());
        assertEquals(confirmPassword, command.confirmPassword());
    }
}

