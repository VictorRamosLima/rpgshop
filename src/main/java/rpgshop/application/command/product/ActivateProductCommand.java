package rpgshop.application.command.product;

import rpgshop.domain.entity.product.constant.StatusChangeCategory;

import java.util.UUID;

public record ActivateProductCommand(
    UUID productId,
    String reason,
    StatusChangeCategory category
) {}
