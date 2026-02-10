package rpgshop.infraestructure.persistence.gateway.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rpgshop.application.command.product.ProductFilter;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.infraestructure.persistence.entity.product.PricingGroupJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductTypeJpaEntity;
import rpgshop.infraestructure.persistence.repository.product.ProductRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductGatewayJpaTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductGatewayJpa productGatewayJpa;

    private ProductTypeJpaEntity createProductTypeJpaEntity(UUID id, Instant now) {
        return ProductTypeJpaEntity.builder()
            .id(id)
            .name("RPG Book Type")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private PricingGroupJpaEntity createPricingGroupJpaEntity(UUID id, Instant now) {
        return PricingGroupJpaEntity.builder()
            .id(id)
            .name("Standard Pricing")
            .marginPercentage(new BigDecimal("20.00"))
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private ProductType createProductType(UUID id, Instant now) {
        return ProductType.builder()
            .id(id)
            .name("RPG Book Type")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private PricingGroup createPricingGroup(UUID id, Instant now) {
        return PricingGroup.builder()
            .id(id)
            .name("Standard Pricing")
            .marginPercentage(new BigDecimal("20.00"))
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    @Test
    void shouldSaveProduct() {
        final UUID productId = UUID.randomUUID();
        final UUID productTypeId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final Instant now = Instant.now();

        final ProductType productType = createProductType(productTypeId, now);
        final PricingGroup pricingGroup = createPricingGroup(pricingGroupId, now);

        final Product product = Product.builder()
            .id(productId)
            .name("RPG Book")
            .productType(productType)
            .pricingGroup(pricingGroup)
            .sku("SKU-001")
            .barcode("123456789")
            .salePrice(new BigDecimal("100.00"))
            .costPrice(new BigDecimal("50.00"))
            .stockQuantity(10)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final ProductTypeJpaEntity productTypeJpa = createProductTypeJpaEntity(productTypeId, now);
        final PricingGroupJpaEntity pricingGroupJpa = createPricingGroupJpaEntity(pricingGroupId, now);

        final ProductJpaEntity savedEntity = ProductJpaEntity.builder()
            .id(productId)
            .name("RPG Book")
            .productType(productTypeJpa)
            .pricingGroup(pricingGroupJpa)
            .sku("SKU-001")
            .barcode("123456789")
            .salePrice(new BigDecimal("100.00"))
            .costPrice(new BigDecimal("50.00"))
            .stockQuantity(10)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(productRepository.save(any(ProductJpaEntity.class))).thenReturn(savedEntity);

        final Product result = productGatewayJpa.save(product);

        assertNotNull(result);
        assertEquals(productId, result.id());
        assertEquals("RPG Book", result.name());
        verify(productRepository, times(1)).save(any(ProductJpaEntity.class));
    }

    @Test
    void shouldFindById() {
        final UUID productId = UUID.randomUUID();
        final UUID productTypeId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final Instant now = Instant.now();

        final ProductTypeJpaEntity productTypeJpa = createProductTypeJpaEntity(productTypeId, now);
        final PricingGroupJpaEntity pricingGroupJpa = createPricingGroupJpaEntity(pricingGroupId, now);

        final ProductJpaEntity entity = ProductJpaEntity.builder()
            .id(productId)
            .name("RPG Book")
            .productType(productTypeJpa)
            .pricingGroup(pricingGroupJpa)
            .sku("SKU-001")
            .barcode("123456789")
            .salePrice(new BigDecimal("100.00"))
            .costPrice(new BigDecimal("50.00"))
            .stockQuantity(10)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(entity));

        final Optional<Product> result = productGatewayJpa.findById(productId);

        assertTrue(result.isPresent());
        assertEquals(productId, result.get().id());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void shouldReturnEmptyWhenProductNotFoundById() {
        final UUID productId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertTrue(productGatewayJpa.findById(productId).isEmpty());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void shouldFindByFilters() {
        final UUID productId = UUID.randomUUID();
        final UUID productTypeId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);

        final ProductFilter filter = new ProductFilter(
            "RPG", null, null, null, "SKU-001", null, true, null, null
        );

        final ProductTypeJpaEntity productTypeJpa = createProductTypeJpaEntity(productTypeId, now);
        final PricingGroupJpaEntity pricingGroupJpa = createPricingGroupJpaEntity(pricingGroupId, now);

        final ProductJpaEntity entity = ProductJpaEntity.builder()
            .id(productId)
            .name("RPG Book")
            .productType(productTypeJpa)
            .pricingGroup(pricingGroupJpa)
            .sku("SKU-001")
            .barcode("123456789")
            .salePrice(new BigDecimal("100.00"))
            .costPrice(new BigDecimal("50.00"))
            .stockQuantity(10)
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final Page<ProductJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(productRepository.findByFilters(
            filter.name(), filter.productTypeId(), filter.categoryId(),
            filter.pricingGroupId(), filter.sku(), filter.barcode(),
            filter.isActive(), filter.minPrice(), filter.maxPrice(), pageable
        )).thenReturn(page);

        final Page<Product> result = productGatewayJpa.findByFilters(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("RPG Book", result.getContent().getFirst().name());
        verify(productRepository, times(1)).findByFilters(
            filter.name(), filter.productTypeId(), filter.categoryId(),
            filter.pricingGroupId(), filter.sku(), filter.barcode(),
            filter.isActive(), filter.minPrice(), filter.maxPrice(), pageable
        );
    }

    @Test
    void shouldFindEligibleForAutoDeactivation() {
        final UUID productId1 = UUID.randomUUID();
        final UUID productId2 = UUID.randomUUID();
        final BigDecimal threshold = new BigDecimal("50.00");

        when(productRepository.findEligibleForAutoInactivation(threshold))
            .thenReturn(List.of(productId1, productId2));

        final List<UUID> result = productGatewayJpa.findEligibleForAutoDeactivation(threshold);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(productId1));
        assertTrue(result.contains(productId2));
        verify(productRepository, times(1)).findEligibleForAutoInactivation(threshold);
    }

    @Test
    void shouldReturnEmptyListWhenNoEligibleProductsForAutoDeactivation() {
        final BigDecimal threshold = new BigDecimal("50.00");

        when(productRepository.findEligibleForAutoInactivation(threshold)).thenReturn(List.of());

        final List<UUID> result = productGatewayJpa.findEligibleForAutoDeactivation(threshold);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findEligibleForAutoInactivation(threshold);
    }

    @Test
    void shouldReturnTrueWhenExistsBySku() {
        final String sku = "SKU-001";

        when(productRepository.existsBySku(sku)).thenReturn(true);

        assertTrue(productGatewayJpa.existsBySku(sku));
        verify(productRepository, times(1)).existsBySku(sku);
    }

    @Test
    void shouldReturnFalseWhenNotExistsBySku() {
        final String sku = "SKU-001";

        when(productRepository.existsBySku(sku)).thenReturn(false);

        assertFalse(productGatewayJpa.existsBySku(sku));
        verify(productRepository, times(1)).existsBySku(sku);
    }

    @Test
    void shouldReturnTrueWhenExistsByBarcode() {
        final String barcode = "123456789";

        when(productRepository.existsByBarcode(barcode)).thenReturn(true);

        assertTrue(productGatewayJpa.existsByBarcode(barcode));
        verify(productRepository, times(1)).existsByBarcode(barcode);
    }

    @Test
    void shouldReturnFalseWhenNotExistsByBarcode() {
        final String barcode = "123456789";

        when(productRepository.existsByBarcode(barcode)).thenReturn(false);

        assertFalse(productGatewayJpa.existsByBarcode(barcode));
        verify(productRepository, times(1)).existsByBarcode(barcode);
    }

    @Test
    void shouldDeactivateOutOfMarket() {
        final UUID productId1 = UUID.randomUUID();
        final UUID productId2 = UUID.randomUUID();
        final List<UUID> ids = List.of(productId1, productId2);
        final Instant now = Instant.now();

        when(productRepository.bulkDeactivateByIds(ids, now)).thenReturn(2);

        final int result = productGatewayJpa.deactivateOutOfMarket(ids, now);

        assertEquals(2, result);
        verify(productRepository, times(1)).bulkDeactivateByIds(ids, now);
    }

    @Test
    void shouldReturnZeroWhenNoProductsDeactivated() {
        final List<UUID> ids = List.of();
        final Instant now = Instant.now();

        when(productRepository.bulkDeactivateByIds(ids, now)).thenReturn(0);

        final int result = productGatewayJpa.deactivateOutOfMarket(ids, now);

        assertEquals(0, result);
        verify(productRepository, times(1)).bulkDeactivateByIds(ids, now);
    }
}

