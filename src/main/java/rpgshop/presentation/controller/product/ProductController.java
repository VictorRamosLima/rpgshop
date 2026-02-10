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
import rpgshop.application.command.product.ProductFilter;
import rpgshop.application.command.product.UpdateProductCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.product.CategoryGateway;
import rpgshop.application.gateway.product.PricingGroupGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.application.gateway.product.ProductTypeGateway;
import rpgshop.application.usecase.product.ActivateProductUseCase;
import rpgshop.application.usecase.product.CreateProductUseCase;
import rpgshop.application.usecase.product.UpdateProductUseCase;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final ActivateProductUseCase activateProductUseCase;
    private final ProductGateway productGateway;

    public ProductController(
        final CreateProductUseCase createProductUseCase,
        final UpdateProductUseCase updateProductUseCase,
        final ActivateProductUseCase activateProductUseCase,
        final ProductGateway productGateway
    ) {
        this.createProductUseCase = createProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.activateProductUseCase = activateProductUseCase;
        this.productGateway = productGateway;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Boolean isActive,
        @RequestParam(required = false) BigDecimal minPrice,
        @RequestParam(required = false) BigDecimal maxPrice,
        @RequestParam(defaultValue = "0") int page,
        Model model
    ) {
        final var filter = new ProductFilter(
            name, null, null, null, null, null, isActive, minPrice, maxPrice
        );
        final Page<Product> products = productGateway.findByFilters(filter, PageRequest.of(page, 10));
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        final var product = productGateway.findById(id);
        if (product.isEmpty()) {
            return "redirect:/products";
        }
        model.addAttribute("product", product.get());
        model.addAttribute("statusChangeCategories", StatusChangeCategory.values());
        return "product/detail";
    }

    @PostMapping("/{id}/activate")
    public String activate(
        @PathVariable UUID id,
        @RequestParam String reason,
        @RequestParam StatusChangeCategory category,
        Model model
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
}
