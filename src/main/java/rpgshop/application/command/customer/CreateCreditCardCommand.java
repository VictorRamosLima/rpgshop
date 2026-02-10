package rpgshop.application.command.customer;

import java.util.UUID;

public record CreateCreditCardCommand(
    UUID customerId,
    String cardNumber,
    String printedName,
    UUID cardBrandId,
    String securityCode,
    boolean isPreferred
) {}
