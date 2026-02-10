package rpgshop.domain.entity.order;

import lombok.Builder;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
public record Order(
    UUID id,
    Customer customer,
    OrderStatus status,
    Address deliveryAddress,
    BigDecimal freightCost,
    BigDecimal subtotal,
    BigDecimal total,
    Instant purchasedAt,
    Instant dispatchedAt,
    Instant deliveredAt,
    List<OrderItem> items,
    List<OrderPayment> payments,
    Instant createdAt,
    Instant updatedAt
) {}
