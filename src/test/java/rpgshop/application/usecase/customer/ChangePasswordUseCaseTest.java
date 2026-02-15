package rpgshop.application.usecase.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import rpgshop.application.command.customer.ChangePasswordCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.gateway.user.UserGateway;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.user.User;
import rpgshop.domain.entity.user.constant.UserType;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChangePasswordUseCaseTest {
    @Mock
    private CustomerGateway customerGateway;

    @Mock
    private UserGateway userGateway;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ChangePasswordUseCase useCase;

    @Test
    void shouldUpdatePasswordWhenCommandIsValid() {
        final UUID customerId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        final ChangePasswordCommand command = new ChangePasswordCommand(customerId, "NovaSenha@123", "NovaSenha@123");

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(Customer.builder().id(customerId).build()));
        when(userGateway.findByUserTypeAndUserTypeId(UserType.CUSTOMER, customerId))
            .thenReturn(Optional.of(User.builder().id(userId).userType(UserType.CUSTOMER).userTypeId(customerId).build()));
        when(passwordEncoder.encode("NovaSenha@123")).thenReturn("encodedPassword");
        when(userGateway.updatePassword(userId, "encodedPassword")).thenReturn(1);

        useCase.execute(command);

        verify(userGateway).updatePassword(userId, "encodedPassword");
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
        final UUID userId = UUID.randomUUID();
        final ChangePasswordCommand command = new ChangePasswordCommand(customerId, "NovaSenha@123", "NovaSenha@123");

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(Customer.builder().id(customerId).build()));
        when(userGateway.findByUserTypeAndUserTypeId(UserType.CUSTOMER, customerId))
            .thenReturn(Optional.of(User.builder().id(userId).userType(UserType.CUSTOMER).userTypeId(customerId).build()));
        when(passwordEncoder.encode("NovaSenha@123")).thenReturn("encodedPassword");
        when(userGateway.updatePassword(userId, "encodedPassword")).thenReturn(0);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }
}
