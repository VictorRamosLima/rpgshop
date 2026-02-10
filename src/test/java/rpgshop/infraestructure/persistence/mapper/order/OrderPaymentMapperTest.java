package rpgshop.infraestructure.persistence.mapper.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.domain.entity.customer.CardBrand;
import rpgshop.domain.entity.customer.CreditCard;
import rpgshop.domain.entity.order.OrderPayment;
import rpgshop.infraestructure.persistence.entity.coupon.CouponJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CardBrandJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CreditCardJpaEntity;
import rpgshop.infraestructure.persistence.entity.order.OrderPaymentJpaEntity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("OrderPaymentMapper")
class OrderPaymentMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with credit card")
        void shouldMapEntityToDomainWithCreditCard() {
            final UUID id = UUID.randomUUID();
            final BigDecimal amount = new BigDecimal("150.00");
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final CreditCardJpaEntity creditCardEntity = createCreditCardEntity();

            final OrderPaymentJpaEntity entity = OrderPaymentJpaEntity.builder()
                .id(id)
                .creditCard(creditCardEntity)
                .coupon(null)
                .amount(amount)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final OrderPayment domain = OrderPaymentMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertNotNull(domain.creditCard());
            assertEquals(creditCardEntity.getCardNumber(), domain.creditCard().cardNumber());
            assertNull(domain.coupon());
            assertEquals(amount, domain.amount());
            assertEquals(createdAt, domain.createdAt());
            assertEquals(updatedAt, domain.updatedAt());
        }

        @Test
        @DisplayName("should map entity to domain with coupon")
        void shouldMapEntityToDomainWithCoupon() {
            final UUID id = UUID.randomUUID();
            final BigDecimal amount = new BigDecimal("50.00");
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final CouponJpaEntity couponEntity = createCouponEntity();

            final OrderPaymentJpaEntity entity = OrderPaymentJpaEntity.builder()
                .id(id)
                .creditCard(null)
                .coupon(couponEntity)
                .amount(amount)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final OrderPayment domain = OrderPaymentMapper.toDomain(entity);

            assertNotNull(domain);
            assertNull(domain.creditCard());
            assertNotNull(domain.coupon());
            assertEquals(couponEntity.getCode(), domain.coupon().code());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> OrderPaymentMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with credit card")
        void shouldMapDomainToEntityWithCreditCard() {
            final UUID id = UUID.randomUUID();
            final BigDecimal amount = new BigDecimal("200.00");
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final CreditCard creditCard = createCreditCardDomain();

            final OrderPayment domain = OrderPayment.builder()
                .id(id)
                .creditCard(creditCard)
                .coupon(null)
                .amount(amount)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final OrderPaymentJpaEntity entity = OrderPaymentMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertNotNull(entity.getCreditCard());
            assertEquals(creditCard.cardNumber(), entity.getCreditCard().getCardNumber());
            assertNull(entity.getCoupon());
            assertEquals(amount, entity.getAmount());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
        }

        @Test
        @DisplayName("should map domain to entity with coupon")
        void shouldMapDomainToEntityWithCoupon() {
            final UUID id = UUID.randomUUID();
            final BigDecimal amount = new BigDecimal("30.00");
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final Coupon coupon = createCouponDomain();

            final OrderPayment domain = OrderPayment.builder()
                .id(id)
                .creditCard(null)
                .coupon(coupon)
                .amount(amount)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final OrderPaymentJpaEntity entity = OrderPaymentMapper.toEntity(domain);

            assertNotNull(entity);
            assertNull(entity.getCreditCard());
            assertNotNull(entity.getCoupon());
            assertEquals(coupon.code(), entity.getCoupon().getCode());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> OrderPaymentMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = OrderPaymentMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            final var exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
            assertInstanceOf(IllegalInstantiationException.class, exception.getCause());
        }
    }

    private CreditCardJpaEntity createCreditCardEntity() {
        final CardBrandJpaEntity cardBrand = CardBrandJpaEntity.builder()
            .id(UUID.randomUUID())
            .name("Visa")
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

        return CreditCardJpaEntity.builder()
            .id(UUID.randomUUID())
            .cardNumber("4111111111111111")
            .printedName("JOHN DOE")
            .cardBrand(cardBrand)
            .securityCode("123")
            .isPreferred(true)
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private CreditCard createCreditCardDomain() {
        final CardBrand cardBrand = CardBrand.builder()
            .id(UUID.randomUUID())
            .name("Visa")
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

        return CreditCard.builder()
            .id(UUID.randomUUID())
            .cardNumber("4111111111111111")
            .printedName("JOHN DOE")
            .cardBrand(cardBrand)
            .securityCode("123")
            .isPreferred(true)
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private CouponJpaEntity createCouponEntity() {
        return CouponJpaEntity.builder()
            .id(UUID.randomUUID())
            .code("DISCOUNT10")
            .type(CouponType.PROMOTIONAL)
            .value(new BigDecimal("10.00"))
            .customer(null)
            .isUsed(false)
            .expiresAt(Instant.now().plusSeconds(86400))
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private Coupon createCouponDomain() {
        return Coupon.builder()
            .id(UUID.randomUUID())
            .code("DISCOUNT10")
            .type(CouponType.PROMOTIONAL)
            .value(new BigDecimal("10.00"))
            .customer(null)
            .isUsed(false)
            .expiresAt(Instant.now().plusSeconds(86400))
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }
}

