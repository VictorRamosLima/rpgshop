package rpgshop.infraestructure.persistence.mapper.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.product.Category;
import rpgshop.infraestructure.persistence.entity.product.CategoryJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.PricingGroupJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductTypeJpaEntity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CategoryMapper")
class CategoryMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final String name = "Board Games";
            final String description = "Tabletop board games";
            final boolean isActive = true;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final CategoryJpaEntity entity = CategoryJpaEntity.builder()
                .id(id)
                .name(name)
                .description(description)
                .products(Collections.emptyList())
                .isActive(isActive)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deactivatedAt(null)
                .build();

            final Category domain = CategoryMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertEquals(name, domain.name());
            assertEquals(description, domain.description());
            assertTrue(domain.products().isEmpty());
            assertEquals(isActive, domain.isActive());
            assertEquals(createdAt, domain.createdAt());
            assertEquals(updatedAt, domain.updatedAt());
            assertNull(domain.deactivatedAt());
        }

        @Test
        @DisplayName("should map entity with products to domain")
        void shouldMapEntityWithProductsToDomain() {
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

            final ProductJpaEntity productEntity = ProductJpaEntity.builder()
                .id(UUID.randomUUID())
                .name("Chess Set")
                .productType(productType)
                .pricingGroup(pricingGroup)
                .height(new BigDecimal("10.00"))
                .width(new BigDecimal("10.00"))
                .depth(new BigDecimal("5.00"))
                .weight(new BigDecimal("0.500"))
                .salePrice(new BigDecimal("99.90"))
                .costPrice(new BigDecimal("50.00"))
                .stockQuantity(10)
                .statusChanges(Collections.emptyList())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final CategoryJpaEntity entity = CategoryJpaEntity.builder()
                .id(UUID.randomUUID())
                .name("Board Games")
                .description("Tabletop board games")
                .products(List.of(productEntity))
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final Category domain = CategoryMapper.toDomain(entity);

            assertEquals(1, domain.products().size());
            assertEquals(productEntity.getName(), domain.products().getFirst().name());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> CategoryMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final String name = "Card Games";
            final String description = "Trading and collectible card games";
            final boolean isActive = false;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final Instant deactivatedAt = Instant.now();

            final Category domain = Category.builder()
                .id(id)
                .name(name)
                .description(description)
                .products(Collections.emptyList())
                .isActive(isActive)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .deactivatedAt(deactivatedAt)
                .build();

            final CategoryJpaEntity entity = CategoryMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertEquals(name, entity.getName());
            assertEquals(description, entity.getDescription());
            assertEquals(isActive, entity.isActive());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(deactivatedAt, entity.getDeactivatedAt());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> CategoryMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = CategoryMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            final var exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
            assertInstanceOf(IllegalInstantiationException.class, exception.getCause());
        }
    }
}
