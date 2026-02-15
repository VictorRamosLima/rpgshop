package rpgshop.infraestructure.persistence.mapper.log;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.log.TransactionLog;
import rpgshop.domain.entity.log.constant.OperationType;
import rpgshop.infraestructure.persistence.entity.log.TransactionLogJpaEntity;
import rpgshop.infraestructure.persistence.entity.user.UserJpaEntity;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("TransactionLogMapper")
class TransactionLogMapperTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final String entityName = "Product";
            final UUID entityId = UUID.randomUUID();
            final OperationType operation = OperationType.UPDATE;
            final UUID userId = UUID.randomUUID();
            final Instant timestamp = Instant.now();
            final var previousData = objectMapper.readTree("{\"name\":\"Old Name\"}");
            final var newData = objectMapper.readTree("{\"name\":\"New Name\"}");

            final TransactionLogJpaEntity entity = TransactionLogJpaEntity.builder()
                .id(id)
                .entityName(entityName)
                .entityId(entityId)
                .operation(operation)
                .user(UserJpaEntity.builder().id(userId).build())
                .timestamp(timestamp)
                .previousData(previousData)
                .newData(newData)
                .build();

            final TransactionLog domain = TransactionLogMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertEquals(entityName, domain.entityName());
            assertEquals(entityId, domain.entityId());
            assertEquals(operation, domain.operation());
            assertEquals(userId, domain.userId());
            assertEquals(timestamp, domain.timestamp());
            assertEquals(previousData.toString(), domain.previousData());
            assertEquals(newData.toString(), domain.newData());
        }

        @Test
        @DisplayName("should map entity with null user to domain")
        void shouldMapEntityWithNullUserToDomain() {
            final TransactionLogJpaEntity entity = TransactionLogJpaEntity.builder()
                .id(UUID.randomUUID())
                .entityName("Customer")
                .entityId(UUID.randomUUID())
                .operation(OperationType.INSERT)
                .user(null)
                .timestamp(Instant.now())
                .newData(objectMapper.readTree("{\"name\":\"New Customer\"}"))
                .build();

            final TransactionLog domain = TransactionLogMapper.toDomain(entity);

            assertNull(domain.userId());
        }

        @Test
        @DisplayName("should map entity with null previousData to domain")
        void shouldMapEntityWithNullPreviousDataToDomain() {
            final TransactionLogJpaEntity entity = TransactionLogJpaEntity.builder()
                .id(UUID.randomUUID())
                .entityName("Customer")
                .entityId(UUID.randomUUID())
                .operation(OperationType.INSERT)
                .timestamp(Instant.now())
                .previousData(null)
                .newData(objectMapper.readTree("{\"name\":\"New Customer\"}"))
                .build();

            final TransactionLog domain = TransactionLogMapper.toDomain(entity);

            assertNull(domain.previousData());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> TransactionLogMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final String entityName = "Order";
            final UUID entityId = UUID.randomUUID();
            final OperationType operation = OperationType.UPDATE;
            final UUID userId = UUID.randomUUID();
            final Instant timestamp = Instant.now();
            final String previousData = "{\"status\":\"ACTIVE\"}";
            final String newData = "{\"status\":\"DELETED\"}";

            final TransactionLog domain = TransactionLog.builder()
                .id(id)
                .entityName(entityName)
                .entityId(entityId)
                .operation(operation)
                .userId(userId)
                .timestamp(timestamp)
                .previousData(previousData)
                .newData(newData)
                .build();

            final TransactionLogJpaEntity entity = TransactionLogMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertEquals(entityName, entity.getEntityName());
            assertEquals(entityId, entity.getEntityId());
            assertEquals(operation, entity.getOperation());
            assertNotNull(entity.getUser());
            assertEquals(userId, entity.getUser().getId());
            assertEquals(timestamp, entity.getTimestamp());
            assertNotNull(entity.getPreviousData());
            assertNotNull(entity.getNewData());
        }

        @Test
        @DisplayName("should map domain with null userId to entity")
        void shouldMapDomainWithNullUserIdToEntity() {
            final TransactionLog domain = TransactionLog.builder()
                .id(UUID.randomUUID())
                .entityName("Customer")
                .entityId(UUID.randomUUID())
                .operation(OperationType.INSERT)
                .userId(null)
                .timestamp(Instant.now())
                .newData("{\"name\":\"New Customer\"}")
                .build();

            final TransactionLogJpaEntity entity = TransactionLogMapper.toEntity(domain);

            assertNull(entity.getUser());
        }

        @Test
        @DisplayName("should map domain with null previousData to entity")
        void shouldMapDomainWithNullPreviousDataToEntity() {
            final TransactionLog domain = TransactionLog.builder()
                .id(UUID.randomUUID())
                .entityName("Customer")
                .entityId(UUID.randomUUID())
                .operation(OperationType.UPDATE)
                .userId(UUID.randomUUID())
                .timestamp(Instant.now())
                .previousData(null)
                .newData("{\"name\":\"New Customer\"}")
                .build();

            final TransactionLogJpaEntity entity = TransactionLogMapper.toEntity(domain);

            assertNull(entity.getPreviousData());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> TransactionLogMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = TransactionLogMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            final var exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
            assertInstanceOf(IllegalInstantiationException.class, exception.getCause());
        }
    }
}
