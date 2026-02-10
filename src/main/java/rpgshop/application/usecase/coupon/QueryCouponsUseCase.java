package rpgshop.application.usecase.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.gateway.coupon.CouponGateway;
import rpgshop.domain.entity.coupon.Coupon;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class QueryCouponsUseCase {
    private final CouponGateway couponGateway;

    public QueryCouponsUseCase(final CouponGateway couponGateway) {
        this.couponGateway = couponGateway;
    }

    @Transactional(readOnly = true)
    public Page<Coupon> findByCustomerId(final UUID customerId, final Pageable pageable) {
        return couponGateway.findByCustomerId(customerId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Coupon> findAvailableByCustomerId(final UUID customerId) {
        return couponGateway.findAvailableByCustomerId(customerId, Instant.now());
    }
}
