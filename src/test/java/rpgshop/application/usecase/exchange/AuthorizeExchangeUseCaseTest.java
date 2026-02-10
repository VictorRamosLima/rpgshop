package rpgshop.application.usecase.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.gateway.exchange.ExchangeNotificationGateway;
import rpgshop.application.gateway.exchange.ExchangeRequestGateway;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizeExchangeUseCaseTest {
    @Mock
    private ExchangeRequestGateway exchangeRequestGateway;

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private ExchangeNotificationGateway exchangeNotificationGateway;

    @InjectMocks
    private AuthorizeExchangeUseCase useCase;

    @Test
    void shouldNotifyCustomerWhenExchangeIsAuthorized() {
        final UUID customerId = UUID.randomUUID();
        final UUID orderId = UUID.randomUUID();
        final UUID exchangeId = UUID.randomUUID();

        final Customer customer = Customer.builder().id(customerId).name("Cliente").build();
        final Order order = Order.builder().id(orderId).customer(customer).status(OrderStatus.IN_EXCHANGE).build();
        final ExchangeRequest request = ExchangeRequest.builder()
            .id(exchangeId)
            .order(order)
            .quantity(1)
            .status(ExchangeStatus.REQUESTED)
            .reason("Defeito")
            .build();

        when(exchangeRequestGateway.findById(exchangeId)).thenReturn(Optional.of(request));
        when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(exchangeRequestGateway.save(any(ExchangeRequest.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        final ExchangeRequest authorized = useCase.execute(exchangeId);

        assertEquals(ExchangeStatus.AUTHORIZED, authorized.status());
        verify(exchangeNotificationGateway).notifyExchangeAuthorized(customerId, orderId, exchangeId);
    }
}
