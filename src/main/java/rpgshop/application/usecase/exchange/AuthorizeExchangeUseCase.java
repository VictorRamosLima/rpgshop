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

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthorizeExchangeUseCase {
    private final ExchangeRequestGateway exchangeRequestGateway;
    private final OrderGateway orderGateway;

    public AuthorizeExchangeUseCase(
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
            throw new BusinessRuleException("Somente solicitacoes com status SOLICITADO podem ser autorizadas");
        }

        final ExchangeRequest authorized = request.toBuilder()
            .status(ExchangeStatus.AUTHORIZED)
            .authorizedAt(Instant.now())
            .build();

        final ExchangeRequest saved = exchangeRequestGateway.save(authorized);

        final Order order = orderGateway.findById(request.order().id())
            .orElseThrow(() -> new EntityNotFoundException("Order", request.order().id()));

        final Order updatedOrder = order.toBuilder()
            .status(OrderStatus.EXCHANGE_AUTHORIZED)
            .build();
        orderGateway.save(updatedOrder);

        return saved;
    }
}
