package rpgshop.application.usecase.log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rpgshop.application.gateway.log.TransactionLogGateway;
import rpgshop.domain.entity.log.TransactionLog;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static rpgshop.domain.entity.log.constant.OperationType.UPDATE;

@ExtendWith(MockitoExtension.class)
class QueryTransactionLogsUseCaseTest {
    @Mock
    private TransactionLogGateway transactionLogGateway;

    @InjectMocks
    private QueryTransactionLogsUseCase useCase;

    @Test
    void shouldFindLogsByEntityAndId() {
        final Pageable pageable = PageRequest.of(0, 10);
        final UUID entityId = UUID.randomUUID();
        final TransactionLog transactionLog = TransactionLog.builder().id(UUID.randomUUID()).build();
        final Page<TransactionLog> page = new PageImpl<>(List.of(transactionLog));

        when(transactionLogGateway.findByEntityNameAndEntityId("Order", entityId, pageable)).thenReturn(page);

        final Page<TransactionLog> result = useCase.findByEntityNameAndEntityId("Order", entityId, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldFindLogsByFilters() {
        final Pageable pageable = PageRequest.of(0, 10);
        final TransactionLog transactionLog = TransactionLog.builder().id(UUID.randomUUID()).build();
        final Page<TransactionLog> page = new PageImpl<>(List.of(transactionLog));

        when(transactionLogGateway.findByFilters(any(), any(), any(), any(), any(), any(), any())).thenReturn(page);

        final Page<TransactionLog> result = useCase.findByFilters(
            "Order",
            UUID.randomUUID(),
            UPDATE,
            "admin",
            Instant.now().minusSeconds(3600),
            Instant.now(),
            pageable
        );

        assertEquals(1, result.getTotalElements());
    }
}
