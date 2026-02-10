package rpgshop.application.usecase.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DispatchOrderUseCaseTest {
    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private DispatchOrderUseCase useCase;

    @Test
    void shouldDispatchOrderWhenStatusIsApproved() {
        final UUID orderId = UUID.randomUUID();
        final Order order = Order.builder().id(orderId).status(OrderStatus.APPROVED).build();

        when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Order dispatched = useCase.execute(orderId);

        assertEquals(OrderStatus.IN_TRANSIT, dispatched.status());
        assertNotNull(dispatched.dispatchedAt());
    }

    @Test
    void shouldThrowWhenOrderIsNotApproved() {
        final UUID orderId = UUID.randomUUID();
        final Order order = Order.builder().id(orderId).status(OrderStatus.PROCESSING).build();
        when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(BusinessRuleException.class, () -> useCase.execute(orderId));
    }
}
