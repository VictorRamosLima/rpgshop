package rpgshop.application.command.order;

import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(
    UUID customerId,
    UUID deliveryAddressId,
    List<PaymentInfo> payments
) {}
