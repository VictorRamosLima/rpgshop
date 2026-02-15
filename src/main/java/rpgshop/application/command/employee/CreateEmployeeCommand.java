package rpgshop.application.command.employee;

public record CreateEmployeeCommand(
    String name,
    String cpf,
    String email,
    String password,
    String confirmPassword
) {}
