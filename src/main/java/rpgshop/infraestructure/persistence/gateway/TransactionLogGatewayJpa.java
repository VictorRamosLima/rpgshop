package rpgshop.infraestructure.persistence.gateway;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rpgshop.application.gateway.log.TransactionLogGateway;
import rpgshop.domain.entity.log.TransactionLog;
import rpgshop.domain.entity.log.constant.OperationType;
import rpgshop.infraestructure.mapper.log.TransactionLogMapper;
import rpgshop.infraestructure.persistence.repository.log.TransactionLogRepository;

import java.time.Instant;
import java.util.UUID;

@Component
public class TransactionLogGatewayJpa implements TransactionLogGateway {
    private final TransactionLogRepository transactionLogRepository;

    public TransactionLogGatewayJpa(final TransactionLogRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
    }

    @Override
    public TransactionLog save(final TransactionLog log) {
        final var entity = TransactionLogMapper.toEntity(log);
        final var saved = transactionLogRepository.save(entity);
        return TransactionLogMapper.toDomain(saved);
    }

    @Override
    public Page<TransactionLog> findByEntityNameAndEntityId(
        final String entityName,
        final UUID entityId,
        final Pageable pageable
    ) {
        return transactionLogRepository.findByEntityNameAndEntityId(entityName, entityId, pageable)
            .map(TransactionLogMapper::toDomain);
    }

    @Override
    public Page<TransactionLog> findByFilters(
        final String entityName,
        final UUID entityId,
        final OperationType operation,
        final String responsibleUser,
        final Instant startDate,
        final Instant endDate,
        final Pageable pageable
    ) {
        return transactionLogRepository.findByFilters(
            entityName,
            entityId,
            operation,
            responsibleUser,
            startDate,
            endDate,
            pageable
        ).map(TransactionLogMapper::toDomain);
    }
}
