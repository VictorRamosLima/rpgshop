package rpgshop.presentation.controller.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import rpgshop.application.command.customer.CreateCustomerCommand;
import rpgshop.application.command.customer.CustomerFilter;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.usecase.customer.ChangePasswordUseCase;
import rpgshop.application.usecase.customer.CreateAddressUseCase;
import rpgshop.application.usecase.customer.CreateCreditCardUseCase;
import rpgshop.application.usecase.customer.CreateCustomerUseCase;
import rpgshop.application.usecase.customer.CreatePhoneUseCase;
import rpgshop.application.usecase.customer.DeactivateCustomerUseCase;
import rpgshop.application.usecase.customer.QueryCustomersUseCase;
import rpgshop.application.usecase.customer.UpdateCustomerUseCase;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.domain.entity.customer.constant.PhoneType;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    @Mock
    private CreateCustomerUseCase createCustomerUseCase;
    @Mock
    private UpdateCustomerUseCase updateCustomerUseCase;
    @Mock
    private DeactivateCustomerUseCase deactivateCustomerUseCase;
    @Mock
    private QueryCustomersUseCase queryCustomersUseCase;
    @Mock
    private ChangePasswordUseCase changePasswordUseCase;
    @Mock
    private CreateAddressUseCase createAddressUseCase;
    @Mock
    private CreateCreditCardUseCase createCreditCardUseCase;
    @Mock
    private CreatePhoneUseCase createPhoneUseCase;
    @Mock
    private Model model;

    @InjectMocks
    private CustomerController controller;

    @Test
    void shouldListCustomersUsingFilter() {
        final Page<Customer> customers = new PageImpl<>(List.of(Customer.builder().id(UUID.randomUUID()).build()));
        when(queryCustomersUseCase.execute(any(), eq(PageRequest.of(1, 10)))).thenReturn(customers);

        final String view = controller.list("Ana", "123", "ana@test.com", Gender.FEMALE, true, 1, model);

        assertEquals("customer/list", view);
        final ArgumentCaptor<CustomerFilter> captor = ArgumentCaptor.forClass(CustomerFilter.class);
        verify(queryCustomersUseCase).execute(captor.capture(), eq(PageRequest.of(1, 10)));
        assertEquals("Ana", captor.getValue().name());
        assertEquals("123", captor.getValue().cpf());
        assertEquals("ana@test.com", captor.getValue().email());
        verify(model).addAttribute("customers", customers);
        verify(model).addAttribute(eq("genders"), any());
    }

    @Test
    void shouldRedirectToListWhenCustomerDetailIsNotFound() {
        final UUID customerId = UUID.randomUUID();
        when(queryCustomersUseCase.findById(customerId)).thenReturn(Optional.empty());

        final String view = controller.detail(customerId, model);

        assertEquals("redirect:/customers", view);
        verify(queryCustomersUseCase).findById(customerId);
    }

    @Test
    void shouldRenderDetailWhenCustomerExists() {
        final UUID customerId = UUID.randomUUID();
        final Customer customer = Customer.builder().id(customerId).name("Maria").build();
        when(queryCustomersUseCase.findById(customerId)).thenReturn(Optional.of(customer));

        final String view = controller.detail(customerId, model);

        assertEquals("customer/detail", view);
        verify(model).addAttribute("customer", customer);
        verify(model).addAttribute(eq("genders"), any());
        verify(model).addAttribute(eq("phoneTypes"), any());
        verify(model).addAttribute(eq("addressPurposes"), any());
    }

    @Test
    void shouldCreateCustomerAndRedirect() {
        final String view = controller.create(
            Gender.FEMALE,
            "Maria",
            "1990-05-10",
            "11122233344",
            "maria@test.com",
            "Senha@123",
            "Senha@123",
            PhoneType.MOBILE,
            "11",
            "999999999",
            ResidenceType.HOUSE,
            StreetType.STREET,
            "Rua A",
            "10",
            "Centro",
            "01000000",
            "Sao Paulo",
            "SP",
            "Brasil",
            null,
            model
        );

        assertEquals("redirect:/customers", view);
        verify(createCustomerUseCase).execute(any(CreateCustomerCommand.class));
    }

    @Test
    void shouldReturnCreateViewWithErrorWhenCreateFails() {
        when(createCustomerUseCase.execute(any())).thenThrow(new BusinessRuleException("email invalido"));

        final String view = controller.create(
            Gender.MALE,
            "Joao",
            "1992-01-10",
            "44433322211",
            "joao@test.com",
            "Senha@123",
            "Senha@123",
            PhoneType.MOBILE,
            "11",
            "999999999",
            ResidenceType.HOUSE,
            StreetType.STREET,
            "Rua B",
            "20",
            "Centro",
            "02000000",
            "Sao Paulo",
            "SP",
            "Brasil",
            null,
            model
        );

        assertEquals("customer/create", view);
        verify(model).addAttribute("error", "email invalido");
        verify(model).addAttribute(eq("genders"), any());
        verify(model).addAttribute(eq("phoneTypes"), any());
    }
}
