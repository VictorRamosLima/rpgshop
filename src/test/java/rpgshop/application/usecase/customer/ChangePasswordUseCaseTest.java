package rpgshop.application.usecase.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.customer.ChangePasswordCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.customer.Customer;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangePasswordUseCaseTest {
    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private ChangePasswordUseCase useCase;

    @Test
    void shouldUpdatePasswordWhenCommandIsValid() {
        final UUID customerId = UUID.randomUUID();
        final ChangePasswordCommand command = new ChangePasswordCommand(customerId, "NovaSenha@123", "NovaSenha@123");

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(Customer.builder().id(customerId).build()));
        when(customerGateway.updatePassword(customerId, "NovaSenha@123")).thenReturn(1);

        useCase.execute(command);

        verify(customerGateway).updatePassword(customerId, "NovaSenha@123");
    }

    @Test
    void shouldThrowWhenPasswordIsWeak() {
        final ChangePasswordCommand command = new ChangePasswordCommand(UUID.randomUUID(), "abc", "abc");

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
        verifyNoMoreInteractions(customerGateway);
    }

    @Test
    void shouldThrowWhenPasswordWasNotUpdated() {
        final UUID customerId = UUID.randomUUID();
        final ChangePasswordCommand command = new ChangePasswordCommand(customerId, "NovaSenha@123", "NovaSenha@123");

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(Customer.builder().id(customerId).build()));
        when(customerGateway.updatePassword(customerId, "NovaSenha@123")).thenReturn(0);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }
}
