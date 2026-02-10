package rpgshop.application.usecase.analysis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rpgshop.application.command.analysis.SalesAnalysisFilter;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.application.gateway.order.OrderItemGateway;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalesAnalysisUseCaseTest {
    @Mock
    private OrderGateway orderGateway;

    @Mock
    private OrderItemGateway orderItemGateway;

    @InjectMocks
    private SalesAnalysisUseCase useCase;

    @Test
    void shouldFindSalesInPeriod() {
        final Instant start = Instant.now().minusSeconds(86400);
        final Instant end = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);
        final Order order = Order.builder().id(UUID.randomUUID()).build();
        final Page<Order> expected = new PageImpl<>(List.of(order));

        when(orderGateway.findSalesInPeriod(start, end, pageable)).thenReturn(expected);

        final Page<Order> result = useCase.findSalesInPeriod(start, end, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldFindByProductAndCategoryAndSumQuantity() {
        final SalesAnalysisFilter filter = new SalesAnalysisFilter(
            UUID.randomUUID(),
            UUID.randomUUID(),
            Instant.now().minusSeconds(86400),
            Instant.now()
        );
        final Pageable pageable = PageRequest.of(0, 10);
        final OrderItem orderItem = OrderItem.builder().id(UUID.randomUUID()).build();
        final Page<OrderItem> page = new PageImpl<>(List.of(orderItem));

        when(
            orderItemGateway.findByProductIdAndPeriod(
                filter.productId(),
                filter.startDate(),
                filter.endDate(),
                pageable
            )
        ).thenReturn(page);
        when(
            orderItemGateway.findByCategoryIdAndPeriod(
                filter.categoryId(),
                filter.startDate(),
                filter.endDate(),
                pageable
            )
        ).thenReturn(page);
        when(
            orderItemGateway.sumQuantitySoldByProductIdAndPeriod(
                filter.productId(),
                filter.startDate(),
                filter.endDate()
            )
        ).thenReturn(15L);

        final Page<OrderItem> byProduct = useCase.findByProductAndPeriod(filter, pageable);
        final Page<OrderItem> byCategory = useCase.findByCategoryAndPeriod(filter, pageable);
        final long sold = useCase.sumQuantitySoldByProductInPeriod(filter);

        assertEquals(1, byProduct.getTotalElements());
        assertEquals(1, byCategory.getTotalElements());
        assertEquals(15L, sold);
    }
}
