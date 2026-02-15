package rpgshop.infraestructure.persistence.gateway.user;

import org.springframework.stereotype.Component;
import rpgshop.application.gateway.user.UserGateway;
import rpgshop.domain.entity.user.User;
import rpgshop.infraestructure.persistence.mapper.user.UserMapper;
import rpgshop.infraestructure.persistence.repository.user.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserGatewayJpa implements UserGateway {
    private final UserRepository userRepository;

    public UserGatewayJpa(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(final User user) {
        final var entity = UserMapper.toEntity(user);
        final var saved = userRepository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(final UUID id) {
        return userRepository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return userRepository.findByEmail(email).map(UserMapper::toDomain);
    }
}
