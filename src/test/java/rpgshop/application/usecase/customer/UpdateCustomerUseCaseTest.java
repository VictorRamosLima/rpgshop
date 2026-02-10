package rpgshop.application.usecase.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.customer.UpdateCustomerCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.constant.Gender;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerUseCaseTest {
    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private UpdateCustomerUseCase useCase;

    @Test
    void shouldUpdateCustomerKeepingCurrentEmailWhenCommandEmailIsNull() {
        final UUID customerId = UUID.randomUUID();
        final UpdateCustomerCommand command = new UpdateCustomerCommand(
            customerId,
            Gender.FEMALE,
            "Novo Nome",
            LocalDate.of(1990, 1, 1),
            null
        );

        final Customer existing = Customer.builder()
            .id(customerId)
            .name("Nome Antigo")
            .gender(Gender.MALE)
            .dateOfBirth(LocalDate.of(1980, 1, 1))
            .email("cliente@teste.com")
            .isActive(true)
            .build();

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(existing));
        when(customerGateway.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Customer updated = useCase.execute(command);

        assertEquals("Novo Nome", updated.name());
        assertEquals("cliente@teste.com", updated.email());
        assertEquals(Gender.FEMALE, updated.gender());
    }

    @Test
    void shouldThrowWhenCustomerIsInactive() {
        final UUID customerId = UUID.randomUUID();
        final UpdateCustomerCommand command = new UpdateCustomerCommand(
            customerId,
            Gender.FEMALE,
            "Novo Nome",
            LocalDate.of(1990, 1, 1),
            "novo@teste.com"
        );

        final Customer existing = Customer.builder().id(customerId).isActive(false).build();
        when(customerGateway.findById(customerId)).thenReturn(Optional.of(existing));

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }
}
