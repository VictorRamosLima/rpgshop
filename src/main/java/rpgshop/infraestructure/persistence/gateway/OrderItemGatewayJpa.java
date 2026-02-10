package rpgshop.infraestructure.persistence.gateway;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rpgshop.application.gateway.order.OrderItemGateway;
import rpgshop.domain.entity.order.OrderItem;
import rpgshop.infraestructure.mapper.order.OrderItemMapper;
import rpgshop.infraestructure.persistence.entity.order.OrderItemJpaEntity;
import rpgshop.infraestructure.persistence.repository.order.OrderItemRepository;
import rpgshop.infraestructure.persistence.repository.order.OrderRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderItemGatewayJpa implements OrderItemGateway {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    public OrderItemGatewayJpa(final OrderItemRepository orderItemRepository, final OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderItem save(final OrderItem orderItem, final UUID orderId) {
        final OrderItemJpaEntity entity = OrderItemMapper.toEntity(orderItem);
        orderRepository.findById(orderId).ifPresent(entity::setOrder);
        final OrderItemJpaEntity saved = orderItemRepository.save(entity);
        return OrderItemMapper.toDomain(saved);
    }

    @Override
    public Optional<OrderItem> findById(final UUID id) {
        return orderItemRepository.findById(id).map(OrderItemMapper::toDomain);
    }

    @Override
    public List<OrderItem> findByOrderId(final UUID orderId) {
        return orderItemRepository.findByOrderId(orderId)
            .stream().map(OrderItemMapper::toDomain).toList();
    }

    @Override
    public List<OrderItem> findDeliveredItemsByOrderId(final UUID orderId) {
        return orderItemRepository.findDeliveredItemsByOrderId(orderId)
            .stream().map(OrderItemMapper::toDomain).toList();
    }

    @Override
    public Page<OrderItem> findByProductIdAndPeriod(final UUID productId, final Instant startDate, final Instant endDate, final Pageable pageable) {
        return orderItemRepository.findByProductIdAndPeriod(productId, startDate, endDate, pageable)
            .map(OrderItemMapper::toDomain);
    }

    @Override
    public Page<OrderItem> findByCategoryIdAndPeriod(final UUID categoryId, final Instant startDate, final Instant endDate, final Pageable pageable) {
        return orderItemRepository.findByCategoryIdAndPeriod(categoryId, startDate, endDate, pageable)
            .map(OrderItemMapper::toDomain);
    }

    @Override
    public long sumQuantitySoldByProductIdAndPeriod(final UUID productId, final Instant startDate, final Instant endDate) {
        return orderItemRepository.sumQuantitySoldByProductIdAndPeriod(productId, startDate, endDate);
    }
}
