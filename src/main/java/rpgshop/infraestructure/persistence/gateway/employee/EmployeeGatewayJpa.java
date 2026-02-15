package rpgshop.infraestructure.persistence.gateway.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rpgshop.application.gateway.employee.EmployeeGateway;
import rpgshop.domain.entity.employee.Employee;
import rpgshop.infraestructure.persistence.mapper.employee.EmployeeMapper;
import rpgshop.infraestructure.persistence.repository.employee.EmployeeRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class EmployeeGatewayJpa implements EmployeeGateway {
    private final EmployeeRepository employeeRepository;

    public EmployeeGatewayJpa(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee save(final Employee employee) {
        final var entity = EmployeeMapper.toEntity(employee);
        final var saved = employeeRepository.save(entity);
        return EmployeeMapper.toDomain(saved);
    }

    @Override
    public Optional<Employee> findById(final UUID id) {
        return employeeRepository.findById(id).map(EmployeeMapper::toDomain);
    }

    @Override
    public Page<Employee> findByFilters(final String name, final String cpf, final Pageable pageable) {
        return employeeRepository.findByFilters(name, cpf, pageable).map(EmployeeMapper::toDomain);
    }

    @Override
    public boolean existsByCpf(final String cpf) {
        return employeeRepository.existsByCpf(cpf);
    }
}
