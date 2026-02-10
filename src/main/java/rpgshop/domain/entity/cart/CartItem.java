package rpgshop.domain.entity.cart;

import lombok.Builder;
import rpgshop.domain.entity.product.Product;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record CartItem(
    UUID id,
    UUID cartId,
    Product product,
    int quantity,
    boolean isBlocked,
    Instant blockedAt,
    Instant expiresAt,
    Instant createdAt,
    Instant updatedAt
) {}
