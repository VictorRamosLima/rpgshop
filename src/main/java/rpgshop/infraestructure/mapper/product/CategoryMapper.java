package rpgshop.infraestructure.mapper.product;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.product.Category;
import rpgshop.domain.entity.product.Product;
import rpgshop.infraestructure.persistence.entity.product.CategoryJpaEntity;

import java.util.ArrayList;
import java.util.Collections;

public final class CategoryMapper {
    private CategoryMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static Category toDomain(final CategoryJpaEntity entity) {
        Assert.notNull(entity, "'CategoryJpaEntity' should not be null");

        var products = entity.getProducts() != null
            ? entity.getProducts().stream().map(ProductMapper::toDomain).toList()
            : Collections.<Product>emptyList();

        return Category.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .products(products)
            .isActive(entity.isActive())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .deactivatedAt(entity.getDeactivatedAt())
            .build();
    }

    @Nonnull
    public static CategoryJpaEntity toEntity(final Category domain) {
        Assert.notNull(domain, "'Category' should not be null");

        var entity = CategoryJpaEntity.builder()
            .id(domain.id())
            .name(domain.name())
            .description(domain.description())
            .isActive(domain.isActive())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .deactivatedAt(domain.deactivatedAt())
            .build();

        if (domain.products() != null) {
            var products = new ArrayList<>(domain.products().stream()
                .map(ProductMapper::toEntity).toList());
            entity.setProducts(products);
        }

        return entity;
    }
}
