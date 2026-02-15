package rpgshop.application.command.auth;

public record LoginCommand(
    String email,
    String password
) {}
