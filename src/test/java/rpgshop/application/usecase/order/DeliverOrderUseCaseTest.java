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
class DeliverOrderUseCaseTest {
    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private DeliverOrderUseCase useCase;

    @Test
    void shouldDeliverOrderWhenStatusIsInTransit() {
        final UUID orderId = UUID.randomUUID();
        final Order order = Order.builder().id(orderId).status(OrderStatus.IN_TRANSIT).build();

        when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Order delivered = useCase.execute(orderId);

        assertEquals(OrderStatus.DELIVERED, delivered.status());
        assertNotNull(delivered.deliveredAt());
    }

    @Test
    void shouldThrowWhenOrderIsNotInTransit() {
        final UUID orderId = UUID.randomUUID();
        final Order order = Order.builder().id(orderId).status(OrderStatus.APPROVED).build();
        when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));

        assertThrows(BusinessRuleException.class, () -> useCase.execute(orderId));
    }
}
