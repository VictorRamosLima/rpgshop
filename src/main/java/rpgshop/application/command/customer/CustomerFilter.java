package rpgshop.application.command.customer;

import rpgshop.domain.entity.customer.constant.Gender;

public record CustomerFilter(
    String name,
    String cpf,
    String email,
    String customerCode,
    Gender gender,
    Boolean isActive
) {}
