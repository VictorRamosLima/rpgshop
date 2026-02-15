package rpgshop.application.usecase.customer;

import jakarta.annotation.Nonnull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.customer.CreateCustomerCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.customer.AddressGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.gateway.customer.PhoneGateway;
import rpgshop.application.gateway.user.UserGateway;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.Phone;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.user.User;
import rpgshop.domain.entity.user.constant.UserType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class CreateCustomerUseCase {
    private static final Pattern STRONG_PASSWORD = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,}$"
    );

    private final AddressGateway addressGateway;
    private final CustomerGateway customerGateway;
    private final PhoneGateway phoneGateway;
    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    public CreateCustomerUseCase(
        final AddressGateway addressGateway,
        final CustomerGateway customerGateway,
        final PhoneGateway phoneGateway,
        final UserGateway userGateway,
        final PasswordEncoder passwordEncoder
    ) {
        this.addressGateway = addressGateway;
        this.customerGateway = customerGateway;
        this.phoneGateway = phoneGateway;
        this.userGateway = userGateway;
        this.passwordEncoder = passwordEncoder;
    }

    @Nonnull
    @Transactional
    public Customer execute(@Nonnull final CreateCustomerCommand command) {
        validateRequiredFields(command);
        validatePassword(command.password(), command.confirmPassword());
        validateUniqueFields(command);

        final Customer customer = Customer.builder()
            .id(UUID.randomUUID())
            .gender(command.gender())
            .name(command.name())
            .dateOfBirth(command.dateOfBirth())
            .cpf(command.cpf())
            .email(command.email())
            .ranking(BigDecimal.ZERO)
            .customerCode(generateCustomerCode())
            .phones(Collections.emptyList())
            .addresses(Collections.emptyList())
            .creditCards(Collections.emptyList())
            .isActive(true)
            .deactivatedAt(null)
            .createdAt(null)
            .updatedAt(null)
            .build();

        final Customer saved = customerGateway.save(customer);

        final User user = User.builder()
            .id(UUID.randomUUID())
            .email(command.email())
            .password(passwordEncoder.encode(command.password()))
            .userType(UserType.CUSTOMER)
            .userTypeId(saved.id())
            .isActive(true)
            .deactivatedAt(null)
            .createdAt(null)
            .updatedAt(null)
            .build();

        userGateway.save(user);

        final Phone phone = Phone.builder()
            .id(UUID.randomUUID())
            .type(command.phoneType())
            .areaCode(command.phoneAreaCode())
            .number(command.phoneNumber())
            .isActive(true)
            .build();

        phoneGateway.save(phone, saved.id());

        addressGateway.save(buildAddress(command, AddressPurpose.RESIDENTIAL, "Residencial"), saved.id());
        addressGateway.save(buildAddress(command, AddressPurpose.BILLING, "Cobranca Principal"), saved.id());
        addressGateway.save(buildAddress(command, AddressPurpose.DELIVERY, "Entrega Principal"), saved.id());

        if (!addressGateway.existsByCustomerIdAndPurpose(saved.id(), AddressPurpose.BILLING)) {
            throw new BusinessRuleException("E obrigatorio possuir ao menos um endereco de cobranca");
        }
        if (!addressGateway.existsByCustomerIdAndPurpose(saved.id(), AddressPurpose.DELIVERY)) {
            throw new BusinessRuleException("E obrigatorio possuir ao menos um endereco de entrega");
        }

        return customerGateway.findById(saved.id()).orElse(saved);
    }

    private Address buildAddress(
        final CreateCustomerCommand command,
        final AddressPurpose purpose,
        final String label
    ) {
        return Address.builder()
            .id(UUID.randomUUID())
            .purpose(purpose)
            .label(label)
            .residenceType(command.residentialResidenceType())
            .streetType(command.residentialStreetType())
            .street(command.residentialStreet())
            .number(command.residentialNumber())
            .neighborhood(command.residentialNeighborhood())
            .zipCode(command.residentialZipCode())
            .city(command.residentialCity())
            .state(command.residentialState())
            .country(command.residentialCountry())
            .observations(command.residentialObservations())
            .isActive(true)
            .build();
    }

    private void validateRequiredFields(final CreateCustomerCommand command) {
        if (command.gender() == null) {
            throw new BusinessRuleException("O genero e obrigatorio");
        }
        if (command.name() == null || command.name().isBlank()) {
            throw new BusinessRuleException("O nome e obrigatorio");
        }
        if (command.dateOfBirth() == null) {
            throw new BusinessRuleException("A data de nascimento e obrigatoria");
        }
        if (command.cpf() == null || command.cpf().isBlank()) {
            throw new BusinessRuleException("O CPF e obrigatorio");
        }
        if (command.email() == null || command.email().isBlank()) {
            throw new BusinessRuleException("O e-mail e obrigatorio");
        }
        if (command.phoneType() == null) {
            throw new BusinessRuleException("O tipo de telefone e obrigatorio");
        }
        if (command.phoneAreaCode() == null || command.phoneAreaCode().isBlank()) {
            throw new BusinessRuleException("O DDD e obrigatorio");
        }
        if (command.phoneNumber() == null || command.phoneNumber().isBlank()) {
            throw new BusinessRuleException("O numero de telefone e obrigatorio");
        }
        if (command.residentialResidenceType() == null) {
            throw new BusinessRuleException("O tipo de residencia do endereco residencial e obrigatorio");
        }
        if (command.residentialStreetType() == null) {
            throw new BusinessRuleException("O tipo de logradouro do endereco residencial e obrigatorio");
        }
        if (command.residentialStreet() == null || command.residentialStreet().isBlank()) {
            throw new BusinessRuleException("O logradouro do endereco residencial e obrigatorio");
        }
        if (command.residentialNumber() == null || command.residentialNumber().isBlank()) {
            throw new BusinessRuleException("O numero do endereco residencial e obrigatorio");
        }
        if (command.residentialNeighborhood() == null || command.residentialNeighborhood().isBlank()) {
            throw new BusinessRuleException("O bairro do endereco residencial e obrigatorio");
        }
        if (command.residentialZipCode() == null || command.residentialZipCode().isBlank()) {
            throw new BusinessRuleException("O CEP do endereco residencial e obrigatorio");
        }
        if (command.residentialCity() == null || command.residentialCity().isBlank()) {
            throw new BusinessRuleException("A cidade do endereco residencial e obrigatoria");
        }
        if (command.residentialState() == null || command.residentialState().isBlank()) {
            throw new BusinessRuleException("O estado do endereco residencial e obrigatorio");
        }
        if (command.residentialCountry() == null || command.residentialCountry().isBlank()) {
            throw new BusinessRuleException("O pais do endereco residencial e obrigatorio");
        }
    }

    private void validatePassword(final String password, final String confirmPassword) {
        if (password == null || password.isBlank()) {
            throw new BusinessRuleException("A senha e obrigatoria");
        }
        if (!STRONG_PASSWORD.matcher(password).matches()) {
            throw new BusinessRuleException(
                "Password must have at least 8 characters, uppercase, lowercase and special characters"
            );
        }
        if (!password.equals(confirmPassword)) {
            throw new BusinessRuleException("As senhas nao coincidem");
        }
    }

    private void validateUniqueFields(final CreateCustomerCommand command) {
        if (customerGateway.existsByCpf(command.cpf())) {
            throw new BusinessRuleException("Ja existe um cliente com o CPF '%s'".formatted(command.cpf()));
        }
        if (userGateway.existsByEmail(command.email())) {
            throw new BusinessRuleException("Ja existe um usuario com o e-mail '%s'".formatted(command.email()));
        }
    }

    private String generateCustomerCode() {
        return "CLI-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
