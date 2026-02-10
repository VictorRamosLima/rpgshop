package rpgshop.application.command.coupon;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.coupon.constant.CouponType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateCouponCommandTest {
    @Test
    void shouldCreateCreateCouponCommand() {
        final String code = "DISCOUNT10";
        final CouponType type = CouponType.PROMOTIONAL;
        final BigDecimal value = new BigDecimal("10.00");
        final UUID customerId = UUID.randomUUID();
        final Instant expiresAt = Instant.now().plusSeconds(86400);

        final CreateCouponCommand command = new CreateCouponCommand(
            code,
            type,
            value,
            customerId,
            expiresAt
        );

        assertEquals(code, command.code());
        assertEquals(type, command.type());
        assertEquals(value, command.value());
        assertEquals(customerId, command.customerId());
        assertEquals(expiresAt, command.expiresAt());
    }
}

