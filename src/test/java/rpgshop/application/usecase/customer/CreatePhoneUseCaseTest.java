package rpgshop.application.usecase.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.customer.CreatePhoneCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.gateway.customer.PhoneGateway;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.Phone;
import rpgshop.domain.entity.customer.constant.PhoneType;

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
class CreatePhoneUseCaseTest {
    @Mock
    private PhoneGateway phoneGateway;

    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private CreatePhoneUseCase useCase;

    @Test
    void shouldCreatePhoneWhenCommandIsValid() {
        final UUID customerId = UUID.randomUUID();
        final CreatePhoneCommand command = new CreatePhoneCommand(customerId, PhoneType.MOBILE, "11", "999999999");

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(Customer.builder().id(customerId).build()));
        when(phoneGateway.existsByCustomerIdAndAreaCodeAndNumber(customerId, "11", "999999999")).thenReturn(false);
        when(phoneGateway.save(any(Phone.class), eq(customerId))).thenAnswer(invocation -> invocation.getArgument(0));

        final Phone saved = useCase.execute(command);

        assertEquals(PhoneType.MOBILE, saved.type());
        assertEquals("11", saved.areaCode());
        assertEquals("999999999", saved.number());
        verify(phoneGateway).save(any(Phone.class), eq(customerId));
    }

    @Test
    void shouldThrowWhenPhoneAlreadyExistsForCustomer() {
        final UUID customerId = UUID.randomUUID();
        final CreatePhoneCommand command = new CreatePhoneCommand(customerId, PhoneType.MOBILE, "11", "999999999");

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(Customer.builder().id(customerId).build()));
        when(phoneGateway.existsByCustomerIdAndAreaCodeAndNumber(customerId, "11", "999999999")).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
        verify(phoneGateway, never()).save(any(Phone.class), eq(customerId));
    }
}
