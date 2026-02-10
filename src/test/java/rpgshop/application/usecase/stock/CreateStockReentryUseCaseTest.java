package rpgshop.application.usecase.stock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.application.gateway.stock.StockEntryGateway;
import rpgshop.application.gateway.supplier.SupplierGateway;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.stock.StockEntry;
import rpgshop.domain.entity.supplier.Supplier;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateStockReentryUseCaseTest {
    @Mock
    private StockEntryGateway stockEntryGateway;

    @Mock
    private ProductGateway productGateway;

    @Mock
    private SupplierGateway supplierGateway;

    @InjectMocks
    private CreateStockReentryUseCase useCase;

    @Test
    void shouldCreateReentryAndUpdateProductStockAndPrice() {
        final UUID productId = UUID.randomUUID();
        final UUID supplierId = UUID.randomUUID();
        final Product product = Product.builder()
            .id(productId)
            .pricingGroup(PricingGroup.builder().marginPercentage(new BigDecimal("10.00")).build())
            .build();
        final Supplier supplier = Supplier.builder().id(supplierId).build();

        when(productGateway.findById(productId)).thenReturn(Optional.of(product));
        when(supplierGateway.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(stockEntryGateway.save(any(StockEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(stockEntryGateway.sumQuantityByProductId(productId)).thenReturn(12);
        when(stockEntryGateway.findMaxCostValueByProductId(productId)).thenReturn(Optional.of(new BigDecimal("30.00")));

        final StockEntry saved = useCase.execute(productId, 2, new BigDecimal("25.00"), supplierId);

        assertEquals(2, saved.quantity());
        assertEquals(new BigDecimal("25.00"), saved.costValue());

        final ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productGateway).save(productCaptor.capture());

        final Product updated = productCaptor.getValue();
        assertEquals(12, updated.stockQuantity());
        assertEquals(new BigDecimal("30.00"), updated.costPrice());
        assertEquals(new BigDecimal("33.00"), updated.salePrice());
    }

    @Test
    void shouldThrowWhenQuantityIsInvalid() {
        assertThrows(
            BusinessRuleException.class,
            () -> useCase.execute(UUID.randomUUID(), 0, new BigDecimal("25.00"), UUID.randomUUID())
        );
    }
}
