package rpgshop.application.command.product;

import rpgshop.domain.entity.product.constant.StatusChangeCategory;

import java.time.Instant;

public record AutoDeactivateProductCommand(
    String reason,
    StatusChangeCategory category,
    Instant when
) {}
