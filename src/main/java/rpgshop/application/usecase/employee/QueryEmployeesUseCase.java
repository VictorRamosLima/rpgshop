package rpgshop.application.usecase.employee;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.employee.EmployeeFilter;
import rpgshop.application.gateway.employee.EmployeeGateway;
import rpgshop.domain.entity.employee.Employee;

import java.util.Optional;
import java.util.UUID;

@Service
public class QueryEmployeesUseCase {
    private final EmployeeGateway employeeGateway;

    public QueryEmployeesUseCase(final EmployeeGateway employeeGateway) {
        this.employeeGateway = employeeGateway;
    }

    @Nonnull
    @Transactional(readOnly = true)
    public Page<Employee> execute(@Nonnull final EmployeeFilter filter, @Nonnull final Pageable pageable) {
        return employeeGateway.findByFilters(filter.name(), filter.cpf(), pageable);
    }

    @Nonnull
    @Transactional(readOnly = true)
    public Optional<Employee> findById(@Nonnull final UUID id) {
        return employeeGateway.findById(id);
    }
}
