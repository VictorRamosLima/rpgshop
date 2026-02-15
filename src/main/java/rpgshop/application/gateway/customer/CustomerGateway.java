package rpgshop.application.gateway.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rpgshop.application.command.customer.CustomerFilter;
import rpgshop.domain.entity.customer.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerGateway {
    Customer save(final Customer customer);
    Optional<Customer> findById(final UUID id);
    Page<Customer> findByFilters(final CustomerFilter filter, final Pageable pageable);
    boolean existsByCpf(final String cpf);
    boolean existsByEmail(final String email);
}
