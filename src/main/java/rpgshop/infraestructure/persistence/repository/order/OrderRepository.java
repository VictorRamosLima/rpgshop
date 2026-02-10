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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = OrderJpaEntity.class, idClass = UUID.class)
public interface OrderRepository {
    @Nonnull
    OrderJpaEntity save(@Nonnull final OrderJpaEntity entity);

    @Nonnull
    Optional<OrderJpaEntity> findById(@Nonnull final UUID id);

    void deleteById(@Nonnull final UUID id);

    boolean existsById(@Nonnull final UUID id);

    @Nonnull
    Optional<OrderJpaEntity> findByOrderCode(@Nonnull final String orderCode);

    boolean existsByOrderCode(@Nonnull final String orderCode);

    @Nonnull
    Page<OrderJpaEntity> findByCustomerId(final UUID customerId, final Pageable pageable);

    @Nonnull
    Page<OrderJpaEntity> findByCustomerIdAndStatus(
        @Nonnull final UUID customerId,
        @Nonnull final OrderStatus status,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    Page<OrderJpaEntity> findByStatus(
        @Nonnull final OrderStatus status,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    @Query("SELECT o FROM OrderJpaEntity o WHERE o.status IN :statuses")
    Page<OrderJpaEntity> findByStatusIn(
        @Param("statuses") @Nonnull final List<OrderStatus> statuses,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    @Query("""
        SELECT o FROM OrderJpaEntity o
        WHERE o.status <> "REJECTED"
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

    long countByStatus(@Nonnull final OrderStatus status);

    long countByCustomerId(@Nonnull final UUID customerId);
}
