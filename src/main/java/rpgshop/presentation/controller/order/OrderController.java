package rpgshop.presentation.controller.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rpgshop.application.command.order.CreateOrderCommand;
import rpgshop.application.command.order.PaymentInfo;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.usecase.order.ApproveOrderUseCase;
import rpgshop.application.usecase.order.CreateOrderUseCase;
import rpgshop.application.usecase.order.DeliverOrderUseCase;
import rpgshop.application.usecase.order.DispatchOrderUseCase;
import rpgshop.application.usecase.order.QueryOrdersUseCase;
import rpgshop.application.usecase.order.RejectOrderUseCase;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final ApproveOrderUseCase approveOrderUseCase;
    private final RejectOrderUseCase rejectOrderUseCase;
    private final DispatchOrderUseCase dispatchOrderUseCase;
    private final DeliverOrderUseCase deliverOrderUseCase;
    private final QueryOrdersUseCase queryOrdersUseCase;

    public OrderController(
        final CreateOrderUseCase createOrderUseCase,
        final ApproveOrderUseCase approveOrderUseCase,
        final RejectOrderUseCase rejectOrderUseCase,
        final DispatchOrderUseCase dispatchOrderUseCase,
        final DeliverOrderUseCase deliverOrderUseCase,
        final QueryOrdersUseCase queryOrdersUseCase
    ) {
        this.createOrderUseCase = createOrderUseCase;
        this.approveOrderUseCase = approveOrderUseCase;
        this.rejectOrderUseCase = rejectOrderUseCase;
        this.dispatchOrderUseCase = dispatchOrderUseCase;
        this.deliverOrderUseCase = deliverOrderUseCase;
        this.queryOrdersUseCase = queryOrdersUseCase;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) OrderStatus status,
        @RequestParam(defaultValue = "0") int page,
        Model model
    ) {
        Page<Order> orders;
        if (status != null) {
            orders = queryOrdersUseCase.findByStatus(status, PageRequest.of(page, 10));
        } else {
            orders = queryOrdersUseCase.findByStatus(null, PageRequest.of(page, 10));
        }
        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values());
        return "order/list";
    }

    @GetMapping("/customer/{customerId}")
    public String listByCustomer(
        @PathVariable UUID customerId,
        @RequestParam(defaultValue = "0") int page,
        Model model
    ) {
        final Page<Order> orders = queryOrdersUseCase.findByCustomerId(customerId, PageRequest.of(page, 10));
        model.addAttribute("orders", orders);
        model.addAttribute("customerId", customerId);
        model.addAttribute("statuses", OrderStatus.values());
        return "order/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        final var order = queryOrdersUseCase.findById(id);
        if (order.isEmpty()) {
            return "redirect:/orders";
        }
        model.addAttribute("order", order.get());
        return "order/detail";
    }

    @GetMapping("/checkout/{customerId}")
    public String showCheckout(@PathVariable UUID customerId, Model model) {
        model.addAttribute("customerId", customerId);
        return "order/checkout";
    }

    @PostMapping("/checkout")
    public String checkout(
        @RequestParam UUID customerId,
        @RequestParam UUID deliveryAddressId,
        @RequestParam UUID creditCardId,
        @RequestParam BigDecimal amount,
        @RequestParam(required = false) UUID couponId,
        Model model
    ) {
        try {
            final List<PaymentInfo> payments = new ArrayList<>();
            payments.add(new PaymentInfo(creditCardId, couponId, amount));

            final var command = new CreateOrderCommand(customerId, deliveryAddressId, payments);
            final Order order = createOrderUseCase.execute(command);
            return "redirect:/orders/" + order.id();
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("customerId", customerId);
            return "order/checkout";
        }
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable UUID id, Model model) {
        try {
            approveOrderUseCase.execute(id);
            return "redirect:/orders/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable UUID id, Model model) {
        try {
            rejectOrderUseCase.execute(id);
            return "redirect:/orders/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/dispatch")
    public String dispatch(@PathVariable UUID id, Model model) {
        try {
            dispatchOrderUseCase.execute(id);
            return "redirect:/orders/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/deliver")
    public String deliver(@PathVariable UUID id, Model model) {
        try {
            deliverOrderUseCase.execute(id);
            return "redirect:/orders/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }
}
