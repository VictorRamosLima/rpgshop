package rpgshop.infraestructure.persistence.repository.coupon;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import rpgshop.infraestructure.persistence.entity.coupon.CouponJpaEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = CouponJpaEntity.class, idClass = UUID.class)
public interface CouponRepository {
    @Nonnull
    CouponJpaEntity save(@Nonnull final CouponJpaEntity entity);

    @Nonnull
    Optional<CouponJpaEntity> findById(@Nonnull final UUID id);

    Optional<CouponJpaEntity> findByCode(@Nonnull final String code);

    boolean existsByCode(@Nonnull final String code);

    @Nonnull
    @Query("""
        SELECT c FROM CouponJpaEntity c
        WHERE c.customer.id = :customerId
            AND c.isUsed = false
            AND (c.expiresAt IS NULL OR c.expiresAt > :now)
        """)
    List<CouponJpaEntity> findAvailableByCustomerId(
        @Param("customerId") @Nonnull final UUID customerId,
        @Param("now") @Nonnull final Instant now
    );

    @Nonnull
    Page<CouponJpaEntity> findByCustomerId(
        @Nonnull final UUID customerId,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    @Query("""
        SELECT c FROM CouponJpaEntity c
        WHERE c.customer.id = :customerId
            AND c.type = "EXCHANGE"
            AND c.isUsed = false
            AND (c.expiresAt IS NULL OR c.expiresAt > :now)
        """)
    List<CouponJpaEntity> findAvailableExchangeCouponsByCustomerId(
        @Param("customerId") @Nonnull final UUID customerId,
        @Param("now") @Nonnull final Instant now
    );
}
