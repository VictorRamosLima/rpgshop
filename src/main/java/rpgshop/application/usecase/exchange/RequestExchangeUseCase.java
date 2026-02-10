package rpgshop.application.usecase.exchange;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.exchange.RequestExchangeCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.exchange.ExchangeRequestGateway;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.application.gateway.order.OrderItemGateway;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.util.UUID;

@Service
public class RequestExchangeUseCase {
    private final ExchangeRequestGateway exchangeRequestGateway;
    private final OrderGateway orderGateway;
    private final OrderItemGateway orderItemGateway;

    public RequestExchangeUseCase(
        final ExchangeRequestGateway exchangeRequestGateway,
        final OrderGateway orderGateway,
        final OrderItemGateway orderItemGateway
    ) {
        this.exchangeRequestGateway = exchangeRequestGateway;
        this.orderGateway = orderGateway;
        this.orderItemGateway = orderItemGateway;
    }

    @Nonnull
    @Transactional
    public ExchangeRequest execute(@Nonnull final RequestExchangeCommand command) {
        if (command.reason() == null || command.reason().isBlank()) {
            throw new BusinessRuleException("Exchange reason is required");
        }
        if (command.quantity() <= 0) {
            throw new BusinessRuleException("Quantity must be greater than zero");
        }

        final Order order = orderGateway.findById(command.orderId())
            .orElseThrow(() -> new EntityNotFoundException("Order", command.orderId()));

        if (order.status() != OrderStatus.DELIVERED) {
            throw new BusinessRuleException("Only delivered orders can have items exchanged");
        }

        final OrderItem orderItem = orderItemGateway.findById(command.orderItemId())
            .orElseThrow(() -> new EntityNotFoundException("OrderItem", command.orderItemId()));

        if (command.quantity() > orderItem.quantity()) {
            throw new BusinessRuleException("Exchange quantity exceeds purchased quantity");
        }

        if (exchangeRequestGateway.existsByOrderItemIdAndStatusNot(
            command.orderItemId(), ExchangeStatus.COMPLETED
        )) {
            throw new BusinessRuleException("An exchange request already exists for this item");
        }

        final ExchangeRequest exchangeRequest = ExchangeRequest.builder()
            .id(UUID.randomUUID())
            .order(order)
            .orderItem(orderItem)
            .quantity(command.quantity())
            .status(ExchangeStatus.REQUESTED)
            .reason(command.reason())
            .build();

        final ExchangeRequest saved = exchangeRequestGateway.save(exchangeRequest);

        final Order updatedOrder = order.toBuilder()
            .status(OrderStatus.IN_EXCHANGE)
            .build();
        orderGateway.save(updatedOrder);

        return saved;
    }
}
