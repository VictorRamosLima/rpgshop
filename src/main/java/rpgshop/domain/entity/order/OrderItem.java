package rpgshop.domain.entity.order;

import lombok.Builder;
import rpgshop.domain.entity.product.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record OrderItem(
    UUID id,
    Product product,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal totalPrice,
    Instant createdAt,
    Instant updatedAt
) {}
