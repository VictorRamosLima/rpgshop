package rpgshop.presentation.controller.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rpgshop.application.command.product.ActivateProductCommand;
import rpgshop.application.command.product.CreateProductCommand;
import rpgshop.application.command.product.DeactivateProductCommand;
import rpgshop.application.command.product.ProductFilter;
import rpgshop.application.command.product.UpdateProductCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.product.CategoryGateway;
import rpgshop.application.gateway.product.PricingGroupGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.application.gateway.product.ProductTypeGateway;
import rpgshop.application.usecase.product.ActivateProductUseCase;
import rpgshop.application.usecase.product.AutoDeactivateProductsUseCase;
import rpgshop.application.usecase.product.CreateProductUseCase;
import rpgshop.application.usecase.product.DeactivateProductUseCase;
import rpgshop.application.usecase.product.UpdateProductUseCase;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public final class ProductController {
    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeactivateProductUseCase deactivateProductUseCase;
    private final ActivateProductUseCase activateProductUseCase;
    private final AutoDeactivateProductsUseCase autoDeactivateProductsUseCase;
    private final ProductGateway productGateway;
    private final ProductTypeGateway productTypeGateway;
    private final CategoryGateway categoryGateway;
    private final PricingGroupGateway pricingGroupGateway;

    public ProductController(
        final CreateProductUseCase createProductUseCase,
        final UpdateProductUseCase updateProductUseCase,
        final DeactivateProductUseCase deactivateProductUseCase,
        final ActivateProductUseCase activateProductUseCase,
        final AutoDeactivateProductsUseCase autoDeactivateProductsUseCase,
        final ProductGateway productGateway,
        final ProductTypeGateway productTypeGateway,
        final CategoryGateway categoryGateway,
        final PricingGroupGateway pricingGroupGateway
    ) {
        this.createProductUseCase = createProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deactivateProductUseCase = deactivateProductUseCase;
        this.activateProductUseCase = activateProductUseCase;
        this.autoDeactivateProductsUseCase = autoDeactivateProductsUseCase;
        this.productGateway = productGateway;
        this.productTypeGateway = productTypeGateway;
        this.categoryGateway = categoryGateway;
        this.pricingGroupGateway = pricingGroupGateway;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) final String name,
        @RequestParam(required = false) final UUID productTypeId,
        @RequestParam(required = false) final UUID categoryId,
        @RequestParam(required = false) final UUID pricingGroupId,
        @RequestParam(required = false) final String sku,
        @RequestParam(required = false) final String barcode,
        @RequestParam(required = false) final Boolean isActive,
        @RequestParam(required = false) final BigDecimal minPrice,
        @RequestParam(required = false) final BigDecimal maxPrice,
        @RequestParam(required = false) final Integer autoDeactivated,
        @RequestParam(defaultValue = "0") final int page,
        final Model model
    ) {
        final var filter = new ProductFilter(
            normalize(name),
            productTypeId,
            categoryId,
            pricingGroupId,
            normalize(sku),
            normalize(barcode),
            isActive,
            minPrice,
            maxPrice
        );
        final Page<Product> products = productGateway.findByFilters(filter, PageRequest.of(page, 10));

        loadReferenceData(model);
        model.addAttribute("products", products);

        if (autoDeactivated != null) {
            model.addAttribute("success", "Inativacao automatica executada. Produtos inativados: " + autoDeactivated);
        }

        return "product/list";
    }

    @GetMapping("/new")
    public String showCreateForm(final Model model) {
        loadReferenceData(model);
        return "product/create";
    }

    @PostMapping
    public String create(
        @RequestParam final String name,
        @RequestParam final UUID productTypeId,
        @RequestParam(required = false) final List<UUID> categoryIds,
        @RequestParam(required = false) final BigDecimal height,
        @RequestParam(required = false) final BigDecimal width,
        @RequestParam(required = false) final BigDecimal depth,
        @RequestParam final BigDecimal weight,
        @RequestParam final UUID pricingGroupId,
        @RequestParam(required = false) final String barcode,
        @RequestParam(required = false) final String sku,
        @RequestParam final BigDecimal costPrice,
        final Model model
    ) {
        try {
            final var command = new CreateProductCommand(
                name,
                productTypeId,
                categoryIds == null ? List.of() : categoryIds,
                height,
                width,
                depth,
                weight,
                pricingGroupId,
                normalize(barcode),
                normalize(sku),
                costPrice
            );
            final Product created = createProductUseCase.execute(command);
            return "redirect:/products/" + created.id();
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            loadReferenceData(model);
            return "product/create";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable final UUID id, final Model model) {
        final var product = productGateway.findById(id);
        if (product.isEmpty()) {
            return "redirect:/products";
        }
        model.addAttribute("product", product.get());
        model.addAttribute("statusChangeCategories", StatusChangeCategory.values());
        return "product/detail";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable final UUID id, final Model model) {
        final var product = productGateway.findById(id);
        if (product.isEmpty()) {
            return "redirect:/products";
        }

        model.addAttribute("product", product.get());
        model.addAttribute(
            "selectedCategoryIds",
            product.get().categories() == null
                ? List.<UUID>of()
                : product.get().categories().stream().map(category -> category.id()).toList()
        );
        loadReferenceData(model);
        return "product/edit";
    }

    @PostMapping("/{id}/update")
    public String update(
        @PathVariable final UUID id,
        @RequestParam final String name,
        @RequestParam final UUID productTypeId,
        @RequestParam(required = false) final List<UUID> categoryIds,
        @RequestParam(required = false) final BigDecimal height,
        @RequestParam(required = false) final BigDecimal width,
        @RequestParam(required = false) final BigDecimal depth,
        @RequestParam final BigDecimal weight,
        @RequestParam final UUID pricingGroupId,
        @RequestParam(required = false) final String barcode,
        @RequestParam(required = false) final String sku,
        @RequestParam final BigDecimal costPrice,
        @RequestParam(required = false) final BigDecimal salePrice,
        @RequestParam(defaultValue = "false") final boolean managerAuthorized,
        final Model model
    ) {
        try {
            final var command = new UpdateProductCommand(
                id,
                name,
                productTypeId,
                categoryIds == null ? List.of() : categoryIds,
                height,
                width,
                depth,
                weight,
                pricingGroupId,
                normalize(barcode),
                normalize(sku),
                costPrice,
                salePrice,
                managerAuthorized
            );
            updateProductUseCase.execute(command);
            return "redirect:/products/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return showEditForm(id, model);
        }
    }

    @PostMapping("/{id}/deactivate")
    public String deactivate(
        @PathVariable final UUID id,
        @RequestParam final String reason,
        @RequestParam final StatusChangeCategory category,
        final Model model
    ) {
        try {
            final var command = new DeactivateProductCommand(id, reason, category);
            deactivateProductUseCase.execute(command);
            return "redirect:/products/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/activate")
    public String activate(
        @PathVariable final UUID id,
        @RequestParam final String reason,
        @RequestParam final StatusChangeCategory category,
        final Model model
    ) {
        try {
            final var command = new ActivateProductCommand(id, reason, category);
            activateProductUseCase.execute(command);
            return "redirect:/products/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/auto-deactivate")
    public String autoDeactivate(
        @RequestParam final BigDecimal threshold,
        final Model model
    ) {
        try {
            final int deactivated = autoDeactivateProductsUseCase.execute(threshold);
            return "redirect:/products?autoDeactivated=" + deactivated;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return list(null, null, null, null, null, null, null, null, null, null, 0, model);
        }
    }

    private void loadReferenceData(final Model model) {
        model.addAttribute("productTypes", productTypeGateway.findActive());
        model.addAttribute("categories", categoryGateway.findActive());
        model.addAttribute("pricingGroups", pricingGroupGateway.findActive());
        model.addAttribute("statusChangeCategories", StatusChangeCategory.values());
    }

    private String normalize(final String value) {
        if (value == null) {
            return null;
        }

        final String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
