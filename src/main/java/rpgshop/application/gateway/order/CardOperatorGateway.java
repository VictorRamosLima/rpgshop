package rpgshop.application.gateway.order;

import rpgshop.application.command.order.CardOperatorDecision;
import rpgshop.domain.entity.order.OrderPayment;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CardOperatorGateway {
    CardOperatorDecision authorize(
        UUID customerId,
        BigDecimal orderTotal,
        List<OrderPayment> payments
    );
}
