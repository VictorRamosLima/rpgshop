package rpgshop.application.command.log;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.log.constant.OperationType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CreateTransactionLogCommandTest {
    @Test
    void shouldCreateCreateTransactionLogCommand() {
        final String entityName = "Customer";
        final UUID entityId = UUID.randomUUID();
        final OperationType operation = OperationType.INSERT;
        final String responsibleUser = "admin";
        final String newData = "{\"name\": \"John\"}";

        final CreateTransactionLogCommand command = new CreateTransactionLogCommand(
            entityName,
            entityId,
            operation,
            responsibleUser,
            null,
            newData
        );

        assertEquals(entityName, command.entityName());
        assertEquals(entityId, command.entityId());
        assertEquals(operation, command.operation());
        assertEquals(responsibleUser, command.responsibleUser());
        assertNull(command.previousData());
        assertEquals(newData, command.newData());
    }
}

