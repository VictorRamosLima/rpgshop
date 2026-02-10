package rpgshop.application.usecase.customer;

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
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.customer.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryCustomersUseCaseTest {
    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private QueryCustomersUseCase useCase;

    @Test
    void shouldDelegateFilteredSearchToGateway() {
        final CustomerFilter filter = new CustomerFilter("Alice", null, null, null, null, true);
        final Pageable pageable = PageRequest.of(0, 10);
        final Page<Customer> page = new PageImpl<>(List.of(Customer.builder().id(UUID.randomUUID()).name("Alice").build()));

        when(customerGateway.findByFilters(filter, pageable)).thenReturn(page);

        final Page<Customer> result = useCase.execute(filter, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldFindCustomerById() {
        final UUID customerId = UUID.randomUUID();
        when(customerGateway.findById(customerId)).thenReturn(Optional.of(Customer.builder().id(customerId).build()));

        final Optional<Customer> result = useCase.findById(customerId);

        assertTrue(result.isPresent());
        assertEquals(customerId, result.orElseThrow().id());
    }
}
