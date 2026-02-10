package rpgshop.application.command.coupon;

import rpgshop.domain.entity.coupon.constant.CouponType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CreateCouponCommand(
    String code,
    CouponType type,
    BigDecimal value,
    UUID customerId,
    Instant expiresAt
) {}
