package rpgshop.application.usecase.customer;

import jakarta.annotation.Nonnull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.customer.ChangePasswordCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.gateway.user.UserGateway;
import rpgshop.domain.entity.user.User;
import rpgshop.domain.entity.user.constant.UserType;

import java.util.regex.Pattern;

@Service
public class ChangePasswordUseCase {
    private static final Pattern STRONG_PASSWORD = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$"
    );

    private final CustomerGateway customerGateway;
    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordUseCase(
        final CustomerGateway customerGateway,
        final UserGateway userGateway,
        final PasswordEncoder passwordEncoder
    ) {
        this.customerGateway = customerGateway;
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void execute(@Nonnull final ChangePasswordCommand command) {
        if (command.newPassword() == null || command.newPassword().isBlank()) {
            throw new BusinessRuleException("A senha e obrigatoria");
        }
        if (!STRONG_PASSWORD.matcher(command.newPassword()).matches()) {
            throw new BusinessRuleException(
                "Password must have at least 8 characters, uppercase, lowercase and special characters"
            );
        }
        if (!command.newPassword().equals(command.confirmPassword())) {
            throw new BusinessRuleException("As senhas nao coincidem");
        }

        customerGateway.findById(command.customerId())
            .orElseThrow(() -> new EntityNotFoundException("Customer", command.customerId()));

        final User user = userGateway.findByUserTypeAndUserTypeId(UserType.CUSTOMER, command.customerId())
            .orElseThrow(() -> new EntityNotFoundException("User for Customer", command.customerId()));

        final String encodedPassword = passwordEncoder.encode(command.newPassword());
        final int updated = userGateway.updatePassword(user.id(), encodedPassword);
        if (updated == 0) {
            throw new BusinessRuleException("Falha ao atualizar a senha");
        }
    }
}
