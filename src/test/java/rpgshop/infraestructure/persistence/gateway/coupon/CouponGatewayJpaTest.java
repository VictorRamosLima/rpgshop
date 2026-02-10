package rpgshop.infraestructure.persistence.gateway.coupon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.infraestructure.persistence.entity.coupon.CouponJpaEntity;
import rpgshop.infraestructure.persistence.repository.coupon.CouponRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponGatewayJpaTest {
    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponGatewayJpa couponGatewayJpa;

    @Test
    void shouldSaveCoupon() {
        final UUID couponId = UUID.randomUUID();
        final Instant now = Instant.now();

        final Coupon coupon = Coupon.builder()
            .id(couponId)
            .code("PROMO10")
            .type(CouponType.PROMOTIONAL)
            .value(new BigDecimal("10.00"))
            .isUsed(false)
            .expiresAt(now.plusSeconds(86400))
            .createdAt(now)
            .updatedAt(now)
            .build();

        final CouponJpaEntity savedEntity = CouponJpaEntity.builder()
            .id(couponId)
            .code("PROMO10")
            .type(CouponType.PROMOTIONAL)
            .value(new BigDecimal("10.00"))
            .isUsed(false)
            .expiresAt(now.plusSeconds(86400))
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(couponRepository.save(any(CouponJpaEntity.class))).thenReturn(savedEntity);

        final Coupon result = couponGatewayJpa.save(coupon);

        assertNotNull(result);
        assertEquals(couponId, result.id());
        assertEquals("PROMO10", result.code());
        verify(couponRepository, times(1)).save(argThat(entity ->
            entity.getCode().equals("PROMO10") && entity.getType() == CouponType.PROMOTIONAL
        ));
    }

    @Test
    void shouldFindById() {
        final UUID couponId = UUID.randomUUID();
        final Instant now = Instant.now();

        final CouponJpaEntity entity = CouponJpaEntity.builder()
            .id(couponId)
            .code("PROMO10")
            .type(CouponType.PROMOTIONAL)
            .value(new BigDecimal("10.00"))
            .isUsed(false)
            .expiresAt(now.plusSeconds(86400))
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(couponRepository.findById(couponId)).thenReturn(Optional.of(entity));

        final Optional<Coupon> result = couponGatewayJpa.findById(couponId);

        assertTrue(result.isPresent());
        assertEquals(couponId, result.get().id());
        verify(couponRepository, times(1)).findById(couponId);
    }

    @Test
    void shouldReturnEmptyWhenCouponNotFoundById() {
        final UUID couponId = UUID.randomUUID();

        when(couponRepository.findById(couponId)).thenReturn(Optional.empty());

        final Optional<Coupon> result = couponGatewayJpa.findById(couponId);

        assertTrue(result.isEmpty());
        verify(couponRepository, times(1)).findById(couponId);
    }

    @Test
    void shouldFindByCode() {
        final UUID couponId = UUID.randomUUID();
        final String code = "PROMO10";
        final Instant now = Instant.now();

        final CouponJpaEntity entity = CouponJpaEntity.builder()
            .id(couponId)
            .code(code)
            .type(CouponType.PROMOTIONAL)
            .value(new BigDecimal("10.00"))
            .isUsed(false)
            .expiresAt(now.plusSeconds(86400))
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(couponRepository.findByCode(code)).thenReturn(Optional.of(entity));

        final Optional<Coupon> result = couponGatewayJpa.findByCode(code);

        assertTrue(result.isPresent());
        assertEquals(code, result.get().code());
        verify(couponRepository, times(1)).findByCode(code);
    }

    @Test
    void shouldReturnEmptyWhenCouponNotFoundByCode() {
        final String code = "INVALID";

        when(couponRepository.findByCode(code)).thenReturn(Optional.empty());

        final Optional<Coupon> result = couponGatewayJpa.findByCode(code);

        assertTrue(result.isEmpty());
        verify(couponRepository, times(1)).findByCode(code);
    }

    @Test
    void shouldReturnTrueWhenCouponExistsByCode() {
        final String code = "PROMO10";

        when(couponRepository.existsByCode(code)).thenReturn(true);

        final boolean result = couponGatewayJpa.existsByCode(code);

        assertTrue(result);
        verify(couponRepository, times(1)).existsByCode(code);
    }

    @Test
    void shouldReturnFalseWhenCouponNotExistsByCode() {
        final String code = "INVALID";

        when(couponRepository.existsByCode(code)).thenReturn(false);

        final boolean result = couponGatewayJpa.existsByCode(code);

        assertFalse(result);
        verify(couponRepository, times(1)).existsByCode(code);
    }

    @Test
    void shouldFindAvailableByCustomerId() {
        final UUID customerId = UUID.randomUUID();
        final UUID couponId = UUID.randomUUID();
        final Instant now = Instant.now();

        final CouponJpaEntity entity = CouponJpaEntity.builder()
            .id(couponId)
            .code("PROMO10")
            .type(CouponType.PROMOTIONAL)
            .value(new BigDecimal("10.00"))
            .isUsed(false)
            .expiresAt(now.plusSeconds(86400))
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(couponRepository.findAvailableByCustomerId(customerId, now)).thenReturn(List.of(entity));

        final List<Coupon> result = couponGatewayJpa.findAvailableByCustomerId(customerId, now);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(couponId, result.getFirst().id());
        verify(couponRepository, times(1)).findAvailableByCustomerId(customerId, now);
    }

    @Test
    void shouldReturnEmptyListWhenNoAvailableCouponsForCustomer() {
        final UUID customerId = UUID.randomUUID();
        final Instant now = Instant.now();

        when(couponRepository.findAvailableByCustomerId(customerId, now)).thenReturn(List.of());

        final List<Coupon> result = couponGatewayJpa.findAvailableByCustomerId(customerId, now);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(couponRepository, times(1)).findAvailableByCustomerId(customerId, now);
    }

    @Test
    void shouldFindAvailableExchangeCouponsByCustomerId() {
        final UUID customerId = UUID.randomUUID();
        final UUID couponId = UUID.randomUUID();
        final Instant now = Instant.now();

        final CouponJpaEntity entity = CouponJpaEntity.builder()
            .id(couponId)
            .code("EXCHANGE10")
            .type(CouponType.EXCHANGE)
            .value(new BigDecimal("10.00"))
            .isUsed(false)
            .expiresAt(now.plusSeconds(86400))
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(couponRepository.findAvailableExchangeCouponsByCustomerId(customerId, now)).thenReturn(List.of(entity));

        final List<Coupon> result = couponGatewayJpa.findAvailableExchangeCouponsByCustomerId(customerId, now);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(CouponType.EXCHANGE, result.getFirst().type());
        verify(couponRepository, times(1)).findAvailableExchangeCouponsByCustomerId(customerId, now);
    }

    @Test
    void shouldFindByCustomerId() {
        final UUID customerId = UUID.randomUUID();
        final UUID couponId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);

        final CouponJpaEntity entity = CouponJpaEntity.builder()
            .id(couponId)
            .code("PROMO10")
            .type(CouponType.PROMOTIONAL)
            .value(new BigDecimal("10.00"))
            .isUsed(false)
            .expiresAt(now.plusSeconds(86400))
            .createdAt(now)
            .updatedAt(now)
            .build();

        final Page<CouponJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(couponRepository.findByCustomerId(customerId, pageable)).thenReturn(page);

        final Page<Coupon> result = couponGatewayJpa.findByCustomerId(customerId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(couponId, result.getContent().getFirst().id());
        verify(couponRepository, times(1)).findByCustomerId(customerId, pageable);
    }
}
