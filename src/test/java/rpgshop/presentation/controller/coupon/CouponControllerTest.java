package rpgshop.presentation.controller.coupon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import rpgshop.application.command.coupon.CreateCouponCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.usecase.coupon.CreateCouponUseCase;
import rpgshop.application.usecase.coupon.QueryCouponsUseCase;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.coupon.constant.CouponType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponControllerTest {
    @Mock
    private CreateCouponUseCase createCouponUseCase;
    @Mock
    private QueryCouponsUseCase queryCouponsUseCase;
    @Mock
    private Model model;

    @InjectMocks
    private CouponController controller;

    @Test
    void shouldListCouponsByCustomer() {
        final UUID customerId = UUID.randomUUID();
        final Page<Coupon> coupons = new PageImpl<>(List.of(Coupon.builder().id(UUID.randomUUID()).build()));
        when(queryCouponsUseCase.findByCustomerId(customerId, PageRequest.of(1, 10))).thenReturn(coupons);

        final String view = controller.listByCustomer(customerId, 1, model);

        assertEquals("coupon/list", view);
        verify(queryCouponsUseCase).findByCustomerId(customerId, PageRequest.of(1, 10));
        verify(model).addAttribute("coupons", coupons);
        verify(model).addAttribute("customerId", customerId);
        verify(model).addAttribute(eq("types"), any());
    }

    @Test
    void shouldCreateCouponAndRedirectToCustomerListWhenCustomerIsProvided() {
        final UUID customerId = UUID.randomUUID();

        final String view = controller.create(
            "CPN-10",
            CouponType.EXCHANGE,
            new BigDecimal("10.00"),
            customerId,
            "2026-01-31",
            model
        );

        assertEquals("redirect:/coupons/customer/" + customerId, view);
        final ArgumentCaptor<CreateCouponCommand> captor = ArgumentCaptor.forClass(CreateCouponCommand.class);
        verify(createCouponUseCase).execute(captor.capture());
        assertEquals("CPN-10", captor.getValue().code());
        assertEquals(Instant.parse("2026-01-31T23:59:59Z"), captor.getValue().expiresAt());
    }

    @Test
    void shouldRenderCreateViewWithErrorWhenCreateFails() {
        when(createCouponUseCase.execute(any())).thenThrow(new BusinessRuleException("cupom invalido"));

        final String view = controller.create(
            "CPN-10",
            CouponType.EXCHANGE,
            new BigDecimal("10.00"),
            null,
            null,
            model
        );

        assertEquals("coupon/create", view);
        verify(model).addAttribute("error", "cupom invalido");
        verify(model).addAttribute(eq("types"), any());
    }
}
