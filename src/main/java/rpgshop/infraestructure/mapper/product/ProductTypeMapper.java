package rpgshop.infraestructure.mapper.product;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.infraestructure.persistence.entity.product.ProductTypeJpaEntity;

public final class ProductTypeMapper {
    private ProductTypeMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static ProductType toDomain(final ProductTypeJpaEntity entity) {
        Assert.notNull(entity, "'ProductTypeJpaEntity' should not be null");

        return ProductType.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .isActive(entity.isActive())
            .deactivatedAt(entity.getDeactivatedAt())
            .updatedAt(entity.getUpdatedAt())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    @Nonnull
    public static ProductTypeJpaEntity toEntity(final ProductType domain) {
        Assert.notNull(domain, "'ProductType' should not be null");

        return ProductTypeJpaEntity.builder()
            .id(domain.id())
            .name(domain.name())
            .description(domain.description())
            .isActive(domain.isActive())
            .deactivatedAt(domain.deactivatedAt())
            .updatedAt(domain.updatedAt())
            .createdAt(domain.createdAt())
            .build();
    }
}
