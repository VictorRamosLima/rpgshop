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

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_UP;

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
        if (productId == null) {
            throw new BusinessRuleException("O produto e obrigatorio");
        }
        if (quantity <= 0) {
            throw new BusinessRuleException("A quantidade deve ser maior que zero");
        }
        if (costValue == null || costValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("O valor de custo deve ser maior que zero");
        }
        if (supplierId == null) {
            throw new BusinessRuleException("O fornecedor e obrigatorio");
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
        final BigDecimal maxCostValue = stockEntryGateway.findMaxCostValueByProductId(product.id()).orElse(costValue);
        final BigDecimal margin = product.pricingGroup().marginPercentage();
        final BigDecimal salePrice = maxCostValue
            .multiply(ONE.add(margin.divide(new BigDecimal("100"), 4, HALF_UP)))
            .setScale(2, HALF_UP);

        final Product updated = product.toBuilder()
            .stockQuantity(totalStock)
            .costPrice(maxCostValue)
            .salePrice(salePrice)
            .build();
        productGateway.save(updated);

        return saved;
    }
}
