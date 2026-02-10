package rpgshop.application.usecase.customer;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.customer.CreateCustomerCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.gateway.customer.PhoneGateway;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.Phone;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class CreateCustomerUseCase {
    private static final Pattern STRONG_PASSWORD = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$"
    );

    private final CustomerGateway customerGateway;
    private final PhoneGateway phoneGateway;

    public CreateCustomerUseCase(final CustomerGateway customerGateway, final PhoneGateway phoneGateway) {
        this.customerGateway = customerGateway;
        this.phoneGateway = phoneGateway;
    }

    @Nonnull
    @Transactional
    public Customer execute(@Nonnull final CreateCustomerCommand command) {
        validateRequiredFields(command);
        validatePassword(command.password(), command.confirmPassword());
        validateUniqueFields(command);

        final Customer customer = Customer.builder()
            .id(UUID.randomUUID())
            .gender(command.gender())
            .name(command.name())
            .dateOfBirth(command.dateOfBirth())
            .cpf(command.cpf())
            .email(command.email())
            .password(command.password())
            .ranking(BigDecimal.ZERO)
            .customerCode(generateCustomerCode())
            .phones(Collections.emptyList())
            .addresses(Collections.emptyList())
            .creditCards(Collections.emptyList())
            .isActive(true)
            .deactivatedAt(null)
            .createdAt(null)
            .updatedAt(null)
            .build();

        final Customer saved = customerGateway.save(customer);

        final Phone phone = Phone.builder()
            .id(UUID.randomUUID())
            .type(command.phoneType())
            .areaCode(command.phoneAreaCode())
            .number(command.phoneNumber())
            .isActive(true)
            .build();

        phoneGateway.save(phone, saved.id());

        return customerGateway.findById(saved.id()).orElse(saved);
    }

    private void validateRequiredFields(final CreateCustomerCommand command) {
        if (command.gender() == null) {
            throw new BusinessRuleException("Gender is required");
        }
        if (command.name() == null || command.name().isBlank()) {
            throw new BusinessRuleException("Name is required");
        }
        if (command.dateOfBirth() == null) {
            throw new BusinessRuleException("Date of birth is required");
        }
        if (command.cpf() == null || command.cpf().isBlank()) {
            throw new BusinessRuleException("CPF is required");
        }
        if (command.email() == null || command.email().isBlank()) {
            throw new BusinessRuleException("Email is required");
        }
        if (command.phoneType() == null) {
            throw new BusinessRuleException("Phone type is required");
        }
        if (command.phoneAreaCode() == null || command.phoneAreaCode().isBlank()) {
            throw new BusinessRuleException("Phone area code is required");
        }
        if (command.phoneNumber() == null || command.phoneNumber().isBlank()) {
            throw new BusinessRuleException("Phone number is required");
        }
    }

    private void validatePassword(final String password, final String confirmPassword) {
        if (password == null || password.isBlank()) {
            throw new BusinessRuleException("Password is required");
        }
        if (!STRONG_PASSWORD.matcher(password).matches()) {
            throw new BusinessRuleException(
                "Password must have at least 8 characters, uppercase, lowercase and special characters"
            );
        }
        if (!password.equals(confirmPassword)) {
            throw new BusinessRuleException("Passwords do not match");
        }
    }

    private void validateUniqueFields(final CreateCustomerCommand command) {
        if (customerGateway.existsByCpf(command.cpf())) {
            throw new BusinessRuleException("A customer with CPF '%s' already exists".formatted(command.cpf()));
        }
        if (customerGateway.existsByEmail(command.email())) {
            throw new BusinessRuleException("A customer with email '%s' already exists".formatted(command.email()));
        }
    }

    private String generateCustomerCode() {
        return "CLI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
