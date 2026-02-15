package rpgshop.presentation.controller.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rpgshop.application.command.employee.CreateEmployeeCommand;
import rpgshop.application.command.employee.EmployeeFilter;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.usecase.employee.CreateEmployeeUseCase;
import rpgshop.application.usecase.employee.QueryEmployeesUseCase;
import rpgshop.domain.entity.employee.Employee;

import java.util.UUID;

@Controller
@RequestMapping("/employees")
public final class EmployeeController {
    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final QueryEmployeesUseCase queryEmployeesUseCase;

    public EmployeeController(
        final CreateEmployeeUseCase createEmployeeUseCase,
        final QueryEmployeesUseCase queryEmployeesUseCase
    ) {
        this.createEmployeeUseCase = createEmployeeUseCase;
        this.queryEmployeesUseCase = queryEmployeesUseCase;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) final String name,
        @RequestParam(required = false) final String cpf,
        @RequestParam(defaultValue = "0") final int page,
        final Model model
    ) {
        final var filter = new EmployeeFilter(name, cpf);
        final Page<Employee> employees = queryEmployeesUseCase.execute(filter, PageRequest.of(page, 10));
        model.addAttribute("employees", employees);
        return "employee/list";
    }

    @GetMapping("/new")
    public String showCreateForm() {
        return "employee/create";
    }

    @PostMapping
    public String create(
        @RequestParam final String name,
        @RequestParam final String cpf,
        @RequestParam final String email,
        @RequestParam final String password,
        @RequestParam final String confirmPassword,
        final Model model
    ) {
        try {
            final var command = new CreateEmployeeCommand(name, cpf, email, password, confirmPassword);
            createEmployeeUseCase.execute(command);
            return "redirect:/employees";
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return "employee/create";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable final UUID id, final Model model) {
        final var employee = queryEmployeesUseCase.findById(id);
        if (employee.isEmpty()) {
            return "redirect:/employees";
        }
        model.addAttribute("employee", employee.get());
        return "employee/detail";
    }
}
