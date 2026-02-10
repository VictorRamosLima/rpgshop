package rpgshop.application.usecase.exchange;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.exchange.ExchangeRequestGateway;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.util.UUID;

@Service
public class DenyExchangeUseCase {
    private final ExchangeRequestGateway exchangeRequestGateway;
    private final OrderGateway orderGateway;

    public DenyExchangeUseCase(
        final ExchangeRequestGateway exchangeRequestGateway,
        final OrderGateway orderGateway
    ) {
        this.exchangeRequestGateway = exchangeRequestGateway;
        this.orderGateway = orderGateway;
    }

    @Nonnull
    @Transactional
    public ExchangeRequest execute(@Nonnull final UUID exchangeRequestId) {
        final ExchangeRequest request = exchangeRequestGateway.findById(exchangeRequestId)
            .orElseThrow(() -> new EntityNotFoundException("ExchangeRequest", exchangeRequestId));

        if (request.status() != ExchangeStatus.REQUESTED) {
            throw new BusinessRuleException("Somente solicitacoes com status SOLICITADO podem ser negadas");
        }

        final ExchangeRequest denied = request.toBuilder()
            .status(ExchangeStatus.DENIED)
            .build();

        final ExchangeRequest saved = exchangeRequestGateway.save(denied);

        final Order order = orderGateway.findById(request.order().id())
            .orElseThrow(() -> new EntityNotFoundException("Order", request.order().id()));

        final Order updatedOrder = order.toBuilder()
            .status(OrderStatus.DELIVERED)
            .build();
        orderGateway.save(updatedOrder);

        return saved;
    }
}
