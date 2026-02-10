package rpgshop.application.command.cart;

import java.util.UUID;

public record UpdateCartItemCommand(
    UUID customerId,
    UUID productId,
    int quantity
) {}
