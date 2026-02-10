package rpgshop.application.command.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateProductCommand(
    String name,
    UUID productTypeId,
    List<UUID> categoryIds,
    BigDecimal height,
    BigDecimal width,
    BigDecimal depth,
    BigDecimal weight,
    UUID pricingGroupId,
    String barcode,
    String sku,
    BigDecimal costPrice
) {}
