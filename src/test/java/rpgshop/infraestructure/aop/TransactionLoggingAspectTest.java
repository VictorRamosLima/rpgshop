package rpgshop.infraestructure.aop;

import lombok.Builder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.gateway.log.TransactionLogGateway;
import rpgshop.domain.entity.log.TransactionLog;
import rpgshop.domain.entity.log.constant.OperationType;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionLoggingAspectTest {
    @Mock
    private TransactionLogGateway transactionLogGateway;

    @Mock
    private ProceedingJoinPoint joinPoint;

    private TransactionLoggingAspect aspect;

    @Builder
    record TestEntity(UUID id, String name) {}

    @BeforeEach
    void setUp() {
        aspect = new TransactionLoggingAspect(transactionLogGateway);
    }

    @AfterEach
    void tearDown() {
        UserContextHolder.clear();
    }

    @Test
    void shouldLogInsertOperationWhenEntityIsNew() throws Throwable {
        final UUID entityId = UUID.randomUUID();
        final TestEntity input = new TestEntity(entityId, "Product A");
        final TestEntity result = new TestEntity(entityId, "Product A");

        when(joinPoint.getArgs()).thenReturn(new Object[]{input});
        when(joinPoint.getTarget()).thenReturn(new GatewayWithoutFindById());
        when(joinPoint.proceed()).thenReturn(result);
        when(transactionLogGateway.save(any(TransactionLog.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        aspect.logSaveOperation(joinPoint);

        final ArgumentCaptor<TransactionLog> captor = ArgumentCaptor.forClass(TransactionLog.class);
        verify(transactionLogGateway).save(captor.capture());

        final TransactionLog savedLog = captor.getValue();
        assertEquals("TestEntity", savedLog.entityName());
        assertEquals(entityId, savedLog.entityId());
        assertEquals(OperationType.INSERT, savedLog.operation());
        assertNotNull(savedLog.newData());
        assertNull(savedLog.previousData());
    }

    @Test
    void shouldLogWithUserIdWhenUserContextIsSet() throws Throwable {
        final UUID userId = UUID.randomUUID();
        final UUID entityId = UUID.randomUUID();
        UserContextHolder.setCurrentUserId(userId);

        final TestEntity input = new TestEntity(entityId, "Product A");
        final TestEntity result = new TestEntity(entityId, "Product A");

        when(joinPoint.getArgs()).thenReturn(new Object[]{input});
        when(joinPoint.getTarget()).thenReturn(new GatewayWithoutFindById());
        when(joinPoint.proceed()).thenReturn(result);
        when(transactionLogGateway.save(any(TransactionLog.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        aspect.logSaveOperation(joinPoint);

        final ArgumentCaptor<TransactionLog> captor = ArgumentCaptor.forClass(TransactionLog.class);
        verify(transactionLogGateway).save(captor.capture());

        assertEquals(userId, captor.getValue().userId());
    }

    @Test
    void shouldLogWithNullUserIdWhenNoUserContext() throws Throwable {
        final UUID entityId = UUID.randomUUID();
        final TestEntity input = new TestEntity(entityId, "Product A");
        final TestEntity result = new TestEntity(entityId, "Product A");

        when(joinPoint.getArgs()).thenReturn(new Object[]{input});
        when(joinPoint.getTarget()).thenReturn(new GatewayWithoutFindById());
        when(joinPoint.proceed()).thenReturn(result);
        when(transactionLogGateway.save(any(TransactionLog.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        aspect.logSaveOperation(joinPoint);

        final ArgumentCaptor<TransactionLog> captor = ArgumentCaptor.forClass(TransactionLog.class);
        verify(transactionLogGateway).save(captor.capture());

        assertNull(captor.getValue().userId());
    }

    @Test
    void shouldDetectUpdateWhenFindByIdReturnsEntity() throws Throwable {
        final UUID entityId = UUID.randomUUID();
        final TestEntity input = new TestEntity(entityId, "Updated Name");
        final TestEntity existing = new TestEntity(entityId, "Original Name");
        final TestEntity result = new TestEntity(entityId, "Updated Name");

        when(joinPoint.getArgs()).thenReturn(new Object[]{input});
        when(joinPoint.getTarget()).thenReturn(new GatewayWithFindById(existing));
        when(joinPoint.proceed()).thenReturn(result);
        when(transactionLogGateway.save(any(TransactionLog.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        aspect.logSaveOperation(joinPoint);

        final ArgumentCaptor<TransactionLog> captor = ArgumentCaptor.forClass(TransactionLog.class);
        verify(transactionLogGateway).save(captor.capture());

        final TransactionLog savedLog = captor.getValue();
        assertEquals(OperationType.UPDATE, savedLog.operation());
        assertNotNull(savedLog.previousData());
        assertNotNull(savedLog.newData());
    }

    @Test
    void shouldStillReturnResultWhenLoggingFails() throws Throwable {
        final UUID entityId = UUID.randomUUID();
        final TestEntity input = new TestEntity(entityId, "Product A");
        final TestEntity result = new TestEntity(entityId, "Product A");

        when(joinPoint.getArgs()).thenReturn(new Object[]{input});
        when(joinPoint.getTarget()).thenReturn(new GatewayWithoutFindById());
        when(joinPoint.proceed()).thenReturn(result);
        when(transactionLogGateway.save(any(TransactionLog.class)))
            .thenThrow(new RuntimeException("DB error"));

        final Object returnValue = aspect.logSaveOperation(joinPoint);

        assertEquals(result, returnValue);
    }

    @Test
    void shouldPropagateExceptionFromOriginalOperation() throws Throwable {
        final UUID entityId = UUID.randomUUID();
        final TestEntity input = new TestEntity(entityId, "Product A");

        when(joinPoint.getArgs()).thenReturn(new Object[]{input});
        when(joinPoint.getTarget()).thenReturn(new GatewayWithoutFindById());
        when(joinPoint.proceed()).thenThrow(new RuntimeException("Save failed"));

        try {
            aspect.logSaveOperation(joinPoint);
        } catch (RuntimeException e) {
            assertEquals("Save failed", e.getMessage());
        }

        verify(transactionLogGateway, never()).save(any());
    }

    static class GatewayWithoutFindById {}

    static class GatewayWithFindById {
        private final Object entity;

        GatewayWithFindById(final Object entity) {
            this.entity = entity;
        }

        public Optional<Object> findById(final UUID id) {
            return Optional.ofNullable(entity);
        }
    }
}
