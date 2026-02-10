package rpgshop.domain.entity.product;

import lombok.Builder;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;
import rpgshop.domain.entity.product.constant.StatusChangeType;

import java.time.Instant;
import java.util.UUID;

import static rpgshop.domain.entity.product.constant.StatusChangeType.ACTIVATE;
import static rpgshop.domain.entity.product.constant.StatusChangeType.DEACTIVATE;

@Builder
public record StatusChange(
    UUID id,
    String reason,
    StatusChangeCategory category,
    StatusChangeType type,
    Instant createdAt
) {
    public boolean isActive() {
        return type == ACTIVATE;
    }

    public static StatusChange deactivate(final String reason, final StatusChangeCategory category) {
        return create(reason, category, DEACTIVATE);
    }

    public static StatusChange activate(final String reason, final StatusChangeCategory category) {
        return create(reason, category, ACTIVATE);
    }

    public static StatusChange create(
        final String reason,
        final StatusChangeCategory category,
        final StatusChangeType type
    ) {
        return StatusChange.builder()
            .reason(reason)
            .category(category)
            .type(type)
            .build();
    }
}
