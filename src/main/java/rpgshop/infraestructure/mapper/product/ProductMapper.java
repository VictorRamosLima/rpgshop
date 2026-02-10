package rpgshop.infraestructure.mapper.product;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.product.Category;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.StatusChange;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;

import java.util.ArrayList;
import java.util.Collections;

public final class ProductMapper {
    private ProductMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static Product toDomain(final ProductJpaEntity entity) {
        Assert.notNull(entity, "'ProductJpaEntity' should not be null");

        var categories = entity.getCategories() != null
            ? entity.getCategories().stream().map(CategoryMapper::toDomain).toList()
            : Collections.<Category>emptyList();

        var statusChanges = entity.getStatusChanges() != null
            ? entity.getStatusChanges().stream().map(StatusChangeMapper::toDomain).toList()
            : Collections.<StatusChange>emptyList();

        return Product.builder()
            .id(entity.getId())
            .name(entity.getName())
            .productType(ProductTypeMapper.toDomain(entity.getProductType()))
            .categories(categories)
            .height(entity.getHeight())
            .width(entity.getWidth())
            .depth(entity.getDepth())
            .weight(entity.getWeight())
            .pricingGroup(PricingGroupMapper.toDomain(entity.getPricingGroup()))
            .barcode(entity.getBarcode())
            .sku(entity.getSku())
            .salePrice(entity.getSalePrice())
            .costPrice(entity.getCostPrice())
            .stockQuantity(entity.getStockQuantity())
            .statusChanges(statusChanges)
            .minimumSaleThreshold(entity.getMinimumSaleThreshold())
            .isActive(entity.isActive())
            .deactivatedAt(entity.getDeactivatedAt())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static ProductJpaEntity toEntity(final Product domain) {
        Assert.notNull(domain, "'Product' should not be null");

        var entity = ProductJpaEntity.builder()
            .id(domain.id())
            .name(domain.name())
            .productType(ProductTypeMapper.toEntity(domain.productType()))
            .height(domain.height())
            .width(domain.width())
            .depth(domain.depth())
            .weight(domain.weight())
            .pricingGroup(PricingGroupMapper.toEntity(domain.pricingGroup()))
            .barcode(domain.barcode())
            .sku(domain.sku())
            .salePrice(domain.salePrice())
            .costPrice(domain.costPrice())
            .stockQuantity(domain.stockQuantity())
            .minimumSaleThreshold(domain.minimumSaleThreshold())
            .isActive(domain.isActive())
            .deactivatedAt(domain.deactivatedAt())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();

        if (domain.categories() != null) {
            var categories = new ArrayList<>(domain.categories().stream()
                .map(CategoryMapper::toEntity).toList());
            entity.setCategories(categories);
        }

        if (domain.statusChanges() != null) {
            var statusChanges = new ArrayList<>(domain.statusChanges().stream()
                .map(StatusChangeMapper::toEntity).toList());
            statusChanges.forEach(sc -> sc.setProduct(entity));
            entity.setStatusChanges(statusChanges);
        }

        return entity;
    }
}
