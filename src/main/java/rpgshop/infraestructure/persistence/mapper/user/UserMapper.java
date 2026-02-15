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
            .name(entity.getName())
            .email(entity.getEmail())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static UserJpaEntity toEntity(final User domain) {
        Assert.notNull(domain, "'User' should not be null");

        return UserJpaEntity.builder()
            .id(domain.id())
            .name(domain.name())
            .email(domain.email())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();
    }
}
