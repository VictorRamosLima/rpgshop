package rpgshop.infraestructure.persistence.gateway;

import org.springframework.stereotype.Component;
import rpgshop.application.gateway.product.ProductTypeGateway;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.infraestructure.mapper.product.ProductTypeMapper;
import rpgshop.infraestructure.persistence.repository.product.ProductTypeRepository;

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
}
