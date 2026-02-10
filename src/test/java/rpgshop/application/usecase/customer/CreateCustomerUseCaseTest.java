package rpgshop.application.usecase.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.customer.CreateCustomerCommand;
import rpgshop.application.gateway.customer.AddressGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.gateway.customer.PhoneGateway;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.Phone;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.domain.entity.customer.constant.PhoneType;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static rpgshop.domain.entity.customer.constant.AddressPurpose.BILLING;
import static rpgshop.domain.entity.customer.constant.AddressPurpose.DELIVERY;
import static rpgshop.domain.entity.customer.constant.AddressPurpose.RESIDENTIAL;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseTest {
    @Mock
    private AddressGateway addressGateway;

    @Mock
    private CustomerGateway customerGateway;

    @Mock
    private PhoneGateway phoneGateway;

    @InjectMocks
    private CreateCustomerUseCase useCase;

    @Test
    void shouldCreateCustomerWithResidentialBillingAndDeliveryAddresses() {
        final UUID customerId = UUID.randomUUID();
        final CreateCustomerCommand command = buildCommand();

        final Customer savedCustomer = Customer.builder()
            .id(customerId)
            .name(command.name())
            .isActive(true)
            .build();

        when(customerGateway.existsByCpf(command.cpf())).thenReturn(false);
        when(customerGateway.existsByEmail(command.email())).thenReturn(false);
        when(customerGateway.save(any(Customer.class))).thenReturn(savedCustomer);
        when(customerGateway.findById(customerId)).thenReturn(Optional.of(savedCustomer));

        when(phoneGateway.save(any(Phone.class), eq(customerId))).thenAnswer(invocation -> invocation.getArgument(0));
        when(addressGateway.existsByCustomerIdAndPurpose(customerId, BILLING)).thenReturn(true);
        when(addressGateway.existsByCustomerIdAndPurpose(customerId, DELIVERY)).thenReturn(true);
        when(addressGateway.save(any(Address.class), eq(customerId)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        useCase.execute(command);

        final ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addressGateway, times(3)).save(addressCaptor.capture(), eq(customerId));

        final Set<AddressPurpose> createdPurposes = addressCaptor.getAllValues().stream()
            .map(Address::purpose)
            .collect(Collectors.toSet());

        assertEquals(Set.of(RESIDENTIAL, BILLING, DELIVERY), createdPurposes);
        assertTrue(createdPurposes.contains(BILLING));
        assertTrue(createdPurposes.contains(DELIVERY));
    }

    private CreateCustomerCommand buildCommand() {
        return new CreateCustomerCommand(
            Gender.FEMALE,
            "Alice Test",
            LocalDate.of(1992, 5, 10),
            "12345678901",
            "alice@test.com",
            "Senha@123",
            "Senha@123",
            PhoneType.MOBILE,
            "11",
            "999999999",
            ResidenceType.APARTMENT,
            StreetType.AVENUE,
            "Paulista",
            "1000",
            "Centro",
            "01310000",
            "Sao Paulo",
            "SP",
            "Brasil",
            "Apto 12"
        );
    }
}
