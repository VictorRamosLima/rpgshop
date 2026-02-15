package rpgshop.infraestructure.persistence.repository.user;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.RepositoryDefinition;
import rpgshop.infraestructure.persistence.entity.user.UserJpaEntity;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = UserJpaEntity.class, idClass = UUID.class)
public interface UserRepository {
    @Nonnull
    UserJpaEntity save(@Nonnull final UserJpaEntity entity);

    @Nonnull
    Optional<UserJpaEntity> findById(@Nonnull final UUID id);

    @Nonnull
    Optional<UserJpaEntity> findByEmail(@Nonnull final String email);
}
