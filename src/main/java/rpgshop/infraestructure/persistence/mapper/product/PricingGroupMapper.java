package rpgshop.infraestructure.persistence.mapper.product;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.infraestructure.persistence.entity.product.PricingGroupJpaEntity;

public final class PricingGroupMapper {
    private PricingGroupMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static PricingGroup toDomain(final PricingGroupJpaEntity entity) {
        Assert.notNull(entity, "'PricingGroupJpaEntity' should not be null");

        return PricingGroup.builder()
            .id(entity.getId())
            .name(entity.getName())
            .marginPercentage(entity.getMarginPercentage())
            .isActive(entity.isActive())
            .deactivatedAt(entity.getDeactivatedAt())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static PricingGroupJpaEntity toEntity(final PricingGroup domain) {
        Assert.notNull(domain, "'PricingGroup' should not be null");

        return PricingGroupJpaEntity.builder()
            .id(domain.id())
            .name(domain.name())
            .marginPercentage(domain.marginPercentage())
            .isActive(domain.isActive())
            .deactivatedAt(domain.deactivatedAt())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();
    }
}
