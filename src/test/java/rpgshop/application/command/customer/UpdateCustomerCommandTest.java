package rpgshop.application.command.customer;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.customer.constant.Gender;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateCustomerCommandTest {
    @Test
    void shouldCreateUpdateCustomerCommand() {
        final UUID id = UUID.randomUUID();
        final Gender gender = Gender.FEMALE;
        final String name = "Jane Doe";
        final LocalDate dateOfBirth = LocalDate.of(1995, 5, 20);
        final String email = "jane@example.com";

        final UpdateCustomerCommand command = new UpdateCustomerCommand(
            id,
            gender,
            name,
            dateOfBirth,
            email
        );

        assertEquals(id, command.id());
        assertEquals(gender, command.gender());
        assertEquals(name, command.name());
        assertEquals(dateOfBirth, command.dateOfBirth());
        assertEquals(email, command.email());
    }
}
