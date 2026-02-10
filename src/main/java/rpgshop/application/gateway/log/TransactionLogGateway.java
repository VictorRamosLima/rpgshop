package rpgshop.application.gateway.log;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rpgshop.domain.entity.log.TransactionLog;
import rpgshop.domain.entity.log.constant.OperationType;

import java.time.Instant;
import java.util.UUID;

public interface TransactionLogGateway {
    TransactionLog save(final TransactionLog log);
    Page<TransactionLog> findByEntityNameAndEntityId(final String entityName, final UUID entityId, final Pageable pageable);
    Page<TransactionLog> findByFilters(final String entityName, final UUID entityId, final OperationType operation, final String responsibleUser, final Instant startDate, final Instant endDate, final Pageable pageable);
}
