package rpgshop.infraestructure.persistence.mapper.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.domain.entity.stock.StockEntry;
import rpgshop.domain.entity.supplier.Supplier;
import rpgshop.infraestructure.persistence.entity.product.PricingGroupJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductTypeJpaEntity;
import rpgshop.infraestructure.persistence.entity.stock.StockEntryJpaEntity;
import rpgshop.infraestructure.persistence.entity.supplier.SupplierJpaEntity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("StockEntryMapper")
class StockEntryMapperTest {
    @Nested
    @DisplayName("toDomain")
    class ToDomainTests {
        @Test
        @DisplayName("should map entity to domain with all fields")
        void shouldMapEntityToDomainWithAllFields() {
            final UUID id = UUID.randomUUID();
            final int quantity = 100;
            final BigDecimal costValue = new BigDecimal("5000.00");
            final LocalDate entryDate = LocalDate.now();
            final boolean isReentry = false;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final ProductJpaEntity productEntity = createProductEntity();
            final SupplierJpaEntity supplierEntity = createSupplierEntity();

            final StockEntryJpaEntity entity = StockEntryJpaEntity.builder()
                .id(id)
                .product(productEntity)
                .quantity(quantity)
                .costValue(costValue)
                .supplier(supplierEntity)
                .entryDate(entryDate)
                .isReentry(isReentry)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final StockEntry domain = StockEntryMapper.toDomain(entity);

            assertNotNull(domain);
            assertEquals(id, domain.id());
            assertNotNull(domain.product());
            assertEquals(productEntity.getName(), domain.product().name());
            assertEquals(quantity, domain.quantity());
            assertEquals(costValue, domain.costValue());
            assertNotNull(domain.supplier());
            assertEquals(supplierEntity.getName(), domain.supplier().name());
            assertEquals(entryDate, domain.entryDate());
            assertEquals(isReentry, domain.isReentry());
            assertEquals(createdAt, domain.createdAt());
            assertEquals(updatedAt, domain.updatedAt());
        }

        @Test
        @DisplayName("should map entity with isReentry true to domain")
        void shouldMapEntityWithIsReentryTrueToDomain() {
            final StockEntryJpaEntity entity = StockEntryJpaEntity.builder()
                .id(UUID.randomUUID())
                .product(createProductEntity())
                .quantity(50)
                .costValue(new BigDecimal("2500.00"))
                .supplier(createSupplierEntity())
                .entryDate(LocalDate.now())
                .isReentry(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            final StockEntry domain = StockEntryMapper.toDomain(entity);

            assertTrue(domain.isReentry());
        }

        @Test
        @DisplayName("should throw exception when entity is null")
        void shouldThrowExceptionWhenEntityIsNull() {
            assertThrows(IllegalArgumentException.class, () -> StockEntryMapper.toDomain(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntityTests {
        @Test
        @DisplayName("should map domain to entity with all fields")
        void shouldMapDomainToEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final int quantity = 200;
            final BigDecimal costValue = new BigDecimal("10000.00");
            final LocalDate entryDate = LocalDate.now();
            final boolean isReentry = true;
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();

            final Product product = createProductDomain();
            final Supplier supplier = createSupplierDomain();

            final StockEntry domain = StockEntry.builder()
                .id(id)
                .product(product)
                .quantity(quantity)
                .costValue(costValue)
                .supplier(supplier)
                .entryDate(entryDate)
                .isReentry(isReentry)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

            final StockEntryJpaEntity entity = StockEntryMapper.toEntity(domain);

            assertNotNull(entity);
            assertEquals(id, entity.getId());
            assertNotNull(entity.getProduct());
            assertEquals(product.name(), entity.getProduct().getName());
            assertEquals(quantity, entity.getQuantity());
            assertEquals(costValue, entity.getCostValue());
            assertNotNull(entity.getSupplier());
            assertEquals(supplier.name(), entity.getSupplier().getName());
            assertEquals(entryDate, entity.getEntryDate());
            assertEquals(isReentry, entity.isReentry());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
        }

        @Test
        @DisplayName("should throw exception when domain is null")
        void shouldThrowExceptionWhenDomainIsNull() {
            assertThrows(IllegalArgumentException.class, () -> StockEntryMapper.toEntity(null));
        }
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {
        @Test
        @DisplayName("should throw exception when instantiated via reflection")
        void shouldThrowExceptionWhenInstantiatedViaReflection() throws NoSuchMethodException {
            final var constructor = StockEntryMapper.class.getDeclaredConstructor();
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

    private SupplierJpaEntity createSupplierEntity() {
        return SupplierJpaEntity.builder()
            .id(UUID.randomUUID())
            .name("RPG Supplies Co.")
            .legalName("RPG Supplies Company LTDA")
            .cnpj("12345678000199")
            .email("contact@rpgsupplies.com")
            .phone("11999887766")
            .isActive(true)
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

    private Supplier createSupplierDomain() {
        return Supplier.builder()
            .id(UUID.randomUUID())
            .name("RPG Supplies Co.")
            .legalName("RPG Supplies Company LTDA")
            .cnpj("12345678000199")
            .email("contact@rpgsupplies.com")
            .phone("11999887766")
            .isActive(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    }
}
