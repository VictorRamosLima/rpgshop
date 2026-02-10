package rpgshop.domain.entity.stock;

import lombok.Builder;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.supplier.Supplier;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Builder(toBuilder = true)
public record StockEntry(
    UUID id,
    Product product,
    Integer quantity,
    BigDecimal costValue,
    Supplier supplier,
    LocalDate entryDate,
    boolean isReentry,
    Instant createdAt,
    Instant updatedAt
) {}
