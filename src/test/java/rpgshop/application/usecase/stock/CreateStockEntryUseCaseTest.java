package rpgshop.application.usecase.stock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.stock.CreateStockEntryCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.application.gateway.stock.StockEntryGateway;
import rpgshop.application.gateway.supplier.SupplierGateway;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.stock.StockEntry;
import rpgshop.domain.entity.supplier.Supplier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateStockEntryUseCaseTest {
    @Mock
    private StockEntryGateway stockEntryGateway;

    @Mock
    private ProductGateway productGateway;

    @Mock
    private SupplierGateway supplierGateway;

    @InjectMocks
    private CreateStockEntryUseCase useCase;

    @Test
    void shouldCreateEntryAndUpdateProductStockAndPrice() {
        final UUID productId = UUID.randomUUID();
        final UUID supplierId = UUID.randomUUID();
        final CreateStockEntryCommand command = new CreateStockEntryCommand(
            productId,
            10,
            new BigDecimal("40.00"),
            supplierId,
            LocalDate.now()
        );

        final Product product = Product.builder()
            .id(productId)
            .pricingGroup(PricingGroup.builder().marginPercentage(new BigDecimal("20.00")).build())
            .build();
        final Supplier supplier = Supplier.builder().id(supplierId).build();

        when(productGateway.findById(productId)).thenReturn(Optional.of(product));
        when(supplierGateway.findById(supplierId)).thenReturn(Optional.of(supplier));
        when(stockEntryGateway.save(any(StockEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(stockEntryGateway.sumQuantityByProductId(productId)).thenReturn(15);
        when(stockEntryGateway.findMaxCostValueByProductId(productId)).thenReturn(Optional.of(new BigDecimal("50.00")));

        final StockEntry saved = useCase.execute(command);

        assertEquals(10, saved.quantity());
        assertEquals(new BigDecimal("40.00"), saved.costValue());

        final ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productGateway).save(productCaptor.capture());

        final Product updatedProduct = productCaptor.getValue();
        assertEquals(15, updatedProduct.stockQuantity());
        assertEquals(new BigDecimal("50.00"), updatedProduct.costPrice());
        assertEquals(new BigDecimal("60.00"), updatedProduct.salePrice());
    }

    @Test
    void shouldThrowWhenQuantityIsInvalid() {
        final CreateStockEntryCommand command = new CreateStockEntryCommand(
            UUID.randomUUID(),
            0,
            new BigDecimal("40.00"),
            UUID.randomUUID(),
            LocalDate.now()
        );

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }
}
