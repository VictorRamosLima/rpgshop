package rpgshop.application.usecase.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.util.Optional;
import java.util.UUID;

@Service
public class QueryOrdersUseCase {
    private final OrderGateway orderGateway;

    public QueryOrdersUseCase(final OrderGateway orderGateway) {
        this.orderGateway = orderGateway;
    }

    @Transactional(readOnly = true)
    public Optional<Order> findById(final UUID id) {
        return orderGateway.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Order> findAll(final Pageable pageable) {
        return orderGateway.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findByCustomerId(final UUID customerId, final Pageable pageable) {
        return orderGateway.findByCustomerId(customerId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findByStatus(final OrderStatus status, final Pageable pageable) {
        return orderGateway.findByStatus(status, pageable);
    }
}
