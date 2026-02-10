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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RejectOrderUseCaseTest {
    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private RejectOrderUseCase useCase;

    @Test
    void shouldRejectOrderWhenStatusIsProcessing() {
        final UUID orderId = UUID.randomUUID();
        final Order processing = Order.builder().id(orderId).status(OrderStatus.PROCESSING).build();

        when(orderGateway.findById(orderId)).thenReturn(Optional.of(processing));
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Order rejected = useCase.execute(orderId);

        assertEquals(OrderStatus.REJECTED, rejected.status());
    }

    @Test
    void shouldThrowWhenOrderIsNotInProcessingStatus() {
        final UUID orderId = UUID.randomUUID();
        final Order approved = Order.builder().id(orderId).status(OrderStatus.APPROVED).build();
        when(orderGateway.findById(orderId)).thenReturn(Optional.of(approved));

        assertThrows(BusinessRuleException.class, () -> useCase.execute(orderId));
    }
}
