package rpgshop.domain.entity.order;

import lombok.Builder;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.customer.CreditCard;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record OrderPayment(
    UUID id,
    Order order,
    CreditCard creditCard,
    Coupon coupon,
    BigDecimal amount,
    Instant createdAt,
    Instant updatedAt
) {}
