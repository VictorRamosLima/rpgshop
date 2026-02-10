package rpgshop.application.usecase.customer;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.customer.CreateAddressCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.customer.AddressGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.customer.Address;

import java.util.UUID;

@Service
public class CreateAddressUseCase {
    private final AddressGateway addressGateway;
    private final CustomerGateway customerGateway;

    public CreateAddressUseCase(final AddressGateway addressGateway, final CustomerGateway customerGateway) {
        this.addressGateway = addressGateway;
        this.customerGateway = customerGateway;
    }

    @Nonnull
    @Transactional
    public Address execute(@Nonnull final CreateAddressCommand command) {
        validateRequiredFields(command);

        customerGateway.findById(command.customerId())
            .orElseThrow(() -> new EntityNotFoundException("Customer", command.customerId()));

        final Address address = Address.builder()
            .id(UUID.randomUUID())
            .purpose(command.purpose())
            .label(command.label())
            .residenceType(command.residenceType())
            .streetType(command.streetType())
            .street(command.street())
            .number(command.number())
            .neighborhood(command.neighborhood())
            .zipCode(command.zipCode())
            .city(command.city())
            .state(command.state())
            .country(command.country())
            .observations(command.observations())
            .isActive(true)
            .build();

        return addressGateway.save(address, command.customerId());
    }

    private void validateRequiredFields(final CreateAddressCommand command) {
        if (command.purpose() == null) {
            throw new BusinessRuleException("Address purpose is required");
        }
        if (command.residenceType() == null) {
            throw new BusinessRuleException("Residence type is required");
        }
        if (command.streetType() == null) {
            throw new BusinessRuleException("Street type is required");
        }
        if (command.street() == null || command.street().isBlank()) {
            throw new BusinessRuleException("Street is required");
        }
        if (command.number() == null || command.number().isBlank()) {
            throw new BusinessRuleException("Number is required");
        }
        if (command.neighborhood() == null || command.neighborhood().isBlank()) {
            throw new BusinessRuleException("Neighborhood is required");
        }
        if (command.zipCode() == null || command.zipCode().isBlank()) {
            throw new BusinessRuleException("ZIP code is required");
        }
        if (command.city() == null || command.city().isBlank()) {
            throw new BusinessRuleException("City is required");
        }
        if (command.state() == null || command.state().isBlank()) {
            throw new BusinessRuleException("State is required");
        }
        if (command.country() == null || command.country().isBlank()) {
            throw new BusinessRuleException("Country is required");
        }
    }
}
