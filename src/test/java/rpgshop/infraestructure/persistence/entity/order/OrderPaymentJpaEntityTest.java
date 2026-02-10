package rpgshop.infraestructure.persistence.entity.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.infraestructure.persistence.entity.coupon.CouponJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CreditCardJpaEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("OrderPaymentJpaEntity")
class OrderPaymentJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final OrderJpaEntity order = OrderJpaEntity.builder().build();
            final CreditCardJpaEntity creditCard = CreditCardJpaEntity.builder().build();
            final CouponJpaEntity coupon = CouponJpaEntity.builder().build();
            final BigDecimal amount = new BigDecimal("150.00");

            final OrderPaymentJpaEntity entity = OrderPaymentJpaEntity.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .order(order)
                .creditCard(creditCard)
                .coupon(coupon)
                .amount(amount)
                .build();

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(order, entity.getOrder());
            assertEquals(creditCard, entity.getCreditCard());
            assertEquals(coupon, entity.getCoupon());
            assertEquals(amount, entity.getAmount());
        }

        @Test
        @DisplayName("should create entity with credit card payment only")
        void shouldCreateEntityWithCreditCardPaymentOnly() {
            final CreditCardJpaEntity creditCard = CreditCardJpaEntity.builder().build();
            final BigDecimal amount = new BigDecimal("200.00");

            final OrderPaymentJpaEntity entity = OrderPaymentJpaEntity.builder()
                .creditCard(creditCard)
                .coupon(null)
                .amount(amount)
                .build();

            assertEquals(creditCard, entity.getCreditCard());
            assertNull(entity.getCoupon());
            assertEquals(amount, entity.getAmount());
        }

        @Test
        @DisplayName("should create entity with coupon payment only")
        void shouldCreateEntityWithCouponPaymentOnly() {
            final CouponJpaEntity coupon = CouponJpaEntity.builder().build();
            final BigDecimal amount = new BigDecimal("50.00");

            final OrderPaymentJpaEntity entity = OrderPaymentJpaEntity.builder()
                .creditCard(null)
                .coupon(coupon)
                .amount(amount)
                .build();

            assertNull(entity.getCreditCard());
            assertEquals(coupon, entity.getCoupon());
            assertEquals(amount, entity.getAmount());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final OrderPaymentJpaEntity entity = OrderPaymentJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getOrder());
            assertNull(entity.getCreditCard());
            assertNull(entity.getCoupon());
            assertNull(entity.getAmount());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final OrderPaymentJpaEntity entity = new OrderPaymentJpaEntity();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getOrder());
            assertNull(entity.getCreditCard());
            assertNull(entity.getCoupon());
            assertNull(entity.getAmount());
        }
    }

    @Nested
    @DisplayName("AllArgsConstructor")
    class AllArgsConstructorTests {
        @Test
        @DisplayName("should create entity with all args")
        void shouldCreateEntityWithAllArgs() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final OrderJpaEntity order = OrderJpaEntity.builder().build();
            final CreditCardJpaEntity creditCard = CreditCardJpaEntity.builder().build();
            final CouponJpaEntity coupon = CouponJpaEntity.builder().build();
            final BigDecimal amount = new BigDecimal("75.50");

            final OrderPaymentJpaEntity entity = new OrderPaymentJpaEntity(
                id, createdAt, updatedAt, order, creditCard, coupon, amount
            );

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(order, entity.getOrder());
            assertEquals(creditCard, entity.getCreditCard());
            assertEquals(coupon, entity.getCoupon());
            assertEquals(amount, entity.getAmount());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final OrderPaymentJpaEntity entity = new OrderPaymentJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final OrderJpaEntity order = OrderJpaEntity.builder().build();
            final CreditCardJpaEntity creditCard = CreditCardJpaEntity.builder().build();
            final CouponJpaEntity coupon = CouponJpaEntity.builder().build();
            final BigDecimal amount = new BigDecimal("99.99");

            entity.setId(id);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setOrder(order);
            entity.setCreditCard(creditCard);
            entity.setCoupon(coupon);
            entity.setAmount(amount);

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(order, entity.getOrder());
            assertEquals(creditCard, entity.getCreditCard());
            assertEquals(coupon, entity.getCoupon());
            assertEquals(amount, entity.getAmount());
        }
    }
}
