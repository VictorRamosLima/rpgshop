package rpgshop.infraestructure.persistence.gateway.stock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import rpgshop.infraestructure.persistence.repository.stock.StockEntryRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockEntryGatewayJpaTest {
    @Mock
    private StockEntryRepository stockEntryRepository;

    @InjectMocks
    private StockEntryGatewayJpa stockEntryGatewayJpa;

    private ProductJpaEntity createProductJpaEntity(UUID productId, Instant now) {
        final UUID typeId = UUID.randomUUID();
        final UUID pricingId = UUID.randomUUID();

        final ProductTypeJpaEntity typeJpa = ProductTypeJpaEntity.builder()
            .id(typeId).name("Type").isActive(true).createdAt(now).updatedAt(now).build();
        final PricingGroupJpaEntity pricingJpa = PricingGroupJpaEntity.builder()
            .id(pricingId).name("Pricing").marginPercentage(BigDecimal.TEN).isActive(true)
            .createdAt(now).updatedAt(now).build();

        return ProductJpaEntity.builder()
            .id(productId).name("Product").productType(typeJpa).pricingGroup(pricingJpa)
            .stockQuantity(10).createdAt(now).updatedAt(now).build();
    }

    private Product createProduct(UUID productId, Instant now) {
        final UUID typeId = UUID.randomUUID();
        final UUID pricingId = UUID.randomUUID();

        final ProductType type = ProductType.builder()
            .id(typeId).name("Type").isActive(true).createdAt(now).updatedAt(now).build();
        final PricingGroup pricing = PricingGroup.builder()
            .id(pricingId).name("Pricing").marginPercentage(BigDecimal.TEN).isActive(true)
            .createdAt(now).updatedAt(now).build();

        return Product.builder()
            .id(productId).name("Product").productType(type).pricingGroup(pricing)
            .stockQuantity(10).createdAt(now).updatedAt(now).build();
    }

    private SupplierJpaEntity createSupplierJpaEntity(UUID supplierId, Instant now) {
        return SupplierJpaEntity.builder()
            .id(supplierId).name("Supplier").cnpj("12345678000190")
            .isActive(true).createdAt(now).updatedAt(now).build();
    }

    private Supplier createSupplier(UUID supplierId, Instant now) {
        return Supplier.builder()
            .id(supplierId).name("Supplier").cnpj("12345678000190")
            .isActive(true).createdAt(now).updatedAt(now).build();
    }

    @Test
    void shouldSaveStockEntry() {
        final UUID entryId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final UUID supplierId = UUID.randomUUID();
        final Instant now = Instant.now();

        final Product product = createProduct(productId, now);
        final Supplier supplier = createSupplier(supplierId, now);

        final StockEntry entry = StockEntry.builder()
            .id(entryId).product(product).supplier(supplier).quantity(100)
            .costValue(new BigDecimal("50.00")).entryDate(LocalDate.now())
            .isReentry(false).createdAt(now).updatedAt(now).build();

        final ProductJpaEntity productEntity = createProductJpaEntity(productId, now);
        final SupplierJpaEntity supplierEntity = createSupplierJpaEntity(supplierId, now);

        final StockEntryJpaEntity savedEntity = StockEntryJpaEntity.builder()
            .id(entryId).product(productEntity).supplier(supplierEntity).quantity(100)
            .costValue(new BigDecimal("50.00")).entryDate(LocalDate.now())
            .isReentry(false).createdAt(now).updatedAt(now).build();

        when(stockEntryRepository.save(any(StockEntryJpaEntity.class))).thenReturn(savedEntity);

        final StockEntry result = stockEntryGatewayJpa.save(entry);

        assertNotNull(result);
        assertEquals(entryId, result.id());
        assertEquals(100, result.quantity());
        assertEquals(new BigDecimal("50.00"), result.costValue());
        verify(stockEntryRepository, times(1)).save(any(StockEntryJpaEntity.class));
    }

    @Test
    void shouldFindById() {
        final UUID entryId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final UUID supplierId = UUID.randomUUID();
        final Instant now = Instant.now();

        final ProductJpaEntity productEntity = createProductJpaEntity(productId, now);
        final SupplierJpaEntity supplierEntity = createSupplierJpaEntity(supplierId, now);

        final StockEntryJpaEntity entity = StockEntryJpaEntity.builder()
            .id(entryId).product(productEntity).supplier(supplierEntity).quantity(100)
            .costValue(new BigDecimal("50.00")).entryDate(LocalDate.now())
            .isReentry(false).createdAt(now).updatedAt(now).build();

        when(stockEntryRepository.findById(entryId)).thenReturn(Optional.of(entity));

        final Optional<StockEntry> result = stockEntryGatewayJpa.findById(entryId);

        assertTrue(result.isPresent());
        assertEquals(entryId, result.get().id());
        verify(stockEntryRepository, times(1)).findById(entryId);
    }

    @Test
    void shouldReturnEmptyWhenStockEntryNotFoundById() {
        final UUID entryId = UUID.randomUUID();

        when(stockEntryRepository.findById(entryId)).thenReturn(Optional.empty());

        assertTrue(stockEntryGatewayJpa.findById(entryId).isEmpty());
        verify(stockEntryRepository, times(1)).findById(entryId);
    }

    @Test
    void shouldFindByProductId() {
        final UUID productId = UUID.randomUUID();
        final UUID entryId = UUID.randomUUID();
        final UUID supplierId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);

        final ProductJpaEntity productEntity = createProductJpaEntity(productId, now);
        final SupplierJpaEntity supplierEntity = createSupplierJpaEntity(supplierId, now);

        final StockEntryJpaEntity entity = StockEntryJpaEntity.builder()
            .id(entryId).product(productEntity).supplier(supplierEntity).quantity(100)
            .costValue(new BigDecimal("50.00")).entryDate(LocalDate.now())
            .isReentry(false).createdAt(now).updatedAt(now).build();

        final Page<StockEntryJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(stockEntryRepository.findByProductId(productId, pageable)).thenReturn(page);

        final Page<StockEntry> result = stockEntryGatewayJpa.findByProductId(productId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(entryId, result.getContent().getFirst().id());
        verify(stockEntryRepository, times(1)).findByProductId(productId, pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoStockEntriesFoundByProductId() {
        final UUID productId = UUID.randomUUID();
        final Pageable pageable = PageRequest.of(0, 10);

        final Page<StockEntryJpaEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(stockEntryRepository.findByProductId(productId, pageable)).thenReturn(emptyPage);

        final Page<StockEntry> result = stockEntryGatewayJpa.findByProductId(productId, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockEntryRepository, times(1)).findByProductId(productId, pageable);
    }

    @Test
    void shouldFindMaxCostValueByProductId() {
        final UUID productId = UUID.randomUUID();
        final BigDecimal maxCostValue = new BigDecimal("100.00");

        when(stockEntryRepository.findMaxCostValueByProductId(productId)).thenReturn(Optional.of(maxCostValue));

        final Optional<BigDecimal> result = stockEntryGatewayJpa.findMaxCostValueByProductId(productId);

        assertTrue(result.isPresent());
        assertEquals(maxCostValue, result.get());
        verify(stockEntryRepository, times(1)).findMaxCostValueByProductId(productId);
    }

    @Test
    void shouldReturnEmptyWhenNoMaxCostValueFoundByProductId() {
        final UUID productId = UUID.randomUUID();

        when(stockEntryRepository.findMaxCostValueByProductId(productId)).thenReturn(Optional.empty());

        final Optional<BigDecimal> result = stockEntryGatewayJpa.findMaxCostValueByProductId(productId);

        assertTrue(result.isEmpty());
        verify(stockEntryRepository, times(1)).findMaxCostValueByProductId(productId);
    }

    @Test
    void shouldSumQuantityByProductId() {
        final UUID productId = UUID.randomUUID();

        when(stockEntryRepository.sumQuantityByProductId(productId)).thenReturn(500);

        final int result = stockEntryGatewayJpa.sumQuantityByProductId(productId);

        assertEquals(500, result);
        verify(stockEntryRepository, times(1)).sumQuantityByProductId(productId);
    }

    @Test
    void shouldReturnZeroWhenNoQuantitySummedByProductId() {
        final UUID productId = UUID.randomUUID();

        when(stockEntryRepository.sumQuantityByProductId(productId)).thenReturn(0);

        final int result = stockEntryGatewayJpa.sumQuantityByProductId(productId);

        assertEquals(0, result);
        verify(stockEntryRepository, times(1)).sumQuantityByProductId(productId);
    }
}
