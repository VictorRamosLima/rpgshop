package rpgshop.infraestructure.persistence.gateway.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;
import rpgshop.infraestructure.persistence.mapper.order.OrderMapper;
import rpgshop.infraestructure.persistence.repository.order.OrderRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderGatewayJpa implements OrderGateway {
    private final OrderRepository orderRepository;

    public OrderGatewayJpa(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order save(final Order order) {
        final var entity = OrderMapper.toEntity(order);
        final var saved = orderRepository.save(entity);
        return OrderMapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(final UUID id) {
        return orderRepository.findById(id).map(OrderMapper::toDomain);
    }

    @Override
    public Page<Order> findAll(final Pageable pageable) {
        return orderRepository.findAll(pageable)
            .map(OrderMapper::toDomain);
    }

    @Override
    public Page<Order> findByCustomerId(final UUID customerId, final Pageable pageable) {
        return orderRepository.findByCustomerId(customerId, pageable)
            .map(OrderMapper::toDomain);
    }

    @Override
    public Page<Order> findByStatus(final OrderStatus status, final Pageable pageable) {
        return orderRepository.findByStatus(status, pageable)
            .map(OrderMapper::toDomain);
    }

    @Override
    public Page<Order> findSalesInPeriod(final Instant startDate, final Instant endDate, final Pageable pageable) {
        return orderRepository.findSalesInPeriod(startDate, endDate, pageable)
            .map(OrderMapper::toDomain);
    }

    @Override
    public Page<Order> findByCustomerIdAndPurchasedAtBetween(
        final UUID customerId,
        final Instant startDate,
        final Instant endDate,
        final Pageable pageable
    ) {
        return orderRepository.findByCustomerIdAndPurchasedAtBetween(customerId, startDate, endDate, pageable)
            .map(OrderMapper::toDomain);
    }
}
