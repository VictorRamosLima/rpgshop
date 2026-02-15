package rpgshop.application.usecase.employee;

import jakarta.annotation.Nonnull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.employee.CreateEmployeeCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.employee.EmployeeGateway;
import rpgshop.application.gateway.user.UserGateway;
import rpgshop.domain.entity.employee.Employee;
import rpgshop.domain.entity.user.User;
import rpgshop.domain.entity.user.constant.UserType;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class CreateEmployeeUseCase {
    private static final Pattern STRONG_PASSWORD = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$"
    );

    private final EmployeeGateway employeeGateway;
    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public CreateEmployeeUseCase(
        final EmployeeGateway employeeGateway,
        final UserGateway userGateway,
        final PasswordEncoder passwordEncoder
    ) {
        this.employeeGateway = employeeGateway;
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Nonnull
    @Transactional
    public Employee execute(@Nonnull final CreateEmployeeCommand command) {
        validateRequiredFields(command);
        validatePassword(command.password(), command.confirmPassword());
        validateUniqueFields(command);

        final Employee employee = Employee.builder()
            .id(UUID.randomUUID())
            .name(command.name())
            .cpf(command.cpf())
            .createdAt(null)
            .updatedAt(null)
            .build();

        final Employee saved = employeeGateway.save(employee);

        final User user = User.builder()
            .id(UUID.randomUUID())
            .email(command.email())
            .password(passwordEncoder.encode(command.password()))
            .userType(UserType.EMPLOYEE)
            .userTypeId(saved.id())
            .isActive(true)
            .deactivatedAt(null)
            .createdAt(null)
            .updatedAt(null)
            .build();

        userGateway.save(user);

        return saved;
    }

    private void validateRequiredFields(final CreateEmployeeCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            throw new BusinessRuleException("O nome e obrigatorio");
        }
        if (command.cpf() == null || command.cpf().isBlank()) {
            throw new BusinessRuleException("O CPF e obrigatorio");
        }
        if (command.email() == null || command.email().isBlank()) {
            throw new BusinessRuleException("O e-mail e obrigatorio");
        }
    }

    private void validatePassword(final String password, final String confirmPassword) {
        if (password == null || password.isBlank()) {
            throw new BusinessRuleException("A senha e obrigatoria");
        }
        if (!STRONG_PASSWORD.matcher(password).matches()) {
            throw new BusinessRuleException(
                "A senha deve ter pelo menos 8 caracteres, letras maiusculas, minusculas e caracteres especiais"
            );
        }
        if (!password.equals(confirmPassword)) {
            throw new BusinessRuleException("As senhas nao coincidem");
        }
    }

    private void validateUniqueFields(final CreateEmployeeCommand command) {
        if (employeeGateway.existsByCpf(command.cpf())) {
            throw new BusinessRuleException("Ja existe um funcionario com o CPF '%s'".formatted(command.cpf()));
        }
        if (userGateway.existsByEmail(command.email())) {
            throw new BusinessRuleException("Ja existe um usuario com o e-mail '%s'".formatted(command.email()));
        }
    }
}
