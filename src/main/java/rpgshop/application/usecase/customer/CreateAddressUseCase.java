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
            throw new BusinessRuleException("A finalidade do endereco e obrigatoria");
        }
        if (command.residenceType() == null) {
            throw new BusinessRuleException("O tipo de residencia e obrigatorio");
        }
        if (command.streetType() == null) {
            throw new BusinessRuleException("O tipo de logradouro e obrigatorio");
        }
        if (command.street() == null || command.street().isBlank()) {
            throw new BusinessRuleException("O logradouro e obrigatorio");
        }
        if (command.number() == null || command.number().isBlank()) {
            throw new BusinessRuleException("O numero e obrigatorio");
        }
        if (command.neighborhood() == null || command.neighborhood().isBlank()) {
            throw new BusinessRuleException("O bairro e obrigatorio");
        }
        if (command.zipCode() == null || command.zipCode().isBlank()) {
            throw new BusinessRuleException("O CEP e obrigatorio");
        }
        if (command.city() == null || command.city().isBlank()) {
            throw new BusinessRuleException("A cidade e obrigatoria");
        }
        if (command.state() == null || command.state().isBlank()) {
            throw new BusinessRuleException("O estado e obrigatorio");
        }
        if (command.country() == null || command.country().isBlank()) {
            throw new BusinessRuleException("O pais e obrigatorio");
        }
    }
}
