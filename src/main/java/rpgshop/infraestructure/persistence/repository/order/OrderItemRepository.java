package rpgshop.infraestructure.persistence.repository.order;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import rpgshop.infraestructure.persistence.entity.order.OrderItemJpaEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = OrderItemJpaEntity.class, idClass = UUID.class)
public interface OrderItemRepository {
    @Nonnull
    OrderItemJpaEntity save(@Nonnull final OrderItemJpaEntity entity);

    @Nonnull
    Optional<OrderItemJpaEntity> findById(@Nonnull final UUID id);

    void deleteById(@Nonnull final UUID id);

    boolean existsById(@Nonnull final UUID id);

    @Nonnull
    List<OrderItemJpaEntity> findByOrderId(@Nonnull final UUID orderId);

    @Nonnull
    Page<OrderItemJpaEntity> findByOrderId(
        @Nonnull final UUID orderId,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    @Query("""
        SELECT oi FROM OrderItemJpaEntity oi
        JOIN oi.order o
        WHERE o.id = :orderId AND o.status = "DELIVERED"
        """)
    List<OrderItemJpaEntity> findDeliveredItemsByOrderId(@Param("orderId") @Nonnull final UUID orderId);

    @Nonnull
    @Query("""
        SELECT oi FROM OrderItemJpaEntity oi
        JOIN oi.order o
        WHERE oi.product.id = :productId
            AND o.status <> "REJECTED"
            AND o.purchasedAt >= :startDate
            AND o.purchasedAt <= :endDate
        """)
    Page<OrderItemJpaEntity> findByProductIdAndPeriod(
        @Param("productId") @Nonnull final UUID productId,
        @Param("startDate") @Nonnull final Instant startDate,
        @Param("endDate") @Nonnull final Instant endDate,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    @Query("""
        SELECT oi FROM OrderItemJpaEntity oi
        JOIN oi.order o
        JOIN oi.product p
        JOIN p.categories c
        WHERE c.id = :categoryId
            AND o.status <> "REJECTED"
            AND o.purchasedAt >= :startDate
            AND o.purchasedAt <= :endDate
        """)
    Page<OrderItemJpaEntity> findByCategoryIdAndPeriod(
        @Param("categoryId") @Nonnull final UUID categoryId,
        @Param("startDate") @Nonnull final Instant startDate,
        @Param("endDate") @Nonnull final Instant endDate,
        @Nonnull final Pageable pageable
    );

    @Query("""
        SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItemJpaEntity oi
        JOIN oi.order o
        WHERE oi.product.id = :productId
            AND o.status <> "REJECTED"
            AND o.purchasedAt >= :startDate
            AND o.purchasedAt <= :endDate
        """)
    long sumQuantitySoldByProductIdAndPeriod(
        @Param("productId") @Nonnull final UUID productId,
        @Param("startDate") @Nonnull final Instant startDate,
        @Param("endDate") @Nonnull final Instant endDate
    );
}
