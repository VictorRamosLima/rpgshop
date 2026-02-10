package rpgshop.domain.entity.product;

import lombok.Builder;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;
import rpgshop.domain.entity.product.constant.StatusChangeType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static rpgshop.domain.entity.product.constant.StatusChangeType.DEACTIVATE;

@Builder(toBuilder = true)
public record Product(
    UUID id,
    String name,
    ProductType productType,
    List<Category> categories,
    BigDecimal height,
    BigDecimal width,
    BigDecimal depth,
    BigDecimal weight,
    PricingGroup pricingGroup,
    String barcode,
    String sku,
    BigDecimal salePrice,
    BigDecimal costPrice,
    Integer stockQuantity,
    List<StatusChange> statusChanges,
    BigDecimal minimumSaleThreshold,
    Instant createdAt,
    Instant updatedAt
) {
    public boolean isActive() {
        if (statusChanges == null || statusChanges.isEmpty()) {
            return true;
        }

        return statusChanges.getLast().isActive();
    }

    public Product activate(final String reason, final StatusChangeCategory category) {
        return updateStatus(StatusChange.activate(reason, category));
    }

    public Product deactivate(final String reason, final StatusChangeCategory category) {
        return updateStatus(StatusChange.deactivate(reason, category));
    }

    private Product updateStatus(final StatusChange statusChange) {
        final Instant now = Instant.now();

        final List<StatusChange> newStatusChanges = Stream.concat(
            statusChanges.stream(),
            Stream.of(statusChange)
        ).toList();

        return this.toBuilder()
            .statusChanges(newStatusChanges)
            .updatedAt(now)
            .build();
    }
}
