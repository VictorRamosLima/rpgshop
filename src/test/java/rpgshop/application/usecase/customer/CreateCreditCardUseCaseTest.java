package rpgshop.application.usecase.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.customer.CreateCreditCardCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.customer.CardBrandGateway;
import rpgshop.application.gateway.customer.CreditCardGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.customer.CardBrand;
import rpgshop.domain.entity.customer.CreditCard;
import rpgshop.domain.entity.customer.Customer;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCreditCardUseCaseTest {
    @Mock
    private CreditCardGateway creditCardGateway;

    @Mock
    private CustomerGateway customerGateway;

    @Mock
    private CardBrandGateway cardBrandGateway;

    @InjectMocks
    private CreateCreditCardUseCase useCase;

    @Test
    void shouldClearPreviousPreferredCardAndCreateNewPreferredCard() {
        final UUID customerId = UUID.randomUUID();
        final UUID brandId = UUID.randomUUID();
        final CreateCreditCardCommand command = new CreateCreditCardCommand(
            customerId,
            "4111111111111111",
            "CLIENTE",
            brandId,
            "123",
            true
        );

        final CardBrand brand = CardBrand.builder().id(brandId).name("Visa").isActive(true).build();

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(Customer.builder().id(customerId).build()));
        when(cardBrandGateway.findById(brandId)).thenReturn(Optional.of(brand));
        when(creditCardGateway.existsByCustomerIdAndCardNumber(customerId, command.cardNumber())).thenReturn(false);
        when(creditCardGateway.save(any(CreditCard.class), eq(customerId))).thenAnswer(invocation -> invocation.getArgument(0));

        final CreditCard saved = useCase.execute(command);

        assertEquals(brandId, saved.cardBrand().id());
        assertEquals(command.cardNumber(), saved.cardNumber());
        assertEquals(command.printedName(), saved.printedName());
        verify(creditCardGateway).clearPreferredByCustomerId(customerId);
    }

    @Test
    void shouldThrowWhenCardNumberAlreadyExistsForCustomer() {
        final UUID customerId = UUID.randomUUID();
        final UUID brandId = UUID.randomUUID();
        final CreateCreditCardCommand command = new CreateCreditCardCommand(
            customerId,
            "4111111111111111",
            "CLIENTE",
            brandId,
            "123",
            false
        );

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(Customer.builder().id(customerId).build()));
        when(cardBrandGateway.findById(brandId)).thenReturn(Optional.of(CardBrand.builder().id(brandId).build()));
        when(creditCardGateway.existsByCustomerIdAndCardNumber(customerId, command.cardNumber())).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
        verify(creditCardGateway, never()).save(any(CreditCard.class), eq(customerId));
    }
}
