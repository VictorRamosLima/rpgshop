package rpgshop.infraestructure.persistence.mapper.user;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.user.User;
import rpgshop.infraestructure.persistence.entity.user.UserJpaEntity;

public final class UserMapper {
    private UserMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static User toDomain(final UserJpaEntity entity) {
        Assert.notNull(entity, "'UserJpaEntity' should not be null");

        return User.builder()
            .id(entity.getId())
            .email(entity.getEmail())
            .password(entity.getPassword())
            .userType(entity.getUserType())
            .userTypeId(entity.getUserTypeId())
            .isActive(entity.isActive())
            .deactivatedAt(entity.getDeactivatedAt())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static UserJpaEntity toEntity(final User domain) {
        Assert.notNull(domain, "'User' should not be null");

        return UserJpaEntity.builder()
            .id(domain.id())
            .email(domain.email())
            .password(domain.password())
            .userType(domain.userType())
            .userTypeId(domain.userTypeId())
            .isActive(domain.isActive())
            .deactivatedAt(domain.deactivatedAt())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();
    }
}
