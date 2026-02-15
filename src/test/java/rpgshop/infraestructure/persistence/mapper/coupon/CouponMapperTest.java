package rpgshop.infraestructure.persistence.mapper.coupon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.infraestructure.persistence.entity.coupon.CouponJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CouponMapper")
class CouponMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final String code = "DISCOUNT10";
            final CouponType type = CouponType.PROMOTIONAL;
            final BigDecimal value = new BigDecimal("10.00");
            final boolean isUsed = false;
            final Instant expiresAt = Instant.now().plusSeconds(86400);
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final CustomerJpaEntity customerEntity = createCustomerEntity();

            final CouponJpaEntity entity = CouponJpaEntity.builder()
                .id(id)
                .code(code)
                .type(type)
                .value(value)
                .customer(customerEntity)
                .isUsed(isUsed)
                .usedAt(null)
                .expiresAt(expiresAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final Coupon domain = CouponMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertEquals(code, domain.code());
            assertEquals(type, domain.type());
            assertEquals(value, domain.value());
            assertNotNull(domain.customer());
            assertEquals(customerEntity.getName(), domain.customer().name());
            assertEquals(isUsed, domain.isUsed());
            assertNull(domain.usedAt());
            assertEquals(expiresAt, domain.expiresAt());
            assertEquals(createdAt, domain.createdAt());
            assertEquals(updatedAt, domain.updatedAt());
        }

        @Test
        @DisplayName("should map entity without customer to domain with null customer")
        void shouldMapEntityWithoutCustomerToDomainWithNullCustomer() {
            final CouponJpaEntity entity = CouponJpaEntity.builder()
                .id(UUID.randomUUID())
                .code("GENERAL20")
                .type(CouponType.PROMOTIONAL)
                .value(new BigDecimal("20.00"))
                .customer(null)
                .isUsed(false)
                .expiresAt(Instant.now().plusSeconds(86400))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final Coupon domain = CouponMapper.toDomain(entity);

            assertNull(domain.customer());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> CouponMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final String code = "SAVE15";
            final CouponType type = CouponType.PROMOTIONAL;
            final BigDecimal value = new BigDecimal("15.00");
            final boolean isUsed = true;
            final Instant usedAt = Instant.now();
            final Instant expiresAt = Instant.now().plusSeconds(86400);
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final Customer customer = createCustomerDomain();

            final Coupon domain = Coupon.builder()
                .id(id)
                .code(code)
                .type(type)
                .value(value)
                .customer(customer)
                .isUsed(isUsed)
                .usedAt(usedAt)
                .expiresAt(expiresAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final CouponJpaEntity entity = CouponMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertEquals(code, entity.getCode());
            assertEquals(type, entity.getType());
            assertEquals(value, entity.getValue());
            assertNotNull(entity.getCustomer());
            assertEquals(customer.name(), entity.getCustomer().getName());
            assertEquals(isUsed, entity.isUsed());
            assertEquals(usedAt, entity.getUsedAt());
            assertEquals(expiresAt, entity.getExpiresAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
        }

        @Test
        @DisplayName("should map domain without customer to entity with null customer")
        void shouldMapDomainWithoutCustomerToEntityWithNullCustomer() {
            final Coupon domain = Coupon.builder()
                .id(UUID.randomUUID())
                .code("GENERAL25")
                .type(CouponType.PROMOTIONAL)
                .value(new BigDecimal("25.00"))
                .customer(null)
                .isUsed(false)
                .expiresAt(Instant.now().plusSeconds(86400))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final CouponJpaEntity entity = CouponMapper.toEntity(domain);

            assertNull(entity.getCustomer());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> CouponMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = CouponMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            final var exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
            assertInstanceOf(IllegalInstantiationException.class, exception.getCause());
        }
    }

    private CustomerJpaEntity createCustomerEntity() {
        return CustomerJpaEntity.builder()
            .id(UUID.randomUUID())
            .gender(Gender.MALE)
            .name("Test Customer")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .cpf("12345678901")
            .email("test@example.com")

            .ranking(BigDecimal.ZERO)
            .customerCode("CUST001")
            .phones(Collections.emptyList())
            .addresses(Collections.emptyList())
            .creditCards(Collections.emptyList())
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private Customer createCustomerDomain() {
        return Customer.builder()
            .id(UUID.randomUUID())
            .gender(Gender.MALE)
            .name("Test Customer")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .cpf("12345678901")
            .email("test@example.com")

            .ranking(BigDecimal.ZERO)
            .customerCode("CUST001")
            .phones(Collections.emptyList())
            .addresses(Collections.emptyList())
            .creditCards(Collections.emptyList())
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }
}
