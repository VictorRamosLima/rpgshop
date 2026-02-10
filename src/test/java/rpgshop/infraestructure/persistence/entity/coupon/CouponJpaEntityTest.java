package rpgshop.infraestructure.persistence.entity.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CouponJpaEntity")
class CouponJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final String code = "PROMO2024";
            final CouponType type = CouponType.PROMOTIONAL;
            final BigDecimal value = new BigDecimal("25.00");
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final boolean isUsed = true;
            final Instant usedAt = Instant.now();
            final Instant expiresAt = Instant.now().plusSeconds(86400);

            final CouponJpaEntity entity = CouponJpaEntity.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .code(code)
                .type(type)
                .value(value)
                .customer(customer)
                .isUsed(isUsed)
                .usedAt(usedAt)
                .expiresAt(expiresAt)
                .build();

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(code, entity.getCode());
            assertEquals(type, entity.getType());
            assertEquals(value, entity.getValue());
            assertEquals(customer, entity.getCustomer());
            assertTrue(entity.isUsed());
            assertEquals(usedAt, entity.getUsedAt());
            assertEquals(expiresAt, entity.getExpiresAt());
        }

        @Test
        @DisplayName("should create entity with EXCHANGE coupon type")
        void shouldCreateEntityWithExchangeCouponType() {
            final CouponJpaEntity entity = CouponJpaEntity.builder()
                .type(CouponType.EXCHANGE)
                .build();

            assertEquals(CouponType.EXCHANGE, entity.getType());
        }

        @Test
        @DisplayName("should create entity with default isUsed as false")
        void shouldCreateEntityWithDefaultIsUsedAsFalse() {
            final CouponJpaEntity entity = CouponJpaEntity.builder().build();
            assertFalse(entity.isUsed());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final CouponJpaEntity entity = CouponJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getCode());
            assertNull(entity.getType());
            assertNull(entity.getValue());
            assertNull(entity.getCustomer());
            assertNull(entity.getUsedAt());
            assertNull(entity.getExpiresAt());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final CouponJpaEntity entity = new CouponJpaEntity();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getCode());
            assertNull(entity.getType());
            assertNull(entity.getValue());
            assertNull(entity.getCustomer());
            assertFalse(entity.isUsed());
            assertNull(entity.getUsedAt());
            assertNull(entity.getExpiresAt());
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
            final String code = "EXCHANGE50";
            final CouponType type = CouponType.EXCHANGE;
            final BigDecimal value = new BigDecimal("50.00");
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final boolean isUsed = false;
            final Instant expiresAt = Instant.now().plusSeconds(172800);

            final CouponJpaEntity entity = new CouponJpaEntity(
                id,
                createdAt,
                updatedAt,
                code,
                type,
                value,
                customer,
                isUsed,
                null,
                expiresAt
            );

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(code, entity.getCode());
            assertEquals(type, entity.getType());
            assertEquals(value, entity.getValue());
            assertEquals(customer, entity.getCustomer());
            assertFalse(entity.isUsed());
            assertNull(entity.getUsedAt());
            assertEquals(expiresAt, entity.getExpiresAt());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {

        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final CouponJpaEntity entity = new CouponJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final String code = "DISCOUNT20";
            final CouponType type = CouponType.PROMOTIONAL;
            final BigDecimal value = new BigDecimal("20.00");
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final Instant usedAt = Instant.now();
            final Instant expiresAt = Instant.now().plusSeconds(3600);

            entity.setId(id);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setCode(code);
            entity.setType(type);
            entity.setValue(value);
            entity.setCustomer(customer);
            entity.setUsed(true);
            entity.setUsedAt(usedAt);
            entity.setExpiresAt(expiresAt);

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(code, entity.getCode());
            assertEquals(type, entity.getType());
            assertEquals(value, entity.getValue());
            assertEquals(customer, entity.getCustomer());
            assertTrue(entity.isUsed());
            assertEquals(usedAt, entity.getUsedAt());
            assertEquals(expiresAt, entity.getExpiresAt());
        }
    }
}
