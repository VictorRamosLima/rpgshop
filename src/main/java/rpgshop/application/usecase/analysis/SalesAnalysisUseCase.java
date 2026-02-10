package rpgshop.application.usecase.analysis;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.analysis.SalesAnalysisFilter;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.application.gateway.order.OrderItemGateway;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;

import java.time.Instant;

@Service
public class SalesAnalysisUseCase {
    private final OrderGateway orderGateway;
    private final OrderItemGateway orderItemGateway;

    public SalesAnalysisUseCase(final OrderGateway orderGateway, final OrderItemGateway orderItemGateway) {
        this.orderGateway = orderGateway;
        this.orderItemGateway = orderItemGateway;
    }

    @Transactional(readOnly = true)
    public Page<Order> findSalesInPeriod(final Instant startDate, final Instant endDate, final Pageable pageable) {
        return orderGateway.findSalesInPeriod(startDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<OrderItem> findByProductAndPeriod(final SalesAnalysisFilter filter, final Pageable pageable) {
        return orderItemGateway.findByProductIdAndPeriod(
            filter.productId(), filter.startDate(), filter.endDate(), pageable
        );
    }

    @Transactional(readOnly = true)
    public Page<OrderItem> findByCategoryAndPeriod(final SalesAnalysisFilter filter, final Pageable pageable) {
        return orderItemGateway.findByCategoryIdAndPeriod(
            filter.categoryId(), filter.startDate(), filter.endDate(), pageable
        );
    }

    @Transactional(readOnly = true)
    public long sumQuantitySoldByProductInPeriod(final SalesAnalysisFilter filter) {
        return orderItemGateway.sumQuantitySoldByProductIdAndPeriod(
            filter.productId(), filter.startDate(), filter.endDate()
        );
    }
}
