package rpgshop.application.usecase.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.exchange.ExchangeRequestGateway;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DenyExchangeUseCaseTest {
    @Mock
    private ExchangeRequestGateway exchangeRequestGateway;

    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private DenyExchangeUseCase useCase;

    @Test
    void shouldDenyRequestedExchangeAndRestoreOrderToDelivered() {
        final UUID orderId = UUID.randomUUID();
        final UUID exchangeId = UUID.randomUUID();
        final Order order = Order.builder().id(orderId).status(OrderStatus.IN_EXCHANGE).build();
        final ExchangeRequest request = ExchangeRequest.builder()
            .id(exchangeId)
            .order(order)
            .status(ExchangeStatus.REQUESTED)
            .build();

        when(exchangeRequestGateway.findById(exchangeId)).thenReturn(Optional.of(request));
        when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(exchangeRequestGateway.save(any(ExchangeRequest.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        final ExchangeRequest denied = useCase.execute(exchangeId);

        assertEquals(ExchangeStatus.DENIED, denied.status());
    }

    @Test
    void shouldThrowWhenExchangeIsNotInRequestedStatus() {
        final UUID exchangeId = UUID.randomUUID();
        final ExchangeRequest request = ExchangeRequest.builder()
            .id(exchangeId)
            .order(Order.builder().id(UUID.randomUUID()).build())
            .status(ExchangeStatus.AUTHORIZED)
            .build();
        when(exchangeRequestGateway.findById(exchangeId)).thenReturn(Optional.of(request));

        assertThrows(BusinessRuleException.class, () -> useCase.execute(exchangeId));
    }
}
