package rpgshop.infraestructure.persistence.mapper.cart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.cart.CartItem;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.infraestructure.persistence.entity.cart.CartItemJpaEntity;
import rpgshop.infraestructure.persistence.entity.cart.CartJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.PricingGroupJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductTypeJpaEntity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CartItemMapper")
class CartItemMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final int quantity = 3;
            final boolean isBlocked = true;
            final Instant blockedAt = Instant.now();
            final Instant expiresAt = Instant.now().plusSeconds(3600);
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final CartJpaEntity cartEntity = CartJpaEntity.builder()
                .id(UUID.randomUUID())
                .build();

            final ProductJpaEntity productEntity = createProductEntity();

            final CartItemJpaEntity entity = CartItemJpaEntity.builder()
                .id(id)
                .cart(cartEntity)
                .product(productEntity)
                .quantity(quantity)
                .isBlocked(isBlocked)
                .blockedAt(blockedAt)
                .expiresAt(expiresAt)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final CartItem domain = CartItemMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertEquals(cartEntity.getId(), domain.cartId());
            assertNotNull(domain.product());
            assertEquals(productEntity.getName(), domain.product().name());
            assertEquals(quantity, domain.quantity());
            assertEquals(isBlocked, domain.isBlocked());
            assertEquals(blockedAt, domain.blockedAt());
            assertEquals(expiresAt, domain.expiresAt());
            assertEquals(createdAt, domain.createdAt());
            assertEquals(updatedAt, domain.updatedAt());
        }

        @Test
        @DisplayName("should map entity without cart to domain with null cartId")
        void shouldMapEntityWithoutCartToDomainWithNullCartId() {
            final CartItemJpaEntity entity = CartItemJpaEntity.builder()
                .id(UUID.randomUUID())
                .cart(null)
                .product(createProductEntity())
                .quantity(1)
                .isBlocked(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final CartItem domain = CartItemMapper.toDomain(entity);

            assertNull(domain.cartId());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> CartItemMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final int quantity = 5;
            final boolean isBlocked = false;
            final Instant blockedAt = null;
            final Instant expiresAt = null;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final Product product = createProductDomain();

            final CartItem domain = CartItem.builder()
                .id(id)
                .cartId(UUID.randomUUID())
                .product(product)
                .quantity(quantity)
                .isBlocked(isBlocked)
                .blockedAt(null)
                .expiresAt(null)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final CartItemJpaEntity entity = CartItemMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertNotNull(entity.getProduct());
            assertEquals(product.name(), entity.getProduct().getName());
            assertEquals(quantity, entity.getQuantity());
            assertEquals(isBlocked, entity.isBlocked());
            assertNull(entity.getBlockedAt());
            assertNull(entity.getExpiresAt());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> CartItemMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = CartItemMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            final var exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
            assertInstanceOf(IllegalInstantiationException.class, exception.getCause());
        }
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
