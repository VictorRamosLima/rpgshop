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
import rpgshop.application.usecase.exchange.AuthorizeExchangeUseCase;
import rpgshop.application.usecase.exchange.DenyExchangeUseCase;
import rpgshop.application.usecase.exchange.QueryExchangesUseCase;
import rpgshop.application.usecase.exchange.ReceiveExchangeItemsUseCase;
import rpgshop.application.usecase.exchange.RequestExchangeUseCase;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;

import java.util.UUID;

@Controller
@RequestMapping("/exchanges")
public class ExchangeController {
    private final RequestExchangeUseCase requestExchangeUseCase;
    private final AuthorizeExchangeUseCase authorizeExchangeUseCase;
    private final DenyExchangeUseCase denyExchangeUseCase;
    private final ReceiveExchangeItemsUseCase receiveExchangeItemsUseCase;
    private final QueryExchangesUseCase queryExchangesUseCase;

    public ExchangeController(
        final RequestExchangeUseCase requestExchangeUseCase,
        final AuthorizeExchangeUseCase authorizeExchangeUseCase,
        final DenyExchangeUseCase denyExchangeUseCase,
        final ReceiveExchangeItemsUseCase receiveExchangeItemsUseCase,
        final QueryExchangesUseCase queryExchangesUseCase
    ) {
        this.requestExchangeUseCase = requestExchangeUseCase;
        this.authorizeExchangeUseCase = authorizeExchangeUseCase;
        this.denyExchangeUseCase = denyExchangeUseCase;
        this.receiveExchangeItemsUseCase = receiveExchangeItemsUseCase;
        this.queryExchangesUseCase = queryExchangesUseCase;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) ExchangeStatus status,
        @RequestParam(defaultValue = "0") int page,
        Model model
    ) {
        Page<ExchangeRequest> exchanges;
        if (status != null) {
            exchanges = queryExchangesUseCase.findByStatus(status, PageRequest.of(page, 10));
        } else {
            exchanges = queryExchangesUseCase.findByStatus(null, PageRequest.of(page, 10));
        }
        model.addAttribute("exchanges", exchanges);
        model.addAttribute("statuses", ExchangeStatus.values());
        return "exchange/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        final var exchange = queryExchangesUseCase.findById(id);
        if (exchange.isEmpty()) {
            return "redirect:/exchanges";
        }
        model.addAttribute("exchange", exchange.get());
        return "exchange/detail";
    }

    @GetMapping("/new")
    public String showRequestForm(
        @RequestParam UUID orderId,
        Model model
    ) {
        model.addAttribute("orderId", orderId);
        return "exchange/request";
    }

    @PostMapping
    public String request(
        @RequestParam UUID orderId,
        @RequestParam UUID orderItemId,
        @RequestParam int quantity,
        @RequestParam String reason,
        Model model
    ) {
        try {
            final var command = new RequestExchangeCommand(orderId, orderItemId, quantity, reason);
            final ExchangeRequest exchange = requestExchangeUseCase.execute(command);
            return "redirect:/exchanges/" + exchange.id();
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("orderId", orderId);
            return "exchange/request";
        }
    }

    @PostMapping("/{id}/authorize")
    public String authorize(@PathVariable UUID id, Model model) {
        try {
            authorizeExchangeUseCase.execute(id);
            return "redirect:/exchanges/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/deny")
    public String deny(@PathVariable UUID id, Model model) {
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
        @PathVariable UUID id,
        @RequestParam(defaultValue = "false") boolean returnToStock,
        Model model
    ) {
        try {
            final var command = new ReceiveExchangeItemsCommand(id, returnToStock);
            receiveExchangeItemsUseCase.execute(command);
            return "redirect:/exchanges/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }
}
