package rpgshop.domain.entity.log;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.log.constant.OperationType;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static rpgshop.domain.entity.log.constant.OperationType.UPDATE;

class TransactionLogTest {
    @Test
    void shouldCreateTransactionLog() {
        final UUID id = UUID.randomUUID();
        final String entityName = "Customer";
        final UUID entityId = UUID.randomUUID();
        final OperationType operation = OperationType.INSERT;
        final UUID userId = UUID.randomUUID();
        final Instant timestamp = Instant.now();
        final String newData = "{\"name\": \"John\"}";

        final TransactionLog transactionLog = TransactionLog.builder()
            .id(id)
            .entityName(entityName)
            .entityId(entityId)
            .operation(operation)
            .userId(userId)
            .timestamp(timestamp)
            .previousData(null)
            .newData(newData)
            .build();

        assertEquals(id, transactionLog.id());
        assertEquals(entityName, transactionLog.entityName());
        assertEquals(entityId, transactionLog.entityId());
        assertEquals(operation, transactionLog.operation());
        assertEquals(userId, transactionLog.userId());
        assertEquals(timestamp, transactionLog.timestamp());
        assertNull(transactionLog.previousData());
        assertEquals(newData, transactionLog.newData());
    }

    @Test
    void shouldCreateTransactionLogWithAllOperationTypes() {
        for (OperationType operation : OperationType.values()) {
            final TransactionLog log = TransactionLog.builder().operation(operation).build();
            assertEquals(operation, log.operation());
        }
    }

    @Test
    void shouldCreateTransactionLogWithNullValues() {
        final TransactionLog transactionLog = TransactionLog.builder().build();

        assertNull(transactionLog.id());
        assertNull(transactionLog.entityName());
        assertNull(transactionLog.entityId());
        assertNull(transactionLog.operation());
        assertNull(transactionLog.userId());
        assertNull(transactionLog.timestamp());
        assertNull(transactionLog.previousData());
        assertNull(transactionLog.newData());
    }

    @Test
    void shouldCreateTransactionLogForUpdateOperation() {
        final String previousData = "{\"name\": \"John\"}";
        final String newData = "{\"name\": \"Jane\"}";

        final TransactionLog transactionLog = TransactionLog.builder()
            .operation(UPDATE)
            .previousData(previousData)
            .newData(newData)
            .build();

        assertEquals(UPDATE, transactionLog.operation());
        assertEquals(previousData, transactionLog.previousData());
        assertEquals(newData, transactionLog.newData());
    }

    @Test
    void shouldCreateTransactionLogWithNullUserId() {
        final TransactionLog transactionLog = TransactionLog.builder()
            .entityName("Product")
            .entityId(UUID.randomUUID())
            .operation(OperationType.INSERT)
            .userId(null)
            .newData("{\"name\": \"Product A\"}")
            .build();

        assertNull(transactionLog.userId());
        assertEquals("Product", transactionLog.entityName());
    }
}
