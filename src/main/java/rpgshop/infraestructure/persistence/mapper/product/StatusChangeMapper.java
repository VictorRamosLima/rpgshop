package rpgshop.infraestructure.persistence.mapper.product;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.product.StatusChange;
import rpgshop.infraestructure.persistence.entity.product.StatusChangeJpaEntity;

public final class StatusChangeMapper {
    private StatusChangeMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static StatusChange toDomain(final StatusChangeJpaEntity entity) {
        Assert.notNull(entity, "'StatusChangeJpaEntity' should not be null");

        return StatusChange.builder()
            .id(entity.getId())
            .reason(entity.getReason())
            .category(entity.getCategory())
            .type(entity.getType())
            .createdAt(entity.getCreatedAt())
            .build();
    }

    @Nonnull
    public static StatusChangeJpaEntity toEntity(final StatusChange domain) {
        Assert.notNull(domain, "'StatusChange' should not be null");

        return StatusChangeJpaEntity.builder()
            .id(domain.id())
            .reason(domain.reason())
            .category(domain.category())
            .type(domain.type())
            .createdAt(domain.createdAt())
            .build();
    }
}

