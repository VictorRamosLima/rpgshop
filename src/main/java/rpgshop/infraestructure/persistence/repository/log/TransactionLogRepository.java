package rpgshop.infraestructure.persistence.repository.log;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import rpgshop.domain.entity.log.constant.OperationType;
import rpgshop.infraestructure.persistence.entity.log.TransactionLogJpaEntity;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = TransactionLogJpaEntity.class, idClass = UUID.class)
public interface TransactionLogRepository {
    @Nonnull
    TransactionLogJpaEntity save(@Nonnull final TransactionLogJpaEntity entity);

    @Nonnull
    Optional<TransactionLogJpaEntity> findById(@Nonnull final UUID id);

    @Nonnull
    Page<TransactionLogJpaEntity> findByEntityNameAndEntityId(
        @Nonnull final String entityName,
        @Nonnull final UUID entityId,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    Page<TransactionLogJpaEntity> findByEntityName(
        @Nonnull final String entityName,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    Page<TransactionLogJpaEntity> findByOperation(
        @Nonnull final OperationType operation,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    Page<TransactionLogJpaEntity> findByResponsibleUser(
        @Nonnull final String responsibleUser,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    Page<TransactionLogJpaEntity> findByTimestampBetween(
        @Nonnull final Instant startDate,
        @Nonnull final Instant endDate,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    @Query("""
        SELECT tl FROM TransactionLogJpaEntity tl
        WHERE (:entityName IS NULL OR tl.entityName = :entityName)
            AND (:entityId IS NULL OR tl.entityId = :entityId)
            AND (:operation IS NULL OR tl.operation = :operation)
            AND (:responsibleUser IS NULL OR tl.responsibleUser = :responsibleUser)
            AND (:startDate IS NULL OR tl.timestamp >= :startDate)
            AND (:endDate IS NULL OR tl.timestamp <= :endDate)
        """)
    Page<TransactionLogJpaEntity> findByFilters(
        @Param("entityName") @Nonnull final String entityName,
        @Param("entityId") @Nonnull final UUID entityId,
        @Param("operation") @Nonnull final OperationType operation,
        @Param("responsibleUser") @Nonnull final String responsibleUser,
        @Param("startDate") @Nonnull final Instant startDate,
        @Param("endDate") @Nonnull final Instant endDate,
        @Nonnull final Pageable pageable
    );
}
