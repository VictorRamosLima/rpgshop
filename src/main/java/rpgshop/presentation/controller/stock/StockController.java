package rpgshop.presentation.controller.stock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rpgshop.application.command.stock.CreateStockEntryCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.stock.StockEntryGateway;
import rpgshop.application.usecase.stock.CreateStockEntryUseCase;
import rpgshop.domain.entity.stock.StockEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping("/stock")
public final class StockController {
    private final CreateStockEntryUseCase createStockEntryUseCase;
    private final StockEntryGateway stockEntryGateway;

    public StockController(
        final CreateStockEntryUseCase createStockEntryUseCase,
        final StockEntryGateway stockEntryGateway
    ) {
        this.createStockEntryUseCase = createStockEntryUseCase;
        this.stockEntryGateway = stockEntryGateway;
    }

    @GetMapping("/product/{productId}")
    public String listByProduct(
        @PathVariable final UUID productId,
        @RequestParam(defaultValue = "0") final int page,
        Model model
    ) {
        final Page<StockEntry> entries = stockEntryGateway.findByProductId(productId, PageRequest.of(page, 10));
        model.addAttribute("entries", entries);
        model.addAttribute("productId", productId);
        return "stock/list";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) final UUID productId, final Model model) {
        model.addAttribute("productId", productId);
        return "stock/create";
    }

    @PostMapping
    public String create(
        @RequestParam final UUID productId,
        @RequestParam final Integer quantity,
        @RequestParam final BigDecimal costValue,
        @RequestParam final UUID supplierId,
        @RequestParam final String entryDate,
        Model model
    ) {
        try {
            final var command = new CreateStockEntryCommand(
                productId,
                quantity,
                costValue,
                supplierId,
                LocalDate.parse(entryDate)
            );
            createStockEntryUseCase.execute(command);
            return "redirect:/stock/product/" + productId;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("productId", productId);
            return "stock/create";
        }
    }
}
