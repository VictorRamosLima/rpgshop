package rpgshop.application.gateway.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rpgshop.domain.entity.coupon.Coupon;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponGateway {
    Coupon save(final Coupon coupon);
    Optional<Coupon> findById(final UUID id);
    Optional<Coupon> findByCode(final String code);
    boolean existsByCode(final String code);
    List<Coupon> findAvailableByCustomerId(final UUID customerId, final Instant now);
    List<Coupon> findAvailableExchangeCouponsByCustomerId(final UUID customerId, final Instant now);
    Page<Coupon> findByCustomerId(final UUID customerId, final Pageable pageable);
}
