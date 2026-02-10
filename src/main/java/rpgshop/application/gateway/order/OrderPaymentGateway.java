package rpgshop.application.gateway.order;

import rpgshop.domain.entity.order.OrderPayment;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface OrderPaymentGateway {
    OrderPayment save(final OrderPayment payment, final UUID orderId);
    List<OrderPayment> findByOrderId(final UUID orderId);
    BigDecimal sumAmountByOrderId(final UUID orderId);
    boolean existsPromotionalCouponByOrderId(final UUID orderId);
}
