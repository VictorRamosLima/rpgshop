package rpgshop.application.usecase.coupon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rpgshop.application.gateway.coupon.CouponGateway;
import rpgshop.domain.entity.coupon.Coupon;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryCouponsUseCaseTest {
    @Mock
    private CouponGateway couponGateway;

    @InjectMocks
    private QueryCouponsUseCase useCase;

    @Test
    void shouldFindCouponsByCustomerId() {
        final UUID customerId = UUID.randomUUID();
        final Pageable pageable = PageRequest.of(0, 10);
        final Coupon coupon = Coupon.builder().id(UUID.randomUUID()).code("CP1").build();
        final Page<Coupon> expected = new PageImpl<>(List.of(coupon));

        when(couponGateway.findByCustomerId(customerId, pageable)).thenReturn(expected);

        final Page<Coupon> result = useCase.findByCustomerId(customerId, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldFindAvailableCouponsByCustomerId() {
        final UUID customerId = UUID.randomUUID();
        final Coupon coupon = Coupon.builder().id(UUID.randomUUID()).code("CP1").build();
        final List<Coupon> expected = List.of(coupon);

        when(couponGateway.findAvailableByCustomerId(any(UUID.class), any())).thenReturn(expected);

        final List<Coupon> result = useCase.findAvailableByCustomerId(customerId);

        assertEquals(1, result.size());
    }
}
