package rpgshop.infraestructure.persistence.repository.order;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import rpgshop.domain.entity.order.constant.OrderStatus;
import rpgshop.infraestructure.persistence.entity.order.OrderJpaEntity;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = OrderJpaEntity.class, idClass = UUID.class)
public interface OrderRepository {
    @Nonnull
    OrderJpaEntity save(@Nonnull final OrderJpaEntity entity);

    @Nonnull
    Optional<OrderJpaEntity> findById(@Nonnull final UUID id);

    @Nonnull
    Page<OrderJpaEntity> findAll(@Nonnull final Pageable pageable);

    @Nonnull
    Page<OrderJpaEntity> findByCustomerId(final UUID customerId, final Pageable pageable);

    @Nonnull
    Page<OrderJpaEntity> findByStatus(
        @Nonnull final OrderStatus status,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    @Query("""
        SELECT o FROM OrderJpaEntity o
        WHERE o.status <> rpgshop.domain.entity.order.constant.OrderStatus.REJECTED
            AND o.purchasedAt >= :startDate
            AND o.purchasedAt <= :endDate
        """)
    Page<OrderJpaEntity> findSalesInPeriod(
        @Param("startDate") @Nonnull final Instant startDate,
        @Param("endDate") @Nonnull final Instant endDate,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    @Query("""
        SELECT o FROM OrderJpaEntity o
        WHERE o.customer.id = :customerId
            AND o.purchasedAt >= :startDate
            AND o.purchasedAt <= :endDate
        """)
    Page<OrderJpaEntity> findByCustomerIdAndPurchasedAtBetween(
        @Param("customerId") @Nonnull final UUID customerId,
        @Param("startDate") @Nonnull final Instant startDate,
        @Param("endDate") @Nonnull final Instant endDate,
        @Nonnull final Pageable pageable
    );
}
