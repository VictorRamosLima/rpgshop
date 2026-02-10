package rpgshop.application.usecase.product;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.product.UpdateProductCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.product.CategoryGateway;
import rpgshop.application.gateway.product.PricingGroupGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.application.gateway.product.ProductTypeGateway;
import rpgshop.domain.entity.product.Category;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.ProductType;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.HALF_UP;

@Service
public class UpdateProductUseCase {
    private final ProductGateway productGateway;
    private final ProductTypeGateway productTypeGateway;
    private final CategoryGateway categoryGateway;
    private final PricingGroupGateway pricingGroupGateway;

    public UpdateProductUseCase(
        final ProductGateway productGateway,
        final ProductTypeGateway productTypeGateway,
        final CategoryGateway categoryGateway,
        final PricingGroupGateway pricingGroupGateway
    ) {
        this.productGateway = productGateway;
        this.productTypeGateway = productTypeGateway;
        this.categoryGateway = categoryGateway;
        this.pricingGroupGateway = pricingGroupGateway;
    }

    @Nonnull
    @Transactional
    public Product execute(@Nonnull final UpdateProductCommand command) {
        validateRequiredFields(command);

        Product existing = productGateway
            .findById(command.id())
            .orElseThrow(() -> new EntityNotFoundException("Product", command.id()));

        ProductType productType = productTypeGateway
            .findById(command.productTypeId())
            .orElseThrow(() -> new EntityNotFoundException("ProductType", command.productTypeId()));

        List<Category> categories = categoryGateway.findAllByIds(command.categoryIds());
        if (categories.size() != command.categoryIds().size()) {
            throw new EntityNotFoundException("One or more categories were not found");
        }

        PricingGroup pricingGroup = pricingGroupGateway
            .findById(command.pricingGroupId())
            .orElseThrow(() -> new EntityNotFoundException("PricingGroup", command.pricingGroupId()));

        validateUniqueIdentifiers(command, existing);

        BigDecimal salePrice = resolveSalePrice(command, pricingGroup);

        final Product updated = existing.toBuilder()
            .name(command.name())
            .productType(productType)
            .categories(categories)
            .height(command.height())
            .width(command.width())
            .depth(command.depth())
            .weight(command.weight())
            .pricingGroup(pricingGroup)
            .barcode(command.barcode())
            .sku(command.sku())
            .salePrice(salePrice)
            .costPrice(command.costPrice())
            .build();

        return productGateway.save(updated);
    }

    private void validateRequiredFields(final UpdateProductCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            throw new BusinessRuleException("O nome do produto e obrigatorio");
        }
        if (command.productTypeId() == null) {
            throw new BusinessRuleException("O tipo de produto e obrigatorio");
        }
        if (command.categoryIds() == null || command.categoryIds().isEmpty()) {
            throw new BusinessRuleException("Pelo menos uma categoria e obrigatoria");
        }
        if (command.pricingGroupId() == null) {
            throw new BusinessRuleException("O grupo de precificacao e obrigatorio");
        }
        if (command.costPrice() == null || command.costPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("O preco de custo deve ser maior que zero");
        }
        if (command.weight() == null || command.weight().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("O peso e obrigatorio e deve ser maior que zero");
        }
        if (command.height() == null || command.height().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("A altura e obrigatoria e deve ser maior que zero");
        }
        if (command.width() == null || command.width().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("A largura e obrigatoria e deve ser maior que zero");
        }
        if (command.depth() == null || command.depth().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("A profundidade e obrigatoria e deve ser maior que zero");
        }
        if ((command.barcode() == null || command.barcode().isBlank())
            && (command.sku() == null || command.sku().isBlank())
        ) {
            throw new BusinessRuleException("Pelo menos um identificador e obrigatorio: codigo de barras ou SKU");
        }
    }

    private void validateUniqueIdentifiers(final UpdateProductCommand command, final Product existing) {
        if (command.sku() != null && !command.sku().isBlank()
            && !command.sku().equals(existing.sku())
            && productGateway.existsBySku(command.sku())
        ) {
            throw new BusinessRuleException("Ja existe um produto com o SKU '%s'".formatted(command.sku()));
        }
        if (command.barcode() != null && !command.barcode().isBlank()
            && !command.barcode().equals(existing.barcode())
            && productGateway.existsByBarcode(command.barcode())
        ) {
            throw new BusinessRuleException("Ja existe um produto com o codigo de barras '%s'".formatted(command.barcode()));
        }
    }

    private BigDecimal resolveSalePrice(final UpdateProductCommand command, final PricingGroup pricingGroup) {
        final BigDecimal minimumSalePrice = calculateMinimumSalePrice(
            command.costPrice(),
            pricingGroup.marginPercentage()
        );

        if (command.salePrice() != null) {
            if (command.salePrice().compareTo(minimumSalePrice) < 0 && !command.managerAuthorized()) {
                throw new BusinessRuleException(
                        "Sale price R$ %s is below the minimum margin (R$ %s). Manager authorization is required"
                                .formatted(command.salePrice(), minimumSalePrice));
            }
            return command.salePrice();
        }

        return minimumSalePrice;
    }

    private BigDecimal calculateMinimumSalePrice(final BigDecimal costPrice, final BigDecimal marginPercentage) {
        final BigDecimal division = marginPercentage.divide(new BigDecimal("100"), 4, HALF_UP);
        final BigDecimal multiplier = ONE.add(division);

        return costPrice.multiply(multiplier).setScale(2, HALF_UP);
    }
}
