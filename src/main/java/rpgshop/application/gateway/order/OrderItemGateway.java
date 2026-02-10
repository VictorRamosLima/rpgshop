package rpgshop.application.gateway.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rpgshop.domain.entity.order.OrderItem;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderItemGateway {
    OrderItem save(final OrderItem orderItem, final UUID orderId);
    Optional<OrderItem> findById(final UUID id);
    List<OrderItem> findByOrderId(final UUID orderId);
    List<OrderItem> findDeliveredItemsByOrderId(final UUID orderId);
    Page<OrderItem> findByProductIdAndPeriod(final UUID productId, final Instant startDate, final Instant endDate, final Pageable pageable);
    Page<OrderItem> findByCategoryIdAndPeriod(final UUID categoryId, final Instant startDate, final Instant endDate, final Pageable pageable);
    long sumQuantitySoldByProductIdAndPeriod(final UUID productId, final Instant startDate, final Instant endDate);
}
