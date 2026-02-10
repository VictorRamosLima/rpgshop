package rpgshop.application.usecase.customer;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.customer.ChangePasswordCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.customer.CustomerGateway;

import java.util.regex.Pattern;

@Service
public class ChangePasswordUseCase {
    private static final Pattern STRONG_PASSWORD = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$"
    );

    private final CustomerGateway customerGateway;

    public ChangePasswordUseCase(final CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    @Transactional
    public void execute(@Nonnull final ChangePasswordCommand command) {
        if (command.newPassword() == null || command.newPassword().isBlank()) {
            throw new BusinessRuleException("Password is required");
        }
        if (!STRONG_PASSWORD.matcher(command.newPassword()).matches()) {
            throw new BusinessRuleException(
                "Password must have at least 8 characters, uppercase, lowercase and special characters"
            );
        }
        if (!command.newPassword().equals(command.confirmPassword())) {
            throw new BusinessRuleException("Passwords do not match");
        }

        customerGateway.findById(command.customerId())
            .orElseThrow(() -> new EntityNotFoundException("Customer", command.customerId()));

        final int updated = customerGateway.updatePassword(command.customerId(), command.newPassword());
        if (updated == 0) {
            throw new BusinessRuleException("Failed to update password");
        }
    }
}
