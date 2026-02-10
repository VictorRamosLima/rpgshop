package rpgshop.application.usecase.customer;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.customer.CreateCreditCardCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.customer.CardBrandGateway;
import rpgshop.application.gateway.customer.CreditCardGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.customer.CardBrand;
import rpgshop.domain.entity.customer.CreditCard;

import java.util.UUID;

@Service
public class CreateCreditCardUseCase {
    private final CreditCardGateway creditCardGateway;
    private final CustomerGateway customerGateway;
    private final CardBrandGateway cardBrandGateway;

    public CreateCreditCardUseCase(
        final CreditCardGateway creditCardGateway,
        final CustomerGateway customerGateway,
        final CardBrandGateway cardBrandGateway
    ) {
        this.creditCardGateway = creditCardGateway;
        this.customerGateway = customerGateway;
        this.cardBrandGateway = cardBrandGateway;
    }

    @Nonnull
    @Transactional
    public CreditCard execute(@Nonnull final CreateCreditCardCommand command) {
        validateRequiredFields(command);

        customerGateway.findById(command.customerId())
            .orElseThrow(() -> new EntityNotFoundException("Customer", command.customerId()));

        final CardBrand cardBrand = cardBrandGateway.findById(command.cardBrandId())
            .orElseThrow(() -> new EntityNotFoundException("CardBrand", command.cardBrandId()));

        if (creditCardGateway.existsByCustomerIdAndCardNumber(command.customerId(), command.cardNumber())) {
            throw new BusinessRuleException("Este numero de cartao ja esta cadastrado para este cliente");
        }

        if (command.isPreferred()) {
            creditCardGateway.clearPreferredByCustomerId(command.customerId());
        }

        final CreditCard creditCard = CreditCard.builder()
            .id(UUID.randomUUID())
            .cardNumber(command.cardNumber())
            .printedName(command.printedName())
            .cardBrand(cardBrand)
            .securityCode(command.securityCode())
            .isPreferred(command.isPreferred())
            .isActive(true)
            .build();

        return creditCardGateway.save(creditCard, command.customerId());
    }

    private void validateRequiredFields(final CreateCreditCardCommand command) {
        if (command.cardNumber() == null || command.cardNumber().isBlank()) {
            throw new BusinessRuleException("O numero do cartao e obrigatorio");
        }
        if (command.printedName() == null || command.printedName().isBlank()) {
            throw new BusinessRuleException("O nome impresso e obrigatorio");
        }
        if (command.cardBrandId() == null) {
            throw new BusinessRuleException("A bandeira do cartao e obrigatoria");
        }
        if (command.securityCode() == null || command.securityCode().isBlank()) {
            throw new BusinessRuleException("O codigo de seguranca e obrigatorio");
        }
    }
}
