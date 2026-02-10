package rpgshop.application.command.customer;

import java.util.UUID;

public record ChangePasswordCommand(
    UUID customerId,
    String newPassword,
    String confirmPassword
) {}
