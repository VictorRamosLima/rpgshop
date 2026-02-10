package rpgshop.infraestructure.mapper.customer;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.customer.CardBrand;
import rpgshop.infraestructure.persistence.entity.customer.CardBrandJpaEntity;

public final class CardBrandMapper {
    private CardBrandMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static CardBrand toDomain(final CardBrandJpaEntity entity) {
        Assert.notNull(entity, "'CardBrandJpaEntity' should not be null");

        return CardBrand.builder()
            .id(entity.getId())
            .name(entity.getName())
            .isActive(entity.isActive())
            .deactivatedAt(entity.getDeactivatedAt())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static CardBrandJpaEntity toEntity(final CardBrand domain) {
        Assert.notNull(domain, "'CardBrand' should not be null");

        return CardBrandJpaEntity.builder()
            .id(domain.id())
            .name(domain.name())
            .isActive(domain.isActive())
            .deactivatedAt(domain.deactivatedAt())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();
    }
}
