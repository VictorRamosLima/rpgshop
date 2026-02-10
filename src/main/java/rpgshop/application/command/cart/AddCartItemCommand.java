package rpgshop.application.command.cart;

import java.util.UUID;

public record AddCartItemCommand(
    UUID customerId,
    UUID productId,
    int quantity
) {}
