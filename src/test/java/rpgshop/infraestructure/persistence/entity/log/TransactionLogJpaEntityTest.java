package rpgshop.infraestructure.persistence.entity.log;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.log.constant.OperationType;
import rpgshop.infraestructure.persistence.entity.user.UserJpaEntity;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("TransactionLogJpaEntity")
class TransactionLogJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final String entityName = "Product";
            final UUID entityId = UUID.randomUUID();
            final OperationType operation = OperationType.INSERT;
            final UserJpaEntity user = UserJpaEntity.builder().id(UUID.randomUUID()).build();
            final Instant timestamp = Instant.now();
            final ObjectNode previousData = JsonNodeFactory.instance.objectNode();
            previousData.put("name", "Old Name");
            final ObjectNode newData = JsonNodeFactory.instance.objectNode();
            newData.put("name", "New Name");

            final TransactionLogJpaEntity entity = TransactionLogJpaEntity.builder()
                .id(id)
                .entityName(entityName)
                .entityId(entityId)
                .operation(operation)
                .user(user)
                .timestamp(timestamp)
                .previousData(previousData)
                .newData(newData)
                .build();

            assertEquals(id, entity.getId());
            assertEquals(entityName, entity.getEntityName());
            assertEquals(entityId, entity.getEntityId());
            assertEquals(operation, entity.getOperation());
            assertEquals(user, entity.getUser());
            assertEquals(timestamp, entity.getTimestamp());
            assertEquals(previousData, entity.getPreviousData());
            assertEquals(newData, entity.getNewData());
        }

        @Test
        @DisplayName("should create entity with INSERT operation type")
        void shouldCreateEntityWithInsertOperationType() {
            final TransactionLogJpaEntity entity = TransactionLogJpaEntity.builder()
                .operation(OperationType.INSERT)
                .build();

            assertEquals(OperationType.INSERT, entity.getOperation());
        }

        @Test
        @DisplayName("should create entity with UPDATE operation type")
        void shouldCreateEntityWithUpdateOperationType() {
            final TransactionLogJpaEntity entity = TransactionLogJpaEntity.builder()
                .operation(OperationType.UPDATE)
                .build();

            assertEquals(OperationType.UPDATE, entity.getOperation());
        }

        @Test
        @DisplayName("should create entity with all operation types")
        void shouldCreateEntityWithAllOperationTypes() {
            for (OperationType operation : OperationType.values()) {
                final TransactionLogJpaEntity entity = TransactionLogJpaEntity.builder()
                    .operation(operation)
                    .build();

                assertEquals(operation, entity.getOperation());
            }
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final TransactionLogJpaEntity entity = TransactionLogJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getEntityName());
            assertNull(entity.getEntityId());
            assertNull(entity.getOperation());
            assertNull(entity.getUser());
            assertNull(entity.getTimestamp());
            assertNull(entity.getPreviousData());
            assertNull(entity.getNewData());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final TransactionLogJpaEntity entity = new TransactionLogJpaEntity();

            assertNull(entity.getId());
            assertNull(entity.getEntityName());
            assertNull(entity.getEntityId());
            assertNull(entity.getOperation());
            assertNull(entity.getUser());
            assertNull(entity.getTimestamp());
            assertNull(entity.getPreviousData());
            assertNull(entity.getNewData());
        }
    }

    @Nested
    @DisplayName("AllArgsConstructor")
    class AllArgsConstructorTests {
        @Test
        @DisplayName("should create entity with all args")
        void shouldCreateEntityWithAllArgs() {
            final UUID id = UUID.randomUUID();
            final String entityName = "Customer";
            final UUID entityId = UUID.randomUUID();
            final OperationType operation = OperationType.UPDATE;
            final UserJpaEntity user = UserJpaEntity.builder().id(UUID.randomUUID()).build();
            final Instant timestamp = Instant.now();
            final ObjectNode previousData = JsonNodeFactory.instance.objectNode();
            previousData.put("email", "old@email.com");
            final ObjectNode newData = JsonNodeFactory.instance.objectNode();
            newData.put("email", "new@email.com");

            final TransactionLogJpaEntity entity = new TransactionLogJpaEntity(
                id, entityName, entityId, operation, user, timestamp, previousData, newData
            );

            assertEquals(id, entity.getId());
            assertEquals(entityName, entity.getEntityName());
            assertEquals(entityId, entity.getEntityId());
            assertEquals(operation, entity.getOperation());
            assertEquals(user, entity.getUser());
            assertEquals(timestamp, entity.getTimestamp());
            assertEquals(previousData, entity.getPreviousData());
            assertEquals(newData, entity.getNewData());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final TransactionLogJpaEntity entity = new TransactionLogJpaEntity();

            final UUID id = UUID.randomUUID();
            final String entityName = "Order";
            final UUID entityId = UUID.randomUUID();
            final OperationType operation = OperationType.INSERT;
            final UserJpaEntity user = UserJpaEntity.builder().id(UUID.randomUUID()).build();
            final Instant timestamp = Instant.now();
            final ObjectNode previousData = JsonNodeFactory.instance.objectNode();
            final ObjectNode newData = JsonNodeFactory.instance.objectNode();
            newData.put("status", "PROCESSING");

            entity.setId(id);
            entity.setEntityName(entityName);
            entity.setEntityId(entityId);
            entity.setOperation(operation);
            entity.setUser(user);
            entity.setTimestamp(timestamp);
            entity.setPreviousData(previousData);
            entity.setNewData(newData);

            assertEquals(id, entity.getId());
            assertEquals(entityName, entity.getEntityName());
            assertEquals(entityId, entity.getEntityId());
            assertEquals(operation, entity.getOperation());
            assertEquals(user, entity.getUser());
            assertEquals(timestamp, entity.getTimestamp());
            assertEquals(previousData, entity.getPreviousData());
            assertEquals(newData, entity.getNewData());
        }
    }

    @Nested
    @DisplayName("JSON Data")
    class JsonDataTests {
        @Test
        @DisplayName("should handle complex JSON data")
        void shouldHandleComplexJsonData() {
            final ObjectNode previousData = JsonNodeFactory.instance.objectNode();
            previousData.put("name", "Old Product");
            previousData.put("price", 10.99);
            previousData.put("active", true);

            final ObjectNode newData = JsonNodeFactory.instance.objectNode();
            newData.put("name", "New Product");
            newData.put("price", 15.99);
            newData.put("active", false);

            final TransactionLogJpaEntity entity = TransactionLogJpaEntity.builder()
                .previousData(previousData)
                .newData(newData)
                .build();

            final JsonNode prevData = entity.getPreviousData();
            final JsonNode currData = entity.getNewData();

            assertEquals("Old Product", prevData.get("name").asString());
            assertEquals(10.99, prevData.get("price").asDouble());
            assertTrue(prevData.get("active").asBoolean());

            assertEquals("New Product", currData.get("name").asString());
            assertEquals(15.99, currData.get("price").asDouble());
            assertFalse(currData.get("active").asBoolean());
        }

        @Test
        @DisplayName("should handle null previous data for INSERT operations")
        void shouldHandleNullPreviousDataForInsertOperations() {
            final ObjectNode newData = JsonNodeFactory.instance.objectNode();
            newData.put("id", "123");

            final TransactionLogJpaEntity entity = TransactionLogJpaEntity.builder()
                .operation(OperationType.INSERT)
                .previousData(null)
                .newData(newData)
                .build();

            assertNull(entity.getPreviousData());
            assertEquals("123", entity.getNewData().get("id").asString());
        }
    }
}
