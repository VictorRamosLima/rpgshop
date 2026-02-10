package rpgshop.infraestructure.persistence.gateway;

import rpgshop.application.command.product.ProductFilter;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.product.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rpgshop.infraestructure.mapper.product.ProductMapper;
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

    public ProductGatewayJpa(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
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
    public List<UUID> findEligibleForAutoDeactivation(BigDecimal threshold) {
        return productRepository.findEligibleForAutoInactivation(threshold);
    }

    @Override
    public boolean existsBySku(String sku) {
        return productRepository.existsBySku(sku);
    }

    @Override
    public boolean existsByBarcode(String barcode) {
        return productRepository.existsByBarcode(barcode);
    }

    @Override
    public int deactivateOutOfMarket(final List<UUID> ids, final Instant now) {
        var changed = productRepository.bulkDeactivateByIds(ids, now);



        return 0;
    }
}
