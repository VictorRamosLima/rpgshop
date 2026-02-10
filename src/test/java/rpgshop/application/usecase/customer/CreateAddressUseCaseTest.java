package rpgshop.application.usecase.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.customer.CreateAddressCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.customer.AddressGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAddressUseCaseTest {
    @Mock
    private AddressGateway addressGateway;

    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private CreateAddressUseCase useCase;

    @Test
    void shouldCreateAddressWhenCommandIsValid() {
        final UUID customerId = UUID.randomUUID();
        final CreateAddressCommand command = buildCommand(customerId);

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(Customer.builder().id(customerId).build()));
        when(addressGateway.save(any(Address.class), eq(customerId))).thenAnswer(invocation -> invocation.getArgument(0));

        final Address address = useCase.execute(command);

        assertEquals(command.street(), address.street());
        assertEquals(command.purpose(), address.purpose());
        assertEquals(command.city(), address.city());
        verify(addressGateway).save(any(Address.class), eq(customerId));
    }

    @Test
    void shouldThrowWhenRequiredFieldIsMissing() {
        final CreateAddressCommand command = new CreateAddressCommand(
            UUID.randomUUID(),
            null,
            "Casa",
            ResidenceType.HOUSE,
            StreetType.STREET,
            "Rua A",
            "10",
            "Centro",
            "01000-000",
            "Sao Paulo",
            "SP",
            "Brasil",
            null
        );

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldThrowWhenCustomerDoesNotExist() {
        final UUID customerId = UUID.randomUUID();
        final CreateAddressCommand command = buildCommand(customerId);
        when(customerGateway.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(command));
    }

    private CreateAddressCommand buildCommand(final UUID customerId) {
        return new CreateAddressCommand(
            customerId,
            AddressPurpose.DELIVERY,
            "Casa",
            ResidenceType.HOUSE,
            StreetType.STREET,
            "Rua A",
            "10",
            "Centro",
            "01000-000",
            "Sao Paulo",
            "SP",
            "Brasil",
            "Fundos"
        );
    }
}
