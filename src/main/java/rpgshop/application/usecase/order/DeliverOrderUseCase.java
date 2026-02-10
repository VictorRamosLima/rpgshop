package rpgshop.application.usecase.order;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.time.Instant;
import java.util.UUID;

@Service
public class DeliverOrderUseCase {
    private final OrderGateway orderGateway;

    public DeliverOrderUseCase(final OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Nonnull
    @Transactional
    public Order execute(@Nonnull final UUID orderId) {
        final Order order = orderGateway.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order", orderId));

        if (order.status() != OrderStatus.IN_TRANSIT) {
            throw new BusinessRuleException("Somente pedidos com status EM TRANSITO podem ser entregues");
        }

        final Order delivered = order.toBuilder()
            .status(OrderStatus.DELIVERED)
            .deliveredAt(Instant.now())
            .build();

        return orderGateway.save(delivered);
    }
}
