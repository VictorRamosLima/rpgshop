package rpgshop.domain.entity.exchange;

import lombok.Builder;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record ExchangeRequest(
    UUID id,
    Order order,
    OrderItem orderItem,
    int quantity,
    ExchangeStatus status,
    String reason,
    Instant authorizedAt,
    Instant receivedAt,
    boolean returnToStock,
    Coupon coupon,
    Instant createdAt,
    Instant updatedAt
) {}
