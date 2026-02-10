package rpgshop.domain.entity.coupon;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.domain.entity.customer.Customer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class CouponTest {
    @Test
    void shouldCreateCoupon() {
        final UUID id = UUID.randomUUID();
        final String code = "DISCOUNT10";
        final CouponType type = CouponType.PROMOTIONAL;
        final BigDecimal value = new BigDecimal("10.00");
        final Customer customer = Customer.builder().id(UUID.randomUUID()).build();
        final boolean isUsed = false;
        final Instant expiresAt = Instant.now().plusSeconds(86400);
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final Coupon coupon = Coupon.builder()
            .id(id)
            .code(code)
            .type(type)
            .value(value)
            .customer(customer)
            .isUsed(isUsed)
            .usedAt(null)
            .expiresAt(expiresAt)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, coupon.id());
        assertEquals(code, coupon.code());
        assertEquals(type, coupon.type());
        assertEquals(value, coupon.value());
        assertEquals(customer, coupon.customer());
        assertFalse(coupon.isUsed());
        assertNull(coupon.usedAt());
        assertEquals(expiresAt, coupon.expiresAt());
        assertEquals(createdAt, coupon.createdAt());
        assertEquals(updatedAt, coupon.updatedAt());
    }

    @Test
    void shouldCreateCouponWithExchangeType() {
        final Coupon coupon = Coupon.builder()
            .type(CouponType.EXCHANGE)
            .build();

        assertEquals(CouponType.EXCHANGE, coupon.type());
    }

    @Test
    void shouldCreateCouponWithNullValues() {
        final Coupon coupon = Coupon.builder().build();

        assertNull(coupon.id());
        assertNull(coupon.code());
        assertNull(coupon.type());
        assertNull(coupon.value());
        assertNull(coupon.customer());
        assertFalse(coupon.isUsed());
        assertNull(coupon.usedAt());
        assertNull(coupon.expiresAt());
        assertNull(coupon.createdAt());
        assertNull(coupon.updatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final String originalCode = "CODE1";
        final String newCode = "CODE2";

        final Coupon original = Coupon.builder().code(originalCode).build();
        final Coupon modified = original.toBuilder().code(newCode).build();

        assertEquals(originalCode, original.code());
        assertEquals(newCode, modified.code());
    }
}

