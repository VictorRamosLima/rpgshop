package rpgshop.infraestructure.persistence.mapper.exchange;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;
import rpgshop.domain.entity.order.constant.OrderStatus;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.infraestructure.persistence.entity.coupon.CouponJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.AddressJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;
import rpgshop.infraestructure.persistence.entity.exchange.ExchangeRequestJpaEntity;
import rpgshop.infraestructure.persistence.entity.order.OrderItemJpaEntity;
import rpgshop.infraestructure.persistence.entity.order.OrderJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.PricingGroupJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductTypeJpaEntity;

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

@DisplayName("ExchangeRequestMapper")
class ExchangeRequestMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final int quantity = 2;
            final ExchangeStatus status = ExchangeStatus.REQUESTED;
            final String reason = "Product arrived damaged";
            final boolean returnToStock = false;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final OrderJpaEntity orderEntity = createOrderEntity();
            final OrderItemJpaEntity orderItemEntity = createOrderItemEntity();

            final ExchangeRequestJpaEntity entity = ExchangeRequestJpaEntity.builder()
                .id(id)
                .order(orderEntity)
                .orderItem(orderItemEntity)
                .quantity(quantity)
                .status(status)
                .reason(reason)
                .authorizedAt(null)
                .receivedAt(null)
                .returnToStock(returnToStock)
                .coupon(null)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final ExchangeRequest domain = ExchangeRequestMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertNotNull(domain.order());
            assertNotNull(domain.orderItem());
            assertEquals(quantity, domain.quantity());
            assertEquals(status, domain.status());
            assertEquals(reason, domain.reason());
            assertNull(domain.authorizedAt());
            assertNull(domain.receivedAt());
            assertEquals(returnToStock, domain.returnToStock());
            assertNull(domain.coupon());
            assertEquals(createdAt, domain.createdAt());
            assertEquals(updatedAt, domain.updatedAt());
        }

        @Test
        @DisplayName("should map entity with coupon to domain")
        void shouldMapEntityWithCouponToDomain() {
            final CouponJpaEntity couponEntity = CouponJpaEntity.builder()
                .id(UUID.randomUUID())
                .code("EXCHANGE50")
                .type(CouponType.EXCHANGE)
                .value(new BigDecimal("50.00"))
                .isUsed(false)
                .expiresAt(Instant.now().plusSeconds(86400))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final ExchangeRequestJpaEntity entity = ExchangeRequestJpaEntity.builder()
                .id(UUID.randomUUID())
                .order(createOrderEntity())
                .orderItem(createOrderItemEntity())
                .quantity(1)
                .status(ExchangeStatus.AUTHORIZED)
                .reason("Customer changed mind")
                .authorizedAt(Instant.now())
                .receivedAt(Instant.now())
                .returnToStock(true)
                .coupon(couponEntity)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final ExchangeRequest domain = ExchangeRequestMapper.toDomain(entity);

            assertNotNull(domain.coupon());
            assertEquals(couponEntity.getCode(), domain.coupon().code());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> ExchangeRequestMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final int quantity = 1;
            final ExchangeStatus status = ExchangeStatus.DENIED;
            final String reason = "Invalid exchange request";
            final boolean returnToStock = false;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final Order order = createOrderDomain();
            final OrderItem orderItem = createOrderItemDomain();

            final ExchangeRequest domain = ExchangeRequest.builder()
                .id(id)
                .order(order)
                .orderItem(orderItem)
                .quantity(quantity)
                .status(status)
                .reason(reason)
                .authorizedAt(null)
                .receivedAt(null)
                .returnToStock(returnToStock)
                .coupon(null)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final ExchangeRequestJpaEntity entity = ExchangeRequestMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertNotNull(entity.getOrder());
            assertNotNull(entity.getOrderItem());
            assertEquals(quantity, entity.getQuantity());
            assertEquals(status, entity.getStatus());
            assertEquals(reason, entity.getReason());
            assertNull(entity.getAuthorizedAt());
            assertNull(entity.getReceivedAt());
            assertEquals(returnToStock, entity.isReturnToStock());
            assertNull(entity.getCoupon());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
        }

        @Test
        @DisplayName("should map domain with coupon to entity")
        void shouldMapDomainWithCouponToEntity() {
            final Coupon coupon = Coupon.builder()
                .id(UUID.randomUUID())
                .code("REFUND100")
                .type(CouponType.EXCHANGE)
                .value(new BigDecimal("100.00"))
                .isUsed(false)
                .expiresAt(Instant.now().plusSeconds(86400))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final ExchangeRequest domain = ExchangeRequest.builder()
                .id(UUID.randomUUID())
                .order(createOrderDomain())
                .orderItem(createOrderItemDomain())
                .quantity(2)
                .status(ExchangeStatus.COMPLETED)
                .reason("Exchange completed")
                .authorizedAt(Instant.now())
                .receivedAt(Instant.now())
                .returnToStock(true)
                .coupon(coupon)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final ExchangeRequestJpaEntity entity = ExchangeRequestMapper.toEntity(domain);

            assertNotNull(entity.getCoupon());
            assertEquals(coupon.code(), entity.getCoupon().getCode());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> ExchangeRequestMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = ExchangeRequestMapper.class.getDeclaredConstructor();
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

    private AddressJpaEntity createAddressEntity() {
        return AddressJpaEntity.builder()
            .id(UUID.randomUUID())
            .purpose(AddressPurpose.DELIVERY)
            .label("Home")
            .residenceType(ResidenceType.HOUSE)
            .streetType(StreetType.STREET)
            .street("Main Street")
            .number("123")
            .neighborhood("Downtown")
            .zipCode("12345678")
            .city("São Paulo")
            .state("SP")
            .country("Brazil")
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private OrderJpaEntity createOrderEntity() {
        return OrderJpaEntity.builder()
            .id(UUID.randomUUID())
            .customer(createCustomerEntity())
            .status(OrderStatus.DELIVERED)
            .deliveryAddress(createAddressEntity())
            .freightCost(new BigDecimal("10.00"))
            .subtotal(new BigDecimal("100.00"))
            .total(new BigDecimal("110.00"))
            .purchasedAt(Instant.now())
            .items(Collections.emptyList())
            .payments(Collections.emptyList())
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private ProductJpaEntity createProductEntity() {
        final ProductTypeJpaEntity productType = ProductTypeJpaEntity.builder()
            .id(UUID.randomUUID())
            .name("Physical")
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

        final PricingGroupJpaEntity pricingGroup = PricingGroupJpaEntity.builder()
            .id(UUID.randomUUID())
            .name("Standard")
            .marginPercentage(new BigDecimal("10.00"))
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

        return ProductJpaEntity.builder()
            .id(UUID.randomUUID())
            .name("Test Product")
            .productType(productType)
            .pricingGroup(pricingGroup)
            .salePrice(new BigDecimal("99.90"))
            .costPrice(new BigDecimal("50.00"))
            .stockQuantity(10)
            .statusChanges(Collections.emptyList())
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private OrderItemJpaEntity createOrderItemEntity() {
        return OrderItemJpaEntity.builder()
            .id(UUID.randomUUID())
            .product(createProductEntity())
            .quantity(2)
            .unitPrice(new BigDecimal("50.00"))
            .totalPrice(new BigDecimal("100.00"))
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

    private Address createAddressDomain() {
        return Address.builder()
            .id(UUID.randomUUID())
            .purpose(AddressPurpose.DELIVERY)
            .label("Home")
            .residenceType(ResidenceType.HOUSE)
            .streetType(StreetType.STREET)
            .street("Main Street")
            .number("123")
            .neighborhood("Downtown")
            .zipCode("12345678")
            .city("São Paulo")
            .state("SP")
            .country("Brazil")
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private Order createOrderDomain() {
        return Order.builder()
            .id(UUID.randomUUID())
            .customer(createCustomerDomain())
            .status(OrderStatus.DELIVERED)
            .deliveryAddress(createAddressDomain())
            .freightCost(new BigDecimal("10.00"))
            .subtotal(new BigDecimal("100.00"))
            .total(new BigDecimal("110.00"))
            .purchasedAt(Instant.now())
            .items(Collections.emptyList())
            .payments(Collections.emptyList())
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private Product createProductDomain() {
        final ProductType productType = ProductType.builder()
            .id(UUID.randomUUID())
            .name("Physical")
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

        final PricingGroup pricingGroup = PricingGroup.builder()
            .id(UUID.randomUUID())
            .name("Standard")
            .marginPercentage(new BigDecimal("10.00"))
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

        return Product.builder()
            .id(UUID.randomUUID())
            .name("Test Product")
            .productType(productType)
            .categories(Collections.emptyList())
            .pricingGroup(pricingGroup)
            .salePrice(new BigDecimal("99.90"))
            .costPrice(new BigDecimal("50.00"))
            .stockQuantity(10)
            .statusChanges(Collections.emptyList())
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private OrderItem createOrderItemDomain() {
        return OrderItem.builder()
            .id(UUID.randomUUID())
            .product(createProductDomain())
            .quantity(2)
            .unitPrice(new BigDecimal("50.00"))
            .totalPrice(new BigDecimal("100.00"))
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }
}
