package rpgshop.application.usecase.customer;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.customer.UpdateCustomerCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.customer.Customer;

@Service
public class UpdateCustomerUseCase {
    private final CustomerGateway customerGateway;

    public UpdateCustomerUseCase(final CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    @Nonnull
    @Transactional
    public Customer execute(@Nonnull final UpdateCustomerCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            throw new BusinessRuleException("Name is required");
        }
        if (command.gender() == null) {
            throw new BusinessRuleException("Gender is required");
        }
        if (command.dateOfBirth() == null) {
            throw new BusinessRuleException("Date of birth is required");
        }

        final Customer existing = customerGateway
            .findById(command.id())
            .orElseThrow(() -> new EntityNotFoundException("Customer", command.id()));

        if (!existing.isActive()) {
            throw new BusinessRuleException("Cannot update an inactive customer");
        }

        final Customer updated = existing.toBuilder()
            .gender(command.gender())
            .name(command.name())
            .dateOfBirth(command.dateOfBirth())
            .email(command.email() != null ? command.email() : existing.email())
            .build();

        return customerGateway.save(updated);
    }
}
