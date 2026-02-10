package rpgshop.application.command.order;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentInfo(
    UUID creditCardId,
    UUID couponId,
    BigDecimal amount
) {}
