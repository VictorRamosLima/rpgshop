package rpgshop.infraestructure.mapper.stock;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.stock.StockEntry;
import rpgshop.infraestructure.mapper.product.ProductMapper;
import rpgshop.infraestructure.mapper.supplier.SupplierMapper;
import rpgshop.infraestructure.persistence.entity.stock.StockEntryJpaEntity;

public final class StockEntryMapper {
    private StockEntryMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static StockEntry toDomain(final StockEntryJpaEntity entity) {
        Assert.notNull(entity, "'StockEntryJpaEntity' should not be null");

        return StockEntry.builder()
            .id(entity.getId())
            .product(ProductMapper.toDomain(entity.getProduct()))
            .quantity(entity.getQuantity())
            .costValue(entity.getCostValue())
            .supplier(SupplierMapper.toDomain(entity.getSupplier()))
            .entryDate(entity.getEntryDate())
            .isReentry(entity.isReentry())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static StockEntryJpaEntity toEntity(final StockEntry domain) {
        Assert.notNull(domain, "'StockEntry' should not be null");

        return StockEntryJpaEntity.builder()
            .id(domain.id())
            .product(ProductMapper.toEntity(domain.product()))
            .quantity(domain.quantity())
            .costValue(domain.costValue())
            .supplier(SupplierMapper.toEntity(domain.supplier()))
            .entryDate(domain.entryDate())
            .isReentry(domain.isReentry())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();
    }
}
