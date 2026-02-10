package rpgshop.application.command.product;

import rpgshop.domain.entity.product.constant.StatusChangeCategory;

import java.util.UUID;

public record DeactivateProductCommand(
    UUID productId,
    String reason,
    StatusChangeCategory category
) {}
