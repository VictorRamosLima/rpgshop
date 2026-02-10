package rpgshop.infraestructure.persistence.mapper.customer;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.customer.Phone;
import rpgshop.infraestructure.persistence.entity.customer.PhoneJpaEntity;

public final class PhoneMapper {
    private PhoneMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static Phone toDomain(final PhoneJpaEntity entity) {
        Assert.notNull(entity, "'PhoneJpaEntity' should not be null");

        return Phone.builder()
            .id(entity.getId())
            .type(entity.getType())
            .areaCode(entity.getAreaCode())
            .number(entity.getNumber())
            .isActive(entity.isActive())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .deactivatedAt(entity.getDeactivatedAt())
            .build();
    }

    @Nonnull
    public static PhoneJpaEntity toEntity(final Phone domain) {
        Assert.notNull(domain, "'Phone' should not be null");

        return PhoneJpaEntity.builder()
            .id(domain.id())
            .type(domain.type())
            .areaCode(domain.areaCode())
            .number(domain.number())
            .isActive(domain.isActive())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .deactivatedAt(domain.deactivatedAt())
            .build();
    }
}

