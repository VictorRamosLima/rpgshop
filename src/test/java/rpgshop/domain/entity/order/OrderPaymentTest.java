package rpgshop.domain.entity.order;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.customer.CreditCard;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderPaymentTest {
    @Test
    void shouldCreateOrderPayment() {
        final UUID id = UUID.randomUUID();
        final Order order = Order.builder().id(UUID.randomUUID()).build();
        final CreditCard creditCard = CreditCard.builder().cardNumber("4111111111111111").build();
        final BigDecimal amount = new BigDecimal("100.00");
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final OrderPayment orderPayment = OrderPayment.builder()
            .id(id)
            .order(order)
            .creditCard(creditCard)
            .coupon(null)
            .amount(amount)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, orderPayment.id());
        assertEquals(order, orderPayment.order());
        assertEquals(creditCard, orderPayment.creditCard());
        assertNull(orderPayment.coupon());
        assertEquals(amount, orderPayment.amount());
        assertEquals(createdAt, orderPayment.createdAt());
        assertEquals(updatedAt, orderPayment.updatedAt());
    }

    @Test
    void shouldCreateOrderPaymentWithCoupon() {
        final Coupon coupon = Coupon.builder().code("DISCOUNT10").build();
        final BigDecimal amount = new BigDecimal("50.00");

        final OrderPayment orderPayment = OrderPayment.builder()
            .coupon(coupon)
            .amount(amount)
            .build();

        assertEquals(coupon, orderPayment.coupon());
        assertEquals(amount, orderPayment.amount());
    }

    @Test
    void shouldCreateOrderPaymentWithNullValues() {
        final OrderPayment orderPayment = OrderPayment.builder().build();

        assertNull(orderPayment.id());
        assertNull(orderPayment.order());
        assertNull(orderPayment.creditCard());
        assertNull(orderPayment.coupon());
        assertNull(orderPayment.amount());
        assertNull(orderPayment.createdAt());
        assertNull(orderPayment.updatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final BigDecimal originalAmount = new BigDecimal("100.00");
        final BigDecimal newAmount = new BigDecimal("200.00");

        final OrderPayment original = OrderPayment.builder().amount(originalAmount).build();
        final OrderPayment modified = original.toBuilder().amount(newAmount).build();

        assertEquals(originalAmount, original.amount());
        assertEquals(newAmount, modified.amount());
    }
}

