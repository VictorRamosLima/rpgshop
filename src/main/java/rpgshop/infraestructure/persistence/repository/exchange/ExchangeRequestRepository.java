package rpgshop.infraestructure.persistence.repository.exchange;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.infraestructure.persistence.entity.exchange.ExchangeRequestJpaEntity;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = ExchangeRequestJpaEntity.class, idClass = UUID.class)
public interface ExchangeRequestRepository {
    @Nonnull
    ExchangeRequestJpaEntity save(@Nonnull final ExchangeRequestJpaEntity entity);

    @Nonnull
    Optional<ExchangeRequestJpaEntity> findById(@Nonnull final UUID id);

    @Nonnull
    Page<ExchangeRequestJpaEntity> findAll(@Nonnull final Pageable pageable);

    @Nonnull
    Page<ExchangeRequestJpaEntity> findByStatus(
        @Nonnull final ExchangeStatus status,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    Page<ExchangeRequestJpaEntity> findByOrderId(
        @Nonnull final UUID orderId,
        @Nonnull final Pageable pageable
    );

    boolean existsByOrderItemIdAndStatusNot(
        @Nonnull final UUID orderItemId,
        @Nonnull final ExchangeStatus status
    );

    @Nonnull
    Page<ExchangeRequestJpaEntity> findByOrderCustomerId(
        @Nonnull final UUID customerId,
        @Nonnull final Pageable pageable
    );
}
