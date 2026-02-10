package rpgshop.presentation.controller.analysis;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rpgshop.application.command.analysis.SalesAnalysisFilter;
import rpgshop.application.usecase.analysis.SalesAnalysisUseCase;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;

import java.time.Instant;
import java.util.UUID;

@Controller
@RequestMapping("/analysis")
public final class AnalysisController {
    private final SalesAnalysisUseCase salesAnalysisUseCase;

    public AnalysisController(final SalesAnalysisUseCase salesAnalysisUseCase) {
        this.salesAnalysisUseCase = salesAnalysisUseCase;
    }

    @GetMapping
    public String dashboard(final Model model) {
        return "analysis/dashboard";
    }

    @GetMapping("/sales")
    public String salesInPeriod(
        @RequestParam(required = false) final String startDate,
        @RequestParam(required = false) final String endDate,
        @RequestParam(defaultValue = "0") final int page,
        final Model model
    ) {
        if (startDate != null && !startDate.isBlank() && endDate != null && !endDate.isBlank()) {
            final Instant start = Instant.parse(startDate + "T00:00:00Z");
            final Instant end = Instant.parse(endDate + "T23:59:59Z");
            final Page<Order> sales = salesAnalysisUseCase.findSalesInPeriod(start, end, PageRequest.of(page, 10));
            model.addAttribute("sales", sales);
        }
        return "analysis/sales";
    }

    @GetMapping("/by-product")
    public String byProduct(
        @RequestParam(required = false) final UUID productId,
        @RequestParam(required = false) final String startDate,
        @RequestParam(required = false) final String endDate,
        final Model model
    ) {
        if (productId != null && startDate != null && !startDate.isBlank() && endDate != null && !endDate.isBlank()) {
            final Instant start = Instant.parse(startDate + "T00:00:00Z");
            final Instant end = Instant.parse(endDate + "T23:59:59Z");
            final var filter = new SalesAnalysisFilter(productId, null, start, end);
            final var items = salesAnalysisUseCase.findByProductAndPeriod(filter, PageRequest.of(0, 100));
            final long totalSold = salesAnalysisUseCase.sumQuantitySoldByProductInPeriod(filter);
            model.addAttribute("items", items);
            model.addAttribute("totalSold", totalSold);
        }
        return "analysis/by-product";
    }

    @GetMapping("/by-category")
    public String byCategory(
        @RequestParam(required = false) final UUID categoryId,
        @RequestParam(required = false) final String startDate,
        @RequestParam(required = false) final String endDate,
        Model model
    ) {
        if (categoryId != null && startDate != null && !startDate.isBlank() && endDate != null && !endDate.isBlank()) {
            final Instant start = Instant.parse(startDate + "T00:00:00Z");
            final Instant end = Instant.parse(endDate + "T23:59:59Z");
            final var filter = new SalesAnalysisFilter(null, categoryId, start, end);
            final var items = salesAnalysisUseCase.findByCategoryAndPeriod(filter, PageRequest.of(0, 100));
            model.addAttribute("items", items);
        }
        return "analysis/by-category";
    }
}
