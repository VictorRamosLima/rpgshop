package rpgshop.infraestructure.persistence.mapper.cart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.cart.Cart;
import rpgshop.domain.entity.cart.CartItem;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.infraestructure.persistence.entity.cart.CartItemJpaEntity;
import rpgshop.infraestructure.persistence.entity.cart.CartJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;
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

@DisplayName("CartMapper")
class CartMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final CustomerJpaEntity customerEntity = createCustomerEntity();

            final CartJpaEntity entity = CartJpaEntity.builder()
                .id(id)
                .customer(customerEntity)
                .items(Collections.emptyList())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final Cart domain = CartMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertNotNull(domain.customer());
            assertEquals(customerEntity.getName(), domain.customer().name());
            assertTrue(domain.items().isEmpty());
            assertEquals(createdAt, domain.createdAt());
            assertEquals(updatedAt, domain.updatedAt());
        }

        @Test
        @DisplayName("should map entity with items to domain")
        void shouldMapEntityWithItemsToDomain() {
            final ProductJpaEntity productEntity = createProductEntity();

            final CartJpaEntity cartEntity = CartJpaEntity.builder()
                .id(UUID.randomUUID())
                .customer(createCustomerEntity())
                .items(Collections.emptyList())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final CartItemJpaEntity itemEntity = CartItemJpaEntity.builder()
                .id(UUID.randomUUID())
                .cart(cartEntity)
                .product(productEntity)
                .quantity(2)
                .isBlocked(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            cartEntity.setItems(List.of(itemEntity));

            final Cart domain = CartMapper.toDomain(cartEntity);

            assertEquals(1, domain.items().size());
            assertEquals(itemEntity.getQuantity(), domain.items().getFirst().quantity());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> CartMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final Customer customer = createCustomerDomain();

            final Cart domain = Cart.builder()
                .id(id)
                .customer(customer)
                .items(Collections.emptyList())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final CartJpaEntity entity = CartMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertNotNull(entity.getCustomer());
            assertEquals(customer.name(), entity.getCustomer().getName());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
        }

        @Test
        @DisplayName("should map domain with items to entity")
        void shouldMapDomainWithItemsToEntity() {
            final Product product = createProductDomain();

            final CartItem item = CartItem.builder()
                .id(UUID.randomUUID())
                .product(product)
                .quantity(3)
                .isBlocked(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final Cart domain = Cart.builder()
                .id(UUID.randomUUID())
                .customer(createCustomerDomain())
                .items(List.of(item))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final CartJpaEntity entity = CartMapper.toEntity(domain);

            assertEquals(1, entity.getItems().size());
            assertEquals(item.quantity(), entity.getItems().getFirst().getQuantity());
            assertEquals(entity, entity.getItems().getFirst().getCart());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> CartMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = CartMapper.class.getDeclaredConstructor();
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
}
