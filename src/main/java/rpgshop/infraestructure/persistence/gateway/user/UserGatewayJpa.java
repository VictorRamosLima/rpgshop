package rpgshop.infraestructure.persistence.gateway.user;

import org.springframework.stereotype.Component;
import rpgshop.application.gateway.user.UserGateway;
import rpgshop.domain.entity.user.User;
import rpgshop.domain.entity.user.constant.UserType;
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

    @Override
    public Optional<User> findByUserTypeAndUserTypeId(final UserType userType, final UUID userTypeId) {
        return userRepository.findByUserTypeAndUserTypeId(userType, userTypeId).map(UserMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public int updatePassword(final UUID id, final String encodedPassword) {
        return userRepository.updatePasswordById(id, encodedPassword);
    }
}
