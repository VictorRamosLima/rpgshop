package rpgshop.application.gateway.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rpgshop.domain.entity.employee.Employee;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeGateway {
    Employee save(final Employee employee);
    Optional<Employee> findById(final UUID id);
    Page<Employee> findByFilters(final String name, final String cpf, final Pageable pageable);
    boolean existsByCpf(final String cpf);
}
