package rpgshop.infraestructure.persistence.repository.user;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import rpgshop.domain.entity.user.constant.UserType;
import rpgshop.infraestructure.persistence.entity.user.UserJpaEntity;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = UserJpaEntity.class, idClass = UUID.class)
public interface UserRepository {
    @Nonnull
    UserJpaEntity save(@Nonnull final UserJpaEntity entity);

    Optional<UserJpaEntity> findById(@Nonnull final UUID id);

    Optional<UserJpaEntity> findByEmail(@Nonnull final String email);

    Optional<UserJpaEntity> findByUserTypeAndUserTypeId(
        @Nonnull final UserType userType,
        @Nonnull final UUID userTypeId
    );

    boolean existsByEmail(@Nonnull final String email);

    @Modifying
    @Query("UPDATE UserJpaEntity u SET u.password = :password WHERE u.id = :id")
    int updatePasswordById(
        @Param("id") @Nonnull final UUID id,
        @Param("password") @Nonnull final String password
    );
}
