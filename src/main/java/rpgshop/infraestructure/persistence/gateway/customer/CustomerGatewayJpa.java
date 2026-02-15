package rpgshop.infraestructure.persistence.gateway.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rpgshop.application.command.customer.CustomerFilter;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.infraestructure.persistence.mapper.customer.CustomerMapper;
import rpgshop.infraestructure.persistence.repository.customer.CustomerRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerGatewayJpa implements CustomerGateway {
    private final CustomerRepository customerRepository;

    public CustomerGatewayJpa(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer save(final Customer customer) {
        final var entity = CustomerMapper.toEntity(customer);
        final var saved = customerRepository.save(entity);
        return CustomerMapper.toDomain(saved);
    }

    @Override
    public Optional<Customer> findById(final UUID id) {
        return customerRepository.findById(id).map(CustomerMapper::toDomain);
    }

    @Override
    public Page<Customer> findByFilters(final CustomerFilter filter, final Pageable pageable) {
        return customerRepository.findByFilters(
            filter.name(),
            filter.cpf(),
            filter.email(),
            filter.customerCode(),
            filter.gender(),
            filter.isActive(),
            pageable
        ).map(CustomerMapper::toDomain);
    }

    @Override
    public boolean existsByCpf(final String cpf) {
        return customerRepository.existsByCpf(cpf);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return customerRepository.existsByEmail(email);
    }
}
