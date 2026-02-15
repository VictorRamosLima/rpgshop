package rpgshop.application.gateway.user;

import rpgshop.domain.entity.user.User;
import rpgshop.domain.entity.user.constant.UserType;

import java.util.Optional;
import java.util.UUID;

public interface UserGateway {
    User save(final User user);
    Optional<User> findById(final UUID id);
    Optional<User> findByEmail(final String email);
    Optional<User> findByUserTypeAndUserTypeId(final UserType userType, final UUID userTypeId);
    boolean existsByEmail(final String email);
    int updatePassword(final UUID id, final String encodedPassword);
}
