package rpgshop.application.usecase.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.exchange.RequestExchangeCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.exchange.ExchangeRequestGateway;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.application.gateway.order.OrderItemGateway;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static rpgshop.domain.entity.exchange.constant.ExchangeStatus.COMPLETED;
import static rpgshop.domain.entity.order.constant.OrderStatus.APPROVED;

@ExtendWith(MockitoExtension.class)
class RequestExchangeUseCaseTest {
    @Mock
    private ExchangeRequestGateway exchangeRequestGateway;

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private OrderItemGateway orderItemGateway;

    @InjectMocks
    private RequestExchangeUseCase useCase;

    @Test
    void shouldCreateExchangeRequestAndUpdateOrderStatus() {
        final UUID orderId = UUID.randomUUID();
        final UUID orderItemId = UUID.randomUUID();
        final RequestExchangeCommand command = new RequestExchangeCommand(orderId, orderItemId, 1, "Defeito");

        final Order order = Order.builder().id(orderId).status(OrderStatus.DELIVERED).build();
        final OrderItem orderItem = OrderItem.builder()
            .id(orderItemId)
            .quantity(2)
            .unitPrice(new BigDecimal("100.00"))
            .build();

        when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));
        when(orderItemGateway.findById(orderItemId)).thenReturn(Optional.of(orderItem));
        when(exchangeRequestGateway.existsByOrderItemIdAndStatusNot(orderItemId, COMPLETED)).thenReturn(false);
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(exchangeRequestGateway.save(any(ExchangeRequest.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        final ExchangeRequest saved = useCase.execute(command);

        assertEquals(ExchangeStatus.REQUESTED, saved.status());
        assertEquals(1, saved.quantity());
    }

    @Test
    void shouldThrowWhenOrderIsNotDelivered() {
        final UUID orderId = UUID.randomUUID();
        final RequestExchangeCommand command = new RequestExchangeCommand(orderId, UUID.randomUUID(), 1, "Defeito");
        final Order order = Order.builder().id(orderId).status(APPROVED).build();

        when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }
}
