package rpgshop.application.usecase.log;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.gateway.log.TransactionLogGateway;
import rpgshop.domain.entity.log.TransactionLog;
import rpgshop.domain.entity.log.constant.OperationType;

import java.time.Instant;
import java.util.UUID;

@Service
public class QueryTransactionLogsUseCase {
    private final TransactionLogGateway transactionLogGateway;

    public QueryTransactionLogsUseCase(final TransactionLogGateway transactionLogGateway) {
        this.transactionLogGateway = transactionLogGateway;
    }

    @Transactional(readOnly = true)
    public Page<TransactionLog> findByEntityNameAndEntityId(
        final String entityName,
        final UUID entityId,
        final Pageable pageable
    ) {
        return transactionLogGateway.findByEntityNameAndEntityId(entityName, entityId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<TransactionLog> findByFilters(
        final String entityName,
        final UUID entityId,
        final OperationType operation,
        final UUID userId,
        final Instant startDate,
        final Instant endDate,
        final Pageable pageable
    ) {
        return transactionLogGateway.findByFilters(
            entityName, entityId, operation, userId, startDate, endDate, pageable
        );
    }
}
