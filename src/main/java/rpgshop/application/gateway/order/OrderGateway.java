package rpgshop.application.gateway.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface OrderGateway {
    Order save(final Order order);
    Optional<Order> findById(final UUID id);
    Page<Order> findAll(final Pageable pageable);
    Page<Order> findByCustomerId(final UUID customerId, final Pageable pageable);
    Page<Order> findByStatus(final OrderStatus status, final Pageable pageable);
    Page<Order> findSalesInPeriod(final Instant startDate, final Instant endDate, final Pageable pageable);
    Page<Order> findByCustomerIdAndPurchasedAtBetween(final UUID customerId, final Instant startDate, final Instant endDate, final Pageable pageable);
}
