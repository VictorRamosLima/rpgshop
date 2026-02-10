package rpgshop.application.usecase.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.customer.CustomerFilter;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.customer.Customer;

import java.util.Optional;
import java.util.UUID;

@Service
public class QueryCustomersUseCase {
    private final CustomerGateway customerGateway;

    public QueryCustomersUseCase(final CustomerGateway customerGateway) {
        this.customerGateway = customerGateway;
    }

    @Transactional(readOnly = true)
    public Page<Customer> execute(final CustomerFilter filter, final Pageable pageable) {
        return customerGateway.findByFilters(filter, pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findById(final UUID id) {
        return customerGateway.findById(id);
    }
}
