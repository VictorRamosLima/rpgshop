package rpgshop.application.usecase.product;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.product.CreateProductCommand;
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
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import static java.math.RoundingMode.HALF_UP;

@Service
public class CreateProductUseCase {
    private final ProductGateway productGateway;
    private final ProductTypeGateway productTypeGateway;
    private final CategoryGateway categoryGateway;
    private final PricingGroupGateway pricingGroupGateway;

    public CreateProductUseCase(
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
    public Product execute(@Nonnull final CreateProductCommand command) {
        validateRequiredFields(command);
        validateUniqueIdentifiers(command);

        ProductType productType = productTypeGateway
            .findById(command.productTypeId())
            .orElseThrow(() -> new EntityNotFoundException("ProductType", command.productTypeId()));

        List<Category> categories = resolveCategories(command.categoryIds());

        PricingGroup pricingGroup = pricingGroupGateway
            .findById(command.pricingGroupId())
            .orElseThrow(() -> new EntityNotFoundException("PricingGroup", command.pricingGroupId()));

        final BigDecimal salePrice = calculateSalePrice(
            command.costPrice(),
            pricingGroup.marginPercentage()
        );

        Product product = Product.builder()
            .id(UUID.randomUUID())
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
            .stockQuantity(0)
            .statusChanges(List.of())
            .build();

        return productGateway.save(product);
    }

    private void validateRequiredFields(CreateProductCommand command) {
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
        if ((command.barcode() == null || command.barcode().isBlank())
                && (command.sku() == null || command.sku().isBlank())) {
            throw new BusinessRuleException("Pelo menos um identificador e obrigatorio: codigo de barras ou SKU");
        }
    }

    private void validateUniqueIdentifiers(final CreateProductCommand command) {
        if (command.sku() != null && !command.sku().isBlank() && productGateway.existsBySku(command.sku())) {
            throw new BusinessRuleException("Ja existe um produto com o SKU '%s'".formatted(command.sku()));
        }
        if (command.barcode() != null && !command.barcode().isBlank() && productGateway.existsByBarcode(command.barcode())) {
            throw new BusinessRuleException("Ja existe um produto com o codigo de barras '%s'".formatted(command.barcode()));
        }
    }

    private List<Category> resolveCategories(List<UUID> categoryIds) {
        List<Category> categories = categoryGateway.findAllByIds(categoryIds);
        if (categories.size() != categoryIds.size()) {
            throw new EntityNotFoundException("One or more categories were not found");
        }
        return categories;
    }

    private BigDecimal calculateSalePrice(BigDecimal costPrice, BigDecimal marginPercentage) {
        final BigDecimal division = marginPercentage.divide(new BigDecimal("100"), 4, HALF_UP);
        final BigDecimal multiplier = BigDecimal.ONE.add(division);

        return costPrice.multiply(multiplier).setScale(2, HALF_UP);
    }
}
