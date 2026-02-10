package rpgshop.infraestructure.persistence.gateway.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rpgshop.application.command.product.ProductFilter;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.product.Product;
import rpgshop.infraestructure.persistence.mapper.product.ProductMapper;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;
import rpgshop.infraestructure.persistence.repository.product.ProductRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductGatewayJpa implements ProductGateway {
    private final ProductRepository productRepository;

    public ProductGatewayJpa(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(final Product product) {
        ProductJpaEntity jpaEntity = ProductMapper.toEntity(product);
        ProductJpaEntity saved = productRepository.save(jpaEntity);
        return ProductMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findById(final UUID id) {
        return productRepository.findById(id).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findByFilters(final ProductFilter filter, final Pageable pageable) {
        return productRepository.findByFilters(
            filter.name(),
            filter.productTypeId(),
            filter.categoryId(),
            filter.pricingGroupId(),
            filter.sku(),
            filter.barcode(),
            filter.isActive(),
            filter.minPrice(),
            filter.maxPrice(),
            pageable
        ).map(ProductMapper::toDomain);
    }

    @Override
    public List<UUID> findEligibleForAutoDeactivation(final BigDecimal threshold) {
        return productRepository.findEligibleForAutoInactivation(threshold);
    }

    @Override
    public boolean existsBySku(final String sku) {
        return productRepository.existsBySku(sku);
    }

    @Override
    public boolean existsByBarcode(final String barcode) {
        return productRepository.existsByBarcode(barcode);
    }

    @Override
    public int deactivateOutOfMarket(final List<UUID> ids, final Instant now) {
        return productRepository.bulkDeactivateByIds(ids, now);
    }
}
