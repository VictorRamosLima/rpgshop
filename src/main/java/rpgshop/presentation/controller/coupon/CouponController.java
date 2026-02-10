package rpgshop.presentation.controller.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rpgshop.application.command.coupon.CreateCouponCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.usecase.coupon.CreateCouponUseCase;
import rpgshop.application.usecase.coupon.QueryCouponsUseCase;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.coupon.constant.CouponType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Controller
@RequestMapping("/coupons")
public final class CouponController {
    private final CreateCouponUseCase createCouponUseCase;
    private final QueryCouponsUseCase queryCouponsUseCase;

    public CouponController(
        final CreateCouponUseCase createCouponUseCase,
        final QueryCouponsUseCase queryCouponsUseCase
    ) {
        this.createCouponUseCase = createCouponUseCase;
        this.queryCouponsUseCase = queryCouponsUseCase;
    }

    @GetMapping("/customer/{customerId}")
    public String listByCustomer(
        @PathVariable final UUID customerId,
        @RequestParam(defaultValue = "0") final int page,
        Model model
    ) {
        final Page<Coupon> coupons = queryCouponsUseCase.findByCustomerId(customerId, PageRequest.of(page, 10));
        model.addAttribute("coupons", coupons);
        model.addAttribute("customerId", customerId);
        model.addAttribute("types", CouponType.values());
        return "coupon/list";
    }

    @GetMapping("/new")
    public String showCreateForm(final Model model) {
        model.addAttribute("types", CouponType.values());
        return "coupon/create";
    }

    @PostMapping
    public String create(
        @RequestParam final String code,
        @RequestParam final CouponType type,
        @RequestParam final BigDecimal value,
        @RequestParam(required = false) final UUID customerId,
        @RequestParam(required = false) final String expiresAt,
        final Model model
    ) {
        try {
            final Instant expiration = expiresAt != null && !expiresAt.isBlank()
                ? Instant.parse(expiresAt + "T23:59:59Z")
                : null;
            final var command = new CreateCouponCommand(code, type, value, customerId, expiration);
            createCouponUseCase.execute(command);
            if (customerId != null) {
                return "redirect:/coupons/customer/" + customerId;
            }
            return "redirect:/coupons/new";
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("types", CouponType.values());
            return "coupon/create";
        }
    }
}
