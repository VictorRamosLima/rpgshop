package rpgshop.infraestructure.persistence.gateway.product;

import org.springframework.stereotype.Component;
import rpgshop.application.gateway.product.ProductTypeGateway;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.infraestructure.persistence.mapper.product.ProductTypeMapper;
import rpgshop.infraestructure.persistence.repository.product.ProductTypeRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductTypeGatewayJpa implements ProductTypeGateway {
    private final ProductTypeRepository productTypeRepository;

    public ProductTypeGatewayJpa(final ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public Optional<ProductType> findById(final UUID id) {
        return productTypeRepository.findById(id).map(ProductTypeMapper::toDomain);
    }

    @Override
    public List<ProductType> findActive() {
        return productTypeRepository.findByIsActiveTrueOrderByNameAsc()
            .stream()
            .map(ProductTypeMapper::toDomain)
            .toList();
    }
}
