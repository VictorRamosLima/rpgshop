package rpgshop.application.usecase.stock;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.application.gateway.stock.StockEntryGateway;
import rpgshop.application.gateway.supplier.SupplierGateway;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.stock.StockEntry;
import rpgshop.domain.entity.supplier.Supplier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class CreateStockReentryUseCase {
    private final StockEntryGateway stockEntryGateway;
    private final ProductGateway productGateway;
    private final SupplierGateway supplierGateway;

    public CreateStockReentryUseCase(
        final StockEntryGateway stockEntryGateway,
        final ProductGateway productGateway,
        final SupplierGateway supplierGateway
    ) {
        this.stockEntryGateway = stockEntryGateway;
        this.productGateway = productGateway;
        this.supplierGateway = supplierGateway;
    }

    @Nonnull
    @Transactional
    public StockEntry execute(
        @Nonnull final UUID productId,
        final int quantity,
        @Nonnull final BigDecimal costValue,
        @Nonnull final UUID supplierId
    ) {
        if (quantity <= 0) {
            throw new BusinessRuleException("Quantity must be greater than zero");
        }

        final Product product = productGateway.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product", productId));

        final Supplier supplier = supplierGateway.findById(supplierId)
            .orElseThrow(() -> new EntityNotFoundException("Supplier", supplierId));

        final StockEntry entry = StockEntry.builder()
            .id(UUID.randomUUID())
            .product(product)
            .quantity(quantity)
            .costValue(costValue)
            .supplier(supplier)
            .entryDate(LocalDate.now())
            .isReentry(true)
            .build();

        final StockEntry saved = stockEntryGateway.save(entry);

        final int totalStock = stockEntryGateway.sumQuantityByProductId(product.id());
        final Product updated = product.toBuilder()
            .stockQuantity(totalStock)
            .build();
        productGateway.save(updated);

        return saved;
    }
}
