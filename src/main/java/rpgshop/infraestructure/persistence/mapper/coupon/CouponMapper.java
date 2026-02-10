package rpgshop.infraestructure.persistence.mapper.coupon;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.infraestructure.persistence.mapper.customer.CustomerMapper;
import rpgshop.infraestructure.persistence.entity.coupon.CouponJpaEntity;

public final class CouponMapper {
    private CouponMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static Coupon toDomain(final CouponJpaEntity entity) {
        Assert.notNull(entity, "'CouponJpaEntity' should not be null");

        return Coupon.builder()
            .id(entity.getId())
            .code(entity.getCode())
            .type(entity.getType())
            .value(entity.getValue())
            .customer(entity.getCustomer() != null ? CustomerMapper.toDomain(entity.getCustomer()) : null)
            .isUsed(entity.isUsed())
            .usedAt(entity.getUsedAt())
            .expiresAt(entity.getExpiresAt())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static CouponJpaEntity toEntity(final Coupon domain) {
        Assert.notNull(domain, "'Coupon' should not be null");

        return CouponJpaEntity.builder()
            .id(domain.id())
            .code(domain.code())
            .type(domain.type())
            .value(domain.value())
            .customer(domain.customer() != null ? CustomerMapper.toEntity(domain.customer()) : null)
            .isUsed(domain.isUsed())
            .usedAt(domain.usedAt())
            .expiresAt(domain.expiresAt())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();
    }
}
