package rpgshop.infraestructure.persistence.mapper.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.product.Category;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.domain.entity.product.StatusChange;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;
import rpgshop.domain.entity.product.constant.StatusChangeType;
import rpgshop.infraestructure.persistence.entity.product.CategoryJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.PricingGroupJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductTypeJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.StatusChangeJpaEntity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ProductMapper")
class ProductMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final String name = "Chess Set";
            final BigDecimal height = new BigDecimal("10.00");
            final BigDecimal width = new BigDecimal("10.00");
            final BigDecimal depth = new BigDecimal("5.00");
            final BigDecimal weight = new BigDecimal("0.500");
            final String barcode = "1234567890123";
            final String sku = "CHE-001";
            final BigDecimal salePrice = new BigDecimal("99.90");
            final BigDecimal costPrice = new BigDecimal("50.00");
            final int stockQuantity = 10;
            final BigDecimal minimumSaleThreshold = new BigDecimal("79.90");
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final ProductTypeJpaEntity productType = createProductTypeEntity();
            final PricingGroupJpaEntity pricingGroup = createPricingGroupEntity();

            final ProductJpaEntity entity = ProductJpaEntity.builder()
                .id(id)
                .name(name)
                .productType(productType)
                .categories(Collections.emptyList())
                .height(height)
                .width(width)
                .depth(depth)
                .weight(weight)
                .pricingGroup(pricingGroup)
                .barcode(barcode)
                .sku(sku)
                .salePrice(salePrice)
                .costPrice(costPrice)
                .stockQuantity(stockQuantity)
                .statusChanges(Collections.emptyList())
                .minimumSaleThreshold(minimumSaleThreshold)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final Product domain = ProductMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertEquals(name, domain.name());
            assertNotNull(domain.productType());
            assertEquals(productType.getName(), domain.productType().name());
            assertTrue(domain.categories().isEmpty());
            assertEquals(height, domain.height());
            assertEquals(width, domain.width());
            assertEquals(depth, domain.depth());
            assertEquals(weight, domain.weight());
            assertNotNull(domain.pricingGroup());
            assertEquals(pricingGroup.getName(), domain.pricingGroup().name());
            assertEquals(barcode, domain.barcode());
            assertEquals(sku, domain.sku());
            assertEquals(salePrice, domain.salePrice());
            assertEquals(costPrice, domain.costPrice());
            assertEquals(stockQuantity, domain.stockQuantity());
            assertTrue(domain.statusChanges().isEmpty());
            assertEquals(minimumSaleThreshold, domain.minimumSaleThreshold());
            assertEquals(createdAt, domain.createdAt());
            assertEquals(updatedAt, domain.updatedAt());
        }

        @Test
        @DisplayName("should map entity with categories to domain")
        void shouldMapEntityWithCategoriesToDomain() {
            final CategoryJpaEntity categoryEntity = CategoryJpaEntity.builder()
                .id(UUID.randomUUID())
                .name("Board Games")
                .description("Tabletop board games")
                .products(Collections.emptyList())
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final ProductJpaEntity entity = createBasicProductEntity();
            entity.setCategories(List.of(categoryEntity));

            final Product domain = ProductMapper.toDomain(entity);

            assertEquals(1, domain.categories().size());
            assertEquals(categoryEntity.getName(), domain.categories().getFirst().name());
        }

        @Test
        @DisplayName("should map entity with status changes to domain")
        void shouldMapEntityWithStatusChangesToDomain() {
            final StatusChangeJpaEntity statusChangeEntity = StatusChangeJpaEntity.builder()
                .id(UUID.randomUUID())
                .reason("Out of stock")
                .category(StatusChangeCategory.OUT_OF_MARKET)
                .type(StatusChangeType.DEACTIVATE)
                .createdAt(Instant.now())
                .build();

            final ProductJpaEntity entity = createBasicProductEntity();
            entity.setStatusChanges(List.of(statusChangeEntity));

            final Product domain = ProductMapper.toDomain(entity);

            assertEquals(1, domain.statusChanges().size());
            assertEquals(statusChangeEntity.getReason(), domain.statusChanges().getFirst().reason());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> ProductMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final String name = "Dice Set";
            final BigDecimal height = new BigDecimal("5.00");
            final BigDecimal width = new BigDecimal("5.00");
            final BigDecimal depth = new BigDecimal("3.00");
            final BigDecimal weight = new BigDecimal("0.100");
            final String barcode = "9876543210987";
            final String sku = "DIC-001";
            final BigDecimal salePrice = new BigDecimal("29.90");
            final BigDecimal costPrice = new BigDecimal("10.00");
            final int stockQuantity = 50;
            final BigDecimal minimumSaleThreshold = new BigDecimal("19.90");
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final ProductType productType = createProductTypeDomain();
            final PricingGroup pricingGroup = createPricingGroupDomain();

            final Product domain = Product.builder()
                .id(id)
                .name(name)
                .productType(productType)
                .categories(Collections.emptyList())
                .height(height)
                .width(width)
                .depth(depth)
                .weight(weight)
                .pricingGroup(pricingGroup)
                .barcode(barcode)
                .sku(sku)
                .salePrice(salePrice)
                .costPrice(costPrice)
                .stockQuantity(stockQuantity)
                .statusChanges(Collections.emptyList())
                .minimumSaleThreshold(minimumSaleThreshold)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final ProductJpaEntity entity = ProductMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertEquals(name, entity.getName());
            assertNotNull(entity.getProductType());
            assertEquals(productType.name(), entity.getProductType().getName());
            assertEquals(height, entity.getHeight());
            assertEquals(width, entity.getWidth());
            assertEquals(depth, entity.getDepth());
            assertEquals(weight, entity.getWeight());
            assertNotNull(entity.getPricingGroup());
            assertEquals(pricingGroup.name(), entity.getPricingGroup().getName());
            assertEquals(barcode, entity.getBarcode());
            assertEquals(sku, entity.getSku());
            assertEquals(salePrice, entity.getSalePrice());
            assertEquals(costPrice, entity.getCostPrice());
            assertEquals(stockQuantity, entity.getStockQuantity());
            assertEquals(minimumSaleThreshold, entity.getMinimumSaleThreshold());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
        }

        @Test
        @DisplayName("should map domain with categories to entity")
        void shouldMapDomainWithCategoriesToEntity() {
            final Category category = Category.builder()
                .id(UUID.randomUUID())
                .name("Card Games")
                .description("Trading card games")
                .products(Collections.emptyList())
                .isActive(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final Product domain = createBasicProductDomain();
            final Product productWithCategories = domain.toBuilder()
                .categories(List.of(category))
                .build();

            final ProductJpaEntity entity = ProductMapper.toEntity(productWithCategories);

            assertEquals(1, entity.getCategories().size());
            assertEquals(category.name(), entity.getCategories().getFirst().getName());
        }

        @Test
        @DisplayName("should map domain with status changes to entity")
        void shouldMapDomainWithStatusChangesToEntity() {
            final StatusChange statusChange = StatusChange.builder()
                .id(UUID.randomUUID())
                .reason("Manual reactivation")
                .category(StatusChangeCategory.BACK_IN_DEMAND)
                .type(StatusChangeType.ACTIVATE)
                .createdAt(Instant.now())
                .build();

            final Product domain = createBasicProductDomain();
            final Product productWithStatusChanges = domain.toBuilder()
                .statusChanges(List.of(statusChange))
                .build();

            final ProductJpaEntity entity = ProductMapper.toEntity(productWithStatusChanges);

            assertEquals(1, entity.getStatusChanges().size());
            assertEquals(statusChange.reason(), entity.getStatusChanges().getFirst().getReason());
            assertEquals(entity, entity.getStatusChanges().getFirst().getProduct());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> ProductMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = ProductMapper.class.getDeclaredConstructor();
            constructor.setAccessible(true);

            final var exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
            assertInstanceOf(IllegalInstantiationException.class, exception.getCause());
        }
    }

    private ProductTypeJpaEntity createProductTypeEntity() {
        return ProductTypeJpaEntity.builder()
            .id(UUID.randomUUID())
            .name("Physical")
            .description("Physical products")
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private PricingGroupJpaEntity createPricingGroupEntity() {
        return PricingGroupJpaEntity.builder()
            .id(UUID.randomUUID())
            .name("Standard")
            .marginPercentage(new BigDecimal("10.00"))
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private ProductType createProductTypeDomain() {
        return ProductType.builder()
            .id(UUID.randomUUID())
            .name("Physical")
            .description("Physical products")
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private PricingGroup createPricingGroupDomain() {
        return PricingGroup.builder()
            .id(UUID.randomUUID())
            .name("Standard")
            .marginPercentage(new BigDecimal("10.00"))
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private ProductJpaEntity createBasicProductEntity() {
        return ProductJpaEntity.builder()
            .id(UUID.randomUUID())
            .name("Test Product")
            .productType(createProductTypeEntity())
            .categories(Collections.emptyList())
            .height(new BigDecimal("10.00"))
            .width(new BigDecimal("10.00"))
            .depth(new BigDecimal("5.00"))
            .weight(new BigDecimal("0.500"))
            .pricingGroup(createPricingGroupEntity())
            .barcode("1234567890123")
            .sku("TST-001")
            .salePrice(new BigDecimal("99.90"))
            .costPrice(new BigDecimal("50.00"))
            .stockQuantity(10)
            .statusChanges(Collections.emptyList())
            .minimumSaleThreshold(new BigDecimal("79.90"))
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }

    private Product createBasicProductDomain() {
        return Product.builder()
            .id(UUID.randomUUID())
            .name("Test Product")
            .productType(createProductTypeDomain())
            .categories(Collections.emptyList())
            .height(new BigDecimal("10.00"))
            .width(new BigDecimal("10.00"))
            .depth(new BigDecimal("5.00"))
            .weight(new BigDecimal("0.500"))
            .pricingGroup(createPricingGroupDomain())
            .barcode("1234567890123")
            .sku("TST-001")
            .salePrice(new BigDecimal("99.90"))
            .costPrice(new BigDecimal("50.00"))
            .stockQuantity(10)
            .statusChanges(Collections.emptyList())
            .minimumSaleThreshold(new BigDecimal("79.90"))
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }
}
