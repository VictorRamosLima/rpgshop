package rpgshop.application.usecase.customer;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.customer.Customer;

import java.time.Instant;
import java.util.UUID;

@Service
public class DeactivateCustomerUseCase {
    private final CustomerGateway customerGateway;

    public DeactivateCustomerUseCase(final CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    @Nonnull
    @Transactional
    public Customer execute(@Nonnull final UUID customerId) {
        final Customer customer = customerGateway
            .findById(customerId)
            .orElseThrow(() -> new EntityNotFoundException("Customer", customerId));

        if (!customer.isActive()) {
            throw new BusinessRuleException("Customer is already inactive");
        }

        final Customer deactivated = customer.toBuilder()
            .isActive(false)
            .deactivatedAt(Instant.now())
            .build();

        return customerGateway.save(deactivated);
    }
}
