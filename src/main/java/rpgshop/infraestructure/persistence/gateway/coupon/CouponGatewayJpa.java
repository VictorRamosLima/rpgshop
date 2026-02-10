package rpgshop.infraestructure.persistence.gateway.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rpgshop.application.gateway.coupon.CouponGateway;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.infraestructure.persistence.mapper.coupon.CouponMapper;
import rpgshop.infraestructure.persistence.repository.coupon.CouponRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CouponGatewayJpa implements CouponGateway {
    private final CouponRepository couponRepository;

    public CouponGatewayJpa(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public Coupon save(final Coupon coupon) {
        final var entity = CouponMapper.toEntity(coupon);
        final var saved = couponRepository.save(entity);
        return CouponMapper.toDomain(saved);
    }

    @Override
    public Optional<Coupon> findById(final UUID id) {
        return couponRepository.findById(id).map(CouponMapper::toDomain);
    }

    @Override
    public Optional<Coupon> findByCode(final String code) {
        return couponRepository.findByCode(code).map(CouponMapper::toDomain);
    }

    @Override
    public boolean existsByCode(final String code) {
        return couponRepository.existsByCode(code);
    }

    @Override
    public List<Coupon> findAvailableByCustomerId(final UUID customerId, final Instant now) {
        return couponRepository.findAvailableByCustomerId(customerId, now)
            .stream()
            .map(CouponMapper::toDomain)
            .toList();
    }

    @Override
    public List<Coupon> findAvailableExchangeCouponsByCustomerId(final UUID customerId, final Instant now) {
        return couponRepository.findAvailableExchangeCouponsByCustomerId(customerId, now)
            .stream()
            .map(CouponMapper::toDomain)
            .toList();
    }

    @Override
    public Page<Coupon> findByCustomerId(final UUID customerId, final Pageable pageable) {
        return couponRepository.findByCustomerId(customerId, pageable)
            .map(CouponMapper::toDomain);
    }
}
