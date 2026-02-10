package rpgshop.application.usecase.order;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.util.UUID;

@Service
public class RejectOrderUseCase {
    private final OrderGateway orderGateway;

    public RejectOrderUseCase(final OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Nonnull
    @Transactional
    public Order execute(@Nonnull final UUID orderId) {
        final Order order = orderGateway.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order", orderId));

        if (order.status() != OrderStatus.PROCESSING) {
            throw new BusinessRuleException("Only orders with status PROCESSING can be rejected");
        }

        final Order rejected = order.toBuilder()
            .status(OrderStatus.REJECTED)
            .build();

        return orderGateway.save(rejected);
    }
}
