package rpgshop.application.usecase.stock;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.stock.CreateStockEntryCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.application.gateway.stock.StockEntryGateway;
import rpgshop.application.gateway.supplier.SupplierGateway;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.stock.StockEntry;
import rpgshop.domain.entity.supplier.Supplier;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_UP;

@Service
public class CreateStockEntryUseCase {
    private final StockEntryGateway stockEntryGateway;
    private final ProductGateway productGateway;
    private final SupplierGateway supplierGateway;

    public CreateStockEntryUseCase(
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
    public StockEntry execute(@Nonnull final CreateStockEntryCommand command) {
        validateRequiredFields(command);

        final Product product = productGateway.findById(command.productId())
            .orElseThrow(() -> new EntityNotFoundException("Product", command.productId()));

        final Supplier supplier = supplierGateway.findById(command.supplierId())
            .orElseThrow(() -> new EntityNotFoundException("Supplier", command.supplierId()));

        final StockEntry entry = StockEntry.builder()
            .id(UUID.randomUUID())
            .product(product)
            .quantity(command.quantity())
            .costValue(command.costValue())
            .supplier(supplier)
            .entryDate(command.entryDate())
            .isReentry(false)
            .build();

        final StockEntry saved = stockEntryGateway.save(entry);

        updateProductStockAndPrice(product, command.costValue());

        return saved;
    }

    private void validateRequiredFields(final CreateStockEntryCommand command) {
        if (command.productId() == null) {
            throw new BusinessRuleException("O produto e obrigatorio");
        }
        if (command.quantity() == null || command.quantity() <= 0) {
            throw new BusinessRuleException("A quantidade deve ser maior que zero");
        }
        if (command.costValue() == null || command.costValue().compareTo(ZERO) <= 0) {
            throw new BusinessRuleException("O valor de custo deve ser maior que zero");
        }
        if (command.supplierId() == null) {
            throw new BusinessRuleException("O fornecedor e obrigatorio");
        }
        if (command.entryDate() == null) {
            throw new BusinessRuleException("A data de entrada e obrigatoria");
        }
    }

    private void updateProductStockAndPrice(final Product product, final BigDecimal newCostValue) {
        final int totalStock = stockEntryGateway.sumQuantityByProductId(product.id());

        final Optional<BigDecimal> maxCost = stockEntryGateway.findMaxCostValueByProductId(product.id());
        final BigDecimal costPrice = maxCost.orElse(newCostValue);

        final BigDecimal marginPercentage = product.pricingGroup().marginPercentage();
        final BigDecimal division = marginPercentage.divide(new BigDecimal("100"), 4, HALF_UP);
        final BigDecimal multiplier = ONE.add(division);
        final BigDecimal salePrice = costPrice.multiply(multiplier).setScale(2, HALF_UP);

        final Product updated = product.toBuilder()
            .stockQuantity(totalStock)
            .costPrice(costPrice)
            .salePrice(salePrice)
            .build();

        productGateway.save(updated);
    }
}
