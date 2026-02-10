package rpgshop.application.command.product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductFilter(
    String name,
    UUID productTypeId,
    UUID categoryId,
    UUID pricingGroupId,
    String sku,
    String barcode,
    Boolean isActive,
    BigDecimal minPrice,
    BigDecimal maxPrice
) {}
