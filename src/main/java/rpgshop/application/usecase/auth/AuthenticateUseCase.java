package rpgshop.application.usecase.auth;

import jakarta.annotation.Nonnull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.auth.LoginCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.user.UserGateway;
import rpgshop.domain.entity.user.User;

@Service
public class AuthenticateUseCase {
    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public AuthenticateUseCase(
        final UserGateway userGateway,
        final PasswordEncoder passwordEncoder
    ) {
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Nonnull
    @Transactional(readOnly = true)
    public User execute(@Nonnull final LoginCommand command) {
        if (command.email() == null || command.email().isBlank()) {
            throw new BusinessRuleException("O e-mail e obrigatorio");
        }
        if (command.password() == null || command.password().isBlank()) {
            throw new BusinessRuleException("A senha e obrigatoria");
        }

        final User user = userGateway.findByEmail(command.email())
            .orElseThrow(() -> new BusinessRuleException("E-mail ou senha invalidos"));

        if (!user.isActive()) {
            throw new BusinessRuleException("Este usuario esta desativado");
        }

        if (!passwordEncoder.matches(command.password(), user.password())) {
            throw new BusinessRuleException("E-mail ou senha invalidos");
        }

        return user;
    }
}
