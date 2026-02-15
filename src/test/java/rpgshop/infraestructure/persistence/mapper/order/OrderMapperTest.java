package rpgshop.infraestructure.persistence.mapper.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;
import rpgshop.domain.entity.order.constant.OrderStatus;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.infraestructure.persistence.entity.customer.AddressJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("OrderMapper")
class OrderMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final OrderStatus status = OrderStatus.PROCESSING;
            final BigDecimal freightCost = new BigDecimal("15.00");
            final BigDecimal subtotal = new BigDecimal("200.00");
            final BigDecimal total = new BigDecimal("215.00");
            final Instant purchasedAt = Instant.now();
            final Instant dispatchedAt = null;
            final Instant deliveredAt = null;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final CustomerJpaEntity customerEntity = createCustomerEntity();
            final AddressJpaEntity addressEntity = createAddressEntity();

            final OrderJpaEntity entity = OrderJpaEntity.builder()
                .id(id)
                .customer(customerEntity)
                .status(status)
                .deliveryAddress(addressEntity)
                .freightCost(freightCost)
                .subtotal(subtotal)
                .total(total)
                .purchasedAt(purchasedAt)
                .dispatchedAt(dispatchedAt)
                .deliveredAt(deliveredAt)
                .items(Collections.emptyList())
                .payments(Collections.emptyList())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final Order domain = OrderMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertNotNull(domain.customer());
            assertEquals(customerEntity.getName(), domain.customer().name());
            assertEquals(status, domain.status());
            assertNotNull(domain.deliveryAddress());
            assertEquals(addressEntity.getStreet(), domain.deliveryAddress().street());
            assertEquals(freightCost, domain.freightCost());
            assertEquals(subtotal, domain.subtotal());
            assertEquals(total, domain.total());
            assertEquals(purchasedAt, domain.purchasedAt());
            assertEquals(dispatchedAt, domain.dispatchedAt());
            assertEquals(deliveredAt, domain.deliveredAt());
            assertTrue(domain.items().isEmpty());
            assertTrue(domain.payments().isEmpty());
            assertEquals(createdAt, domain.createdAt());
            assertEquals(updatedAt, domain.updatedAt());
        }

        @Test
        @DisplayName("should map entity with items to domain")
        void shouldMapEntityWithItemsToDomain() {
            final ProductJpaEntity productEntity = createProductEntity();

            final OrderItemJpaEntity itemEntity = OrderItemJpaEntity.builder()
                .id(UUID.randomUUID())
                .product(productEntity)
                .quantity(2)
                .unitPrice(new BigDecimal("50.00"))
                .totalPrice(new BigDecimal("100.00"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final OrderJpaEntity entity = createBasicOrderEntity();
            entity.setItems(List.of(itemEntity));

            final Order domain = OrderMapper.toDomain(entity);

            assertEquals(1, domain.items().size());
            assertEquals(itemEntity.getQuantity(), domain.items().getFirst().quantity());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> OrderMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final OrderStatus status = OrderStatus.DELIVERED;
            final BigDecimal freightCost = new BigDecimal("20.00");
            final BigDecimal subtotal = new BigDecimal("300.00");
            final BigDecimal total = new BigDecimal("320.00");
            final Instant purchasedAt = Instant.now().minusSeconds(86400);
            final Instant dispatchedAt = Instant.now().minusSeconds(43200);
            final Instant deliveredAt = Instant.now();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final Customer customer = createCustomerDomain();
            final Address address = createAddressDomain();

            final Order domain = Order.builder()
                .id(id)
                .customer(customer)
                .status(status)
                .deliveryAddress(address)
                .freightCost(freightCost)
                .subtotal(subtotal)
                .total(total)
                .purchasedAt(purchasedAt)
                .dispatchedAt(dispatchedAt)
                .deliveredAt(deliveredAt)
                .items(Collections.emptyList())
                .payments(Collections.emptyList())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final OrderJpaEntity entity = OrderMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertNotNull(entity.getCustomer());
            assertEquals(customer.name(), entity.getCustomer().getName());
            assertEquals(status, entity.getStatus());
            assertNotNull(entity.getDeliveryAddress());
            assertEquals(address.street(), entity.getDeliveryAddress().getStreet());
            assertEquals(freightCost, entity.getFreightCost());
            assertEquals(subtotal, entity.getSubtotal());
            assertEquals(total, entity.getTotal());
            assertEquals(purchasedAt, entity.getPurchasedAt());
            assertEquals(dispatchedAt, entity.getDispatchedAt());
            assertEquals(deliveredAt, entity.getDeliveredAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
        }

        @Test
        @DisplayName("should map domain with items to entity")
        void shouldMapDomainWithItemsToEntity() {
            final Product product = createProductDomain();

            final OrderItem item = OrderItem.builder()
                .id(UUID.randomUUID())
                .product(product)
                .quantity(3)
                .unitPrice(new BigDecimal("30.00"))
                .totalPrice(new BigDecimal("90.00"))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final Order domain = createBasicOrderDomain();
            final Order orderWithItems = domain.toBuilder()
                .items(List.of(item))
                .build();

            final OrderJpaEntity entity = OrderMapper.toEntity(orderWithItems);

            assertEquals(1, entity.getItems().size());
            assertEquals(item.quantity(), entity.getItems().getFirst().getQuantity());
            assertEquals(entity, entity.getItems().getFirst().getOrder());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> OrderMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = OrderMapper.class.getDeclaredConstructor();
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

    private OrderJpaEntity createBasicOrderEntity() {
        return OrderJpaEntity.builder()
            .id(UUID.randomUUID())
            .customer(createCustomerEntity())
            .status(OrderStatus.APPROVED)
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

    private Order createBasicOrderDomain() {
        return Order.builder()
            .id(UUID.randomUUID())
            .customer(createCustomerDomain())
            .status(OrderStatus.APPROVED)
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
}
