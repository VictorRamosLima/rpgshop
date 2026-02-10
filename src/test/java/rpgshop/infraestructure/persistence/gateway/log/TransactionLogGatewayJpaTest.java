package rpgshop.infraestructure.persistence.gateway.log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rpgshop.domain.entity.log.TransactionLog;
import rpgshop.domain.entity.log.constant.OperationType;
import rpgshop.infraestructure.persistence.entity.log.TransactionLogJpaEntity;
import rpgshop.infraestructure.persistence.repository.log.TransactionLogRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionLogGatewayJpaTest {
    @Mock
    private TransactionLogRepository transactionLogRepository;

    @InjectMocks
    private TransactionLogGatewayJpa transactionLogGatewayJpa;

    @Test
    void shouldSaveTransactionLog() {
        final UUID logId = UUID.randomUUID();
        final UUID entityId = UUID.randomUUID();
        final Instant now = Instant.now();

        final TransactionLog log = TransactionLog.builder()
            .id(logId)
            .entityName("Product")
            .entityId(entityId)
            .operation(OperationType.INSERT)
            .responsibleUser("admin")
            .timestamp(now)
            .previousData(null)
            .newData("{\"name\": \"Product A\"}")
            .build();

        final TransactionLogJpaEntity savedEntity = TransactionLogJpaEntity.builder()
            .id(logId)
            .entityName("Product")
            .entityId(entityId)
            .operation(OperationType.INSERT)
            .responsibleUser("admin")
            .timestamp(now)
            .build();

        when(transactionLogRepository.save(any(TransactionLogJpaEntity.class))).thenReturn(savedEntity);

        final TransactionLog result = transactionLogGatewayJpa.save(log);

        assertNotNull(result);
        assertEquals(logId, result.id());
        assertEquals("Product", result.entityName());
        assertEquals(OperationType.INSERT, result.operation());
        verify(transactionLogRepository, times(1)).save(argThat(entity ->
            entity.getEntityName().equals("Product") && entity.getOperation() == OperationType.INSERT
        ));
    }

    @Test
    void shouldFindByEntityNameAndEntityId() {
        final UUID logId = UUID.randomUUID();
        final UUID entityId = UUID.randomUUID();
        final String entityName = "Product";
        final Instant now = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);

        final TransactionLogJpaEntity entity = TransactionLogJpaEntity.builder()
            .id(logId)
            .entityName(entityName)
            .entityId(entityId)
            .operation(OperationType.UPDATE)
            .responsibleUser("admin")
            .timestamp(now)
            .build();

        final Page<TransactionLogJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(transactionLogRepository.findByEntityNameAndEntityId(entityName, entityId, pageable)).thenReturn(page);

        final Page<TransactionLog> result = transactionLogGatewayJpa.findByEntityNameAndEntityId(entityName, entityId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(entityName, result.getContent().getFirst().entityName());
        assertEquals(entityId, result.getContent().getFirst().entityId());
        verify(transactionLogRepository, times(1)).findByEntityNameAndEntityId(entityName, entityId, pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoLogsFoundByEntityNameAndEntityId() {
        final UUID entityId = UUID.randomUUID();
        final String entityName = "Product";
        final Pageable pageable = PageRequest.of(0, 10);

        final Page<TransactionLogJpaEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(transactionLogRepository.findByEntityNameAndEntityId(entityName, entityId, pageable)).thenReturn(emptyPage);

        final Page<TransactionLog> result = transactionLogGatewayJpa.findByEntityNameAndEntityId(entityName, entityId, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transactionLogRepository, times(1)).findByEntityNameAndEntityId(entityName, entityId, pageable);
    }

    @Test
    void shouldFindByFilters() {
        final UUID logId = UUID.randomUUID();
        final UUID entityId = UUID.randomUUID();
        final String entityName = "Product";
        final OperationType operation = OperationType.UPDATE;
        final String responsibleUser = "admin";
        final Instant startDate = Instant.now().minusSeconds(86400);
        final Instant endDate = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);

        final TransactionLogJpaEntity entity = TransactionLogJpaEntity.builder()
            .id(logId)
            .entityName(entityName)
            .entityId(entityId)
            .operation(operation)
            .responsibleUser(responsibleUser)
            .timestamp(Instant.now().minusSeconds(3600))
            .build();

        final Page<TransactionLogJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(transactionLogRepository.findByFilters(
            entityName, entityId, operation, responsibleUser, startDate, endDate, pageable
        )).thenReturn(page);

        final Page<TransactionLog> result = transactionLogGatewayJpa.findByFilters(
            entityName, entityId, operation, responsibleUser, startDate, endDate, pageable
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(operation, result.getContent().getFirst().operation());
        assertEquals(responsibleUser, result.getContent().getFirst().responsibleUser());
        verify(transactionLogRepository, times(1)).findByFilters(
            entityName,
            entityId,
            operation,
            responsibleUser,
            startDate,
            endDate,
            pageable
        );
    }

    @Test
    void shouldFindByFiltersWithNullOptionalParameters() {
        final UUID logId = UUID.randomUUID();
        final UUID entityId = UUID.randomUUID();
        final Pageable pageable = PageRequest.of(0, 10);
        final Instant now = Instant.now();

        final TransactionLogJpaEntity entity = TransactionLogJpaEntity.builder()
            .id(logId)
            .entityName("Product")
            .entityId(entityId)
            .operation(OperationType.INSERT)
            .responsibleUser("admin")
            .timestamp(now)
            .build();

        final Page<TransactionLogJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(transactionLogRepository.findByFilters(null, null, null, null, null, null, pageable)).thenReturn(page);

        final Page<TransactionLog> result = transactionLogGatewayJpa.findByFilters(
            null, null, null, null, null, null, pageable
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(transactionLogRepository, times(1)).findByFilters(
            null, null, null, null, null, null, pageable
        );
    }

    @Test
    void shouldReturnEmptyPageWhenNoLogsMatchFilters() {
        final Pageable pageable = PageRequest.of(0, 10);

        final Page<TransactionLogJpaEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(transactionLogRepository.findByFilters("NonExistent", null, null, null, null, null, pageable)).thenReturn(emptyPage);

        final Page<TransactionLog> result = transactionLogGatewayJpa.findByFilters("NonExistent", null, null, null, null, null, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(transactionLogRepository, times(1)).findByFilters(
            "NonExistent", null, null, null, null, null, pageable
        );
    }
}
