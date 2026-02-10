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
public class DispatchOrderUseCase {
    private final OrderGateway orderGateway;

    public DispatchOrderUseCase(final OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Nonnull
    @Transactional
    public Order execute(@Nonnull final UUID orderId) {
        final Order order = orderGateway.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order", orderId));

        if (order.status() != OrderStatus.APPROVED) {
            throw new BusinessRuleException("Somente pedidos com status APROVADO podem ser despachados");
        }

        final Order dispatched = order.toBuilder()
            .status(OrderStatus.IN_TRANSIT)
            .dispatchedAt(Instant.now())
            .build();

        return orderGateway.save(dispatched);
    }
}
