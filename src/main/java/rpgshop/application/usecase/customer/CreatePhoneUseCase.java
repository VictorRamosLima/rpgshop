package rpgshop.application.usecase.customer;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.customer.CreatePhoneCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.gateway.customer.PhoneGateway;
import rpgshop.domain.entity.customer.Phone;

import java.util.UUID;

@Service
public class CreatePhoneUseCase {
    private final PhoneGateway phoneGateway;
    private final CustomerGateway customerGateway;

    public CreatePhoneUseCase(final PhoneGateway phoneGateway, final CustomerGateway customerGateway) {
        this.phoneGateway = phoneGateway;
        this.customerGateway = customerGateway;
    }

    @Nonnull
    @Transactional
    public Phone execute(@Nonnull final CreatePhoneCommand command) {
        if (command.type() == null) {
            throw new BusinessRuleException("O tipo de telefone e obrigatorio");
        }
        if (command.areaCode() == null || command.areaCode().isBlank()) {
            throw new BusinessRuleException("Area code is required");
        }
        if (command.number() == null || command.number().isBlank()) {
            throw new BusinessRuleException("O numero de telefone e obrigatorio");
        }

        customerGateway.findById(command.customerId())
            .orElseThrow(() -> new EntityNotFoundException("Customer", command.customerId()));

        if (phoneGateway.existsByCustomerIdAndAreaCodeAndNumber(
            command.customerId(), command.areaCode(), command.number()
        )) {
            throw new BusinessRuleException("Este numero de telefone ja esta cadastrado para este cliente");
        }

        final Phone phone = Phone.builder()
            .id(UUID.randomUUID())
            .type(command.type())
            .areaCode(command.areaCode())
            .number(command.number())
            .isActive(true)
            .build();

        return phoneGateway.save(phone, command.customerId());
    }
}
