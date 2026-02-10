package rpgshop.infraestructure.persistence.gateway.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rpgshop.application.command.customer.CustomerFilter;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;
import rpgshop.infraestructure.persistence.repository.customer.CustomerRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerGatewayJpaTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerGatewayJpa customerGatewayJpa;

    @Test
    void shouldSaveCustomer() {
        final UUID customerId = UUID.randomUUID();
        final Instant now = Instant.now();

        final Customer customer = Customer.builder()
            .id(customerId)
            .name("John Doe")
            .cpf("12345678901")
            .email("john@example.com")
            .password("password123")
            .gender(Gender.MALE)
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .customerCode("CUST001")
            .ranking(BigDecimal.ZERO)
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final CustomerJpaEntity savedEntity = CustomerJpaEntity.builder()
            .id(customerId)
            .name("John Doe")
            .cpf("12345678901")
            .email("john@example.com")
            .password("password123")
            .gender(Gender.MALE)
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .customerCode("CUST001")
            .ranking(BigDecimal.ZERO)
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(customerRepository.save(any(CustomerJpaEntity.class))).thenReturn(savedEntity);

        final Customer result = customerGatewayJpa.save(customer);

        assertNotNull(result);
        assertEquals(customerId, result.id());
        assertEquals("John Doe", result.name());
        verify(customerRepository, times(1)).save(argThat(entity ->
            entity.getName().equals("John Doe") && entity.getCpf().equals("12345678901")
        ));
    }

    @Test
    void shouldFindById() {
        final UUID customerId = UUID.randomUUID();
        final Instant now = Instant.now();

        final CustomerJpaEntity entity = CustomerJpaEntity.builder()
            .id(customerId)
            .name("John Doe")
            .cpf("12345678901")
            .email("john@example.com")
            .password("password123")
            .gender(Gender.MALE)
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .customerCode("CUST001")
            .ranking(BigDecimal.ZERO)
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(entity));

        final Optional<Customer> result = customerGatewayJpa.findById(customerId);

        assertTrue(result.isPresent());
        assertEquals(customerId, result.get().id());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void shouldReturnEmptyWhenCustomerNotFoundById() {
        final UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        final Optional<Customer> result = customerGatewayJpa.findById(customerId);

        assertTrue(result.isEmpty());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void shouldFindByFilters() {
        final UUID customerId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);

        final CustomerFilter filter = new CustomerFilter(
            "John", "12345678901", "john@example.com", "CUST001", Gender.MALE, true
        );

        final CustomerJpaEntity entity = CustomerJpaEntity.builder()
            .id(customerId)
            .name("John Doe")
            .cpf("12345678901")
            .email("john@example.com")
            .password("password123")
            .gender(Gender.MALE)
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .customerCode("CUST001")
            .ranking(BigDecimal.ZERO)
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final Page<CustomerJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(customerRepository.findByFilters(
            filter.name(), filter.cpf(), filter.email(),
            filter.customerCode(), filter.gender(), filter.isActive(), pageable
        )).thenReturn(page);

        final Page<Customer> result = customerGatewayJpa.findByFilters(filter, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("John Doe", result.getContent().getFirst().name());
        verify(customerRepository, times(1)).findByFilters(
            filter.name(),
            filter.cpf(),
            filter.email(),
            filter.customerCode(),
            filter.gender(),
            filter.isActive(),
            pageable
        );
    }

    @Test
    void shouldReturnTrueWhenCustomerExistsByCpf() {
        final String cpf = "12345678901";

        when(customerRepository.existsByCpf(cpf)).thenReturn(true);

        final boolean result = customerGatewayJpa.existsByCpf(cpf);

        assertTrue(result);
        verify(customerRepository, times(1)).existsByCpf(cpf);
    }

    @Test
    void shouldReturnFalseWhenCustomerNotExistsByCpf() {
        final String cpf = "12345678901";

        when(customerRepository.existsByCpf(cpf)).thenReturn(false);

        final boolean result = customerGatewayJpa.existsByCpf(cpf);

        assertFalse(result);
        verify(customerRepository, times(1)).existsByCpf(cpf);
    }

    @Test
    void shouldReturnTrueWhenCustomerExistsByEmail() {
        final String email = "john@example.com";

        when(customerRepository.existsByEmail(email)).thenReturn(true);

        final boolean result = customerGatewayJpa.existsByEmail(email);

        assertTrue(result);
        verify(customerRepository, times(1)).existsByEmail(email);
    }

    @Test
    void shouldReturnFalseWhenCustomerNotExistsByEmail() {
        final String email = "john@example.com";

        when(customerRepository.existsByEmail(email)).thenReturn(false);

        final boolean result = customerGatewayJpa.existsByEmail(email);

        assertFalse(result);
        verify(customerRepository, times(1)).existsByEmail(email);
    }

    @Test
    void shouldUpdatePassword() {
        final UUID customerId = UUID.randomUUID();
        final String newPassword = "newPassword123";

        when(customerRepository.updatePasswordById(customerId, newPassword)).thenReturn(1);

        final int result = customerGatewayJpa.updatePassword(customerId, newPassword);

        assertEquals(1, result);
        verify(customerRepository, times(1)).updatePasswordById(customerId, newPassword);
    }

    @Test
    void shouldReturnZeroWhenUpdatePasswordFailsForNonExistentCustomer() {
        final UUID customerId = UUID.randomUUID();
        final String newPassword = "newPassword123";

        when(customerRepository.updatePasswordById(customerId, newPassword)).thenReturn(0);

        final int result = customerGatewayJpa.updatePassword(customerId, newPassword);

        assertEquals(0, result);
        verify(customerRepository, times(1)).updatePasswordById(customerId, newPassword);
    }
}
