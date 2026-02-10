package rpgshop.domain.entity.coupon;

import lombok.Builder;
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.domain.entity.customer.Customer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record Coupon(
    UUID id,
    String code,
    CouponType type,
    BigDecimal value,
    Customer customer,
    boolean isUsed,
    Instant usedAt,
    Instant expiresAt,
    Instant createdAt,
    Instant updatedAt
) {}
