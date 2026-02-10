package rpgshop.application.usecase.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.customer.Customer;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeactivateCustomerUseCaseTest {
    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private DeactivateCustomerUseCase useCase;

    @Test
    void shouldDeactivateActiveCustomer() {
        final UUID customerId = UUID.randomUUID();
        final Customer active = Customer.builder().id(customerId).isActive(true).build();

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(active));
        when(customerGateway.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Customer deactivated = useCase.execute(customerId);

        assertFalse(deactivated.isActive());
        assertEquals(customerId, deactivated.id());
    }

    @Test
    void shouldThrowWhenCustomerIsAlreadyInactive() {
        final UUID customerId = UUID.randomUUID();
        final Customer inactive = Customer.builder().id(customerId).isActive(false).build();

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(inactive));

        assertThrows(BusinessRuleException.class, () -> useCase.execute(customerId));
    }
}
