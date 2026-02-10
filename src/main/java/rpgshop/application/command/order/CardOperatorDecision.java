package rpgshop.application.command.order;

public record CardOperatorDecision(
    boolean approved,
    String reason
) {}
