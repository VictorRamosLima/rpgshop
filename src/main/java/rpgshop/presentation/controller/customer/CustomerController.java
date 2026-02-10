package rpgshop.presentation.controller.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rpgshop.application.command.customer.ChangePasswordCommand;
import rpgshop.application.command.customer.CreateAddressCommand;
import rpgshop.application.command.customer.CreateCreditCardCommand;
import rpgshop.application.command.customer.CreateCustomerCommand;
import rpgshop.application.command.customer.CreatePhoneCommand;
import rpgshop.application.command.customer.CustomerFilter;
import rpgshop.application.command.customer.UpdateCustomerCommand;
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
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.domain.entity.customer.constant.PhoneType;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;

import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    private final CreateCustomerUseCase createCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeactivateCustomerUseCase deactivateCustomerUseCase;
    private final QueryCustomersUseCase queryCustomersUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final CreateAddressUseCase createAddressUseCase;
    private final CreateCreditCardUseCase createCreditCardUseCase;
    private final CreatePhoneUseCase createPhoneUseCase;

    public CustomerController(
        final CreateCustomerUseCase createCustomerUseCase,
        final UpdateCustomerUseCase updateCustomerUseCase,
        final DeactivateCustomerUseCase deactivateCustomerUseCase,
        final QueryCustomersUseCase queryCustomersUseCase,
        final ChangePasswordUseCase changePasswordUseCase,
        final CreateAddressUseCase createAddressUseCase,
        final CreateCreditCardUseCase createCreditCardUseCase,
        final CreatePhoneUseCase createPhoneUseCase
    ) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.updateCustomerUseCase = updateCustomerUseCase;
        this.deactivateCustomerUseCase = deactivateCustomerUseCase;
        this.queryCustomersUseCase = queryCustomersUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.createAddressUseCase = createAddressUseCase;
        this.createCreditCardUseCase = createCreditCardUseCase;
        this.createPhoneUseCase = createPhoneUseCase;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String cpf,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) Gender gender,
        @RequestParam(required = false) Boolean isActive,
        @RequestParam(defaultValue = "0") int page,
        Model model
    ) {
        final var filter = new CustomerFilter(name, cpf, email, null, gender, isActive);
        final Page<Customer> customers = queryCustomersUseCase.execute(filter, PageRequest.of(page, 10));
        model.addAttribute("customers", customers);
        model.addAttribute("genders", Gender.values());
        return "customer/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("genders", Gender.values());
        model.addAttribute("phoneTypes", PhoneType.values());
        return "customer/create";
    }

    @PostMapping
    public String create(
        @RequestParam Gender gender,
        @RequestParam String name,
        @RequestParam String dateOfBirth,
        @RequestParam String cpf,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam String confirmPassword,
        @RequestParam PhoneType phoneType,
        @RequestParam String phoneAreaCode,
        @RequestParam String phoneNumber,
        Model model
    ) {
        try {
            final var command = new CreateCustomerCommand(
                gender, name, LocalDate.parse(dateOfBirth), cpf, email,
                password, confirmPassword, phoneType, phoneAreaCode, phoneNumber
            );
            createCustomerUseCase.execute(command);
            return "redirect:/customers";
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("genders", Gender.values());
            model.addAttribute("phoneTypes", PhoneType.values());
            return "customer/create";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable UUID id, Model model) {
        final var customer = queryCustomersUseCase.findById(id);
        if (customer.isEmpty()) {
            return "redirect:/customers";
        }
        model.addAttribute("customer", customer.get());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("phoneTypes", PhoneType.values());
        model.addAttribute("addressPurposes", AddressPurpose.values());
        model.addAttribute("residenceTypes", ResidenceType.values());
        model.addAttribute("streetTypes", StreetType.values());
        return "customer/detail";
    }

    @PostMapping("/{id}/update")
    public String update(
        @PathVariable UUID id,
        @RequestParam Gender gender,
        @RequestParam String name,
        @RequestParam String dateOfBirth,
        @RequestParam String email,
        Model model
    ) {
        try {
            final var command = new UpdateCustomerCommand(id, gender, name, LocalDate.parse(dateOfBirth), email);
            updateCustomerUseCase.execute(command);
            return "redirect:/customers/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/deactivate")
    public String deactivate(@PathVariable UUID id) {
        deactivateCustomerUseCase.execute(id);
        return "redirect:/customers/" + id;
    }

    @PostMapping("/{id}/change-password")
    public String changePassword(
        @PathVariable UUID id,
        @RequestParam String newPassword,
        @RequestParam String confirmPassword,
        Model model
    ) {
        try {
            final var command = new ChangePasswordCommand(id, newPassword, confirmPassword);
            changePasswordUseCase.execute(command);
            return "redirect:/customers/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/addresses")
    public String addAddress(
        @PathVariable UUID id,
        @RequestParam AddressPurpose purpose,
        @RequestParam(required = false) String label,
        @RequestParam ResidenceType residenceType,
        @RequestParam StreetType streetType,
        @RequestParam String street,
        @RequestParam String number,
        @RequestParam String neighborhood,
        @RequestParam String zipCode,
        @RequestParam String city,
        @RequestParam String state,
        @RequestParam String country,
        @RequestParam(required = false) String observations,
        Model model
    ) {
        try {
            final var command = new CreateAddressCommand(
                id, purpose, label, residenceType, streetType, street, number,
                neighborhood, zipCode, city, state, country, observations
            );
            createAddressUseCase.execute(command);
            return "redirect:/customers/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/credit-cards")
    public String addCreditCard(
        @PathVariable UUID id,
        @RequestParam String cardNumber,
        @RequestParam String printedName,
        @RequestParam UUID cardBrandId,
        @RequestParam String securityCode,
        @RequestParam(defaultValue = "false") boolean isPreferred,
        Model model
    ) {
        try {
            final var command = new CreateCreditCardCommand(
                id, cardNumber, printedName, cardBrandId, securityCode, isPreferred
            );
            createCreditCardUseCase.execute(command);
            return "redirect:/customers/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }

    @PostMapping("/{id}/phones")
    public String addPhone(
        @PathVariable UUID id,
        @RequestParam PhoneType type,
        @RequestParam String areaCode,
        @RequestParam String phoneNumber,
        Model model
    ) {
        try {
            final var command = new CreatePhoneCommand(id, type, areaCode, phoneNumber);
            createPhoneUseCase.execute(command);
            return "redirect:/customers/" + id;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return detail(id, model);
        }
    }
}
