package rpgshop.application.usecase.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryOrdersUseCaseTest {
    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private QueryOrdersUseCase useCase;

    @Test
    void shouldFindOrderById() {
        final UUID orderId = UUID.randomUUID();
        final Order order = Order.builder().id(orderId).build();
        when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));

        final Optional<Order> result = useCase.findById(orderId);

        assertTrue(result.isPresent());
        assertEquals(orderId, result.orElseThrow().id());
    }

    @Test
    void shouldFindOrdersByDifferentCriteria() {
        final Pageable pageable = PageRequest.of(0, 10);
        final UUID customerId = UUID.randomUUID();
        final Page<Order> page = new PageImpl<>(List.of(Order.builder().id(UUID.randomUUID()).build()));

        when(orderGateway.findAll(pageable)).thenReturn(page);
        when(orderGateway.findByCustomerId(customerId, pageable)).thenReturn(page);
        when(orderGateway.findByStatus(OrderStatus.APPROVED, pageable)).thenReturn(page);

        assertEquals(1, useCase.findAll(pageable).getTotalElements());
        assertEquals(1, useCase.findByCustomerId(customerId, pageable).getTotalElements());
        assertEquals(1, useCase.findByStatus(OrderStatus.APPROVED, pageable).getTotalElements());
    }
}
