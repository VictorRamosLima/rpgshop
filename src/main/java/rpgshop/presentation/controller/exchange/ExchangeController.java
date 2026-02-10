package rpgshop.presentation.controller.exchange;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rpgshop.application.command.exchange.ReceiveExchangeItemsCommand;
import rpgshop.application.command.exchange.RequestExchangeCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.order.OrderItemGateway;
import rpgshop.application.gateway.supplier.SupplierGateway;
import rpgshop.application.usecase.exchange.AuthorizeExchangeUseCase;
import rpgshop.application.usecase.exchange.DenyExchangeUseCase;
import rpgshop.application.usecase.exchange.QueryExchangesUseCase;
import rpgshop.application.usecase.exchange.ReceiveExchangeItemsUseCase;
import rpgshop.application.usecase.exchange.RequestExchangeUseCase;
import rpgshop.application.usecase.order.QueryOrdersUseCase;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/exchanges")
public final class ExchangeController {
    private final RequestExchangeUseCase requestExchangeUseCase;
    private final AuthorizeExchangeUseCase authorizeExchangeUseCase;
    private final DenyExchangeUseCase denyExchangeUseCase;
    private final ReceiveExchangeItemsUseCase receiveExchangeItemsUseCase;
    private final QueryExchangesUseCase queryExchangesUseCase;
    private final QueryOrdersUseCase queryOrdersUseCase;
    private final OrderItemGateway orderItemGateway;
    private final SupplierGateway supplierGateway;

    public ExchangeController(
        final RequestExchangeUseCase requestExchangeUseCase,
        final AuthorizeExchangeUseCase authorizeExchangeUseCase,
        final DenyExchangeUseCase denyExchangeUseCase,
        final ReceiveExchangeItemsUseCase receiveExchangeItemsUseCase,
        final QueryExchangesUseCase queryExchangesUseCase,
        final QueryOrdersUseCase queryOrdersUseCase,
        final OrderItemGateway orderItemGateway,
        final SupplierGateway supplierGateway
    ) {
        this.requestExchangeUseCase = requestExchangeUseCase;
        this.authorizeExchangeUseCase = authorizeExchangeUseCase;
        this.denyExchangeUseCase = denyExchangeUseCase;
        this.receiveExchangeItemsUseCase = receiveExchangeItemsUseCase;
        this.queryExchangesUseCase = queryExchangesUseCase;
        this.queryOrdersUseCase = queryOrdersUseCase;
        this.orderItemGateway = orderItemGateway;
        this.supplierGateway = supplierGateway;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) final ExchangeStatus status,
        @RequestParam(defaultValue = "0") final int page,
        Model model
    ) {
        final Page<ExchangeRequest> exchanges = status != null
            ? queryExchangesUseCase.findByStatus(status, PageRequest.of(page, 10))
            : queryExchangesUseCase.findAll(PageRequest.of(page, 10));

        model.addAttribute("exchanges", exchanges);
        model.addAttribute("statuses", ExchangeStatus.values());
        return "exchange/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable final UUID id, final Model model) {
        final var exchange = queryExchangesUseCase.findById(id);
        if (exchange.isEmpty()) {
            return "redirect:/exchanges";
        }
        model.addAttribute("exchange", exchange.get());
        model.addAttribute("activeSuppliers", supplierGateway.findActiveSuppliers(PageRequest.of(0, 100)).getContent());
        return "exchange/detail";
    }

    @GetMapping("/new")
    public String showRequestForm(
        @RequestParam final UUID orderId,
        final Model model
    ) {
        final var order = queryOrdersUseCase.findById(orderId);
        if (order.isEmpty()) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order.get());
        model.addAttribute("orderId", orderId);
        model.addAttribute("orderItems", orderItemGateway.findDeliveredItemsByOrderId(orderId));
        return "exchange/request";
    }

    @PostMapping
    public String request(
        @RequestParam final UUID orderId,
        @RequestParam final UUID orderItemId,
        @RequestParam final int quantity,
        @RequestParam final String reason,
        final Model model
    ) {
        try {
            final var command = new RequestExchangeCommand(orderId, orderItemId, quantity, reason);
            final ExchangeRequest exchange = requestExchangeUseCase.execute(command);
            return "redirect:/exchanges/" + exchange.id();
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("orderId", orderId);
            model.addAttribute("orderItems", orderItemGateway.findDeliveredItemsByOrderId(orderId));
            return "exchange/request";
        }
    }

    @PostMapping("/{id}/authorize")
    public String authorize(@PathVariable final UUID id, final Model model) {
        try {
            authorizeExchangeUseCase.execute(id);
            return "redirect:/exchanges/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/deny")
    public String deny(@PathVariable final UUID id, final Model model) {
        try {
            denyExchangeUseCase.execute(id);
            return "redirect:/exchanges/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/receive")
    public String receive(
        @PathVariable final UUID id,
        @RequestParam(defaultValue = "false") final boolean returnToStock,
        @RequestParam(required = false) final UUID supplierId,
        @RequestParam(required = false) final BigDecimal costValue,
        final Model model
    ) {
        try {
            final var command = new ReceiveExchangeItemsCommand(id, returnToStock, supplierId, costValue);
            receiveExchangeItemsUseCase.execute(command);
            return "redirect:/exchanges/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }
}
