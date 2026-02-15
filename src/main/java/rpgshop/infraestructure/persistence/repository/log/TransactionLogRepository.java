package rpgshop.infraestructure.persistence.repository.log;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import rpgshop.domain.entity.log.constant.OperationType;
import rpgshop.infraestructure.persistence.entity.log.TransactionLogJpaEntity;

import java.time.Instant;
import java.util.UUID;

@RepositoryDefinition(domainClass = TransactionLogJpaEntity.class, idClass = UUID.class)
public interface TransactionLogRepository {
    @Nonnull
    TransactionLogJpaEntity save(@Nonnull final TransactionLogJpaEntity entity);

    @Nonnull
    Page<TransactionLogJpaEntity> findByEntityNameAndEntityId(
        @Nonnull final String entityName,
        @Nonnull final UUID entityId,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    @Query("""
        SELECT tl FROM TransactionLogJpaEntity tl
        WHERE (:entityName IS NULL OR tl.entityName = :entityName)
            AND (:entityId IS NULL OR tl.entityId = :entityId)
            AND (:operation IS NULL OR tl.operation = :operation)
            AND (:userId IS NULL OR tl.user.id = :userId)
            AND tl.timestamp >= COALESCE(:startDate, tl.timestamp)
            AND tl.timestamp <= COALESCE(:endDate, tl.timestamp)
        """)
    Page<TransactionLogJpaEntity> findByFilters(
        @Param("entityName") @Nullable final String entityName,
        @Param("entityId") @Nullable final UUID entityId,
        @Param("operation") @Nullable final OperationType operation,
        @Param("userId") @Nullable final UUID userId,
        @Param("startDate") @Nullable final Instant startDate,
        @Param("endDate") @Nullable final Instant endDate,
        @Nonnull final Pageable pageable
    );
}
