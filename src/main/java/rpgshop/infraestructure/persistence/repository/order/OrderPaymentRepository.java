package rpgshop.infraestructure.persistence.repository.order;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.Query;
import rpgshop.infraestructure.persistence.entity.order.OrderPaymentJpaEntity;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RepositoryDefinition(domainClass = OrderPaymentJpaEntity.class, idClass = UUID.class)
public interface OrderPaymentRepository {
    @Nonnull
    OrderPaymentJpaEntity save(@Nonnull final OrderPaymentJpaEntity entity);

    @Nonnull
    List<OrderPaymentJpaEntity> findByOrderId(@Nonnull final UUID orderId);

    @Nonnull
    @Query("SELECT COALESCE(SUM(op.amount), 0) FROM OrderPaymentJpaEntity op WHERE op.order.id = :orderId")
    BigDecimal sumAmountByOrderId(@Param("orderId") @Nonnull final UUID orderId);

    @Query("""
        SELECT COUNT(op) > 0 FROM OrderPaymentJpaEntity op
        JOIN op.coupon c
        WHERE op.order.id = :orderId AND c.type = "PROMOTIONAL"
        """)
    boolean existsPromotionalCouponByOrderId(@Param("orderId") @Nonnull final UUID orderId);
}
