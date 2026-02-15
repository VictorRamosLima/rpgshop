package rpgshop.application.gateway.user;

import rpgshop.domain.entity.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserGateway {
    User save(final User user);
    Optional<User> findById(final UUID id);
    Optional<User> findByEmail(final String email);
}
