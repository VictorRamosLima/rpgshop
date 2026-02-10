package rpgshop.domain.entity.cart;

import lombok.Builder;
import rpgshop.domain.entity.customer.Customer;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
public record Cart(
    UUID id,
    Customer customer,
    List<CartItem> items,
    Instant createdAt,
    Instant updatedAt
) {}
