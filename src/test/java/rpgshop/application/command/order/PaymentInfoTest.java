package rpgshop.application.command.order;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentInfoTest {
    @Test
    void shouldCreatePaymentInfo() {
        final UUID creditCardId = UUID.randomUUID();
        final UUID couponId = UUID.randomUUID();
        final BigDecimal amount = new BigDecimal("150.00");

        final PaymentInfo paymentInfo = new PaymentInfo(creditCardId, couponId, amount);

        assertEquals(creditCardId, paymentInfo.creditCardId());
        assertEquals(couponId, paymentInfo.couponId());
        assertEquals(amount, paymentInfo.amount());
    }
}

