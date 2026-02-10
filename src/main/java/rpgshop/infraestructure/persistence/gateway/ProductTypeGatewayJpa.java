package rpgshop.infraestructure.persistence.gateway;

import rpgshop.application.gateway.product.ProductTypeGateway;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.infraestructure.mapper.product.ProductTypeMapper;
import rpgshop.infraestructure.persistence.repository.product.ProductTypeRepository;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProductTypeGatewayJpa implements ProductTypeGateway {

    private final ProductTypeRepository productTypeRepository;

    public ProductTypeGatewayJpa(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public Optional<ProductType> findById(UUID id) {
        return productTypeRepository.findById(id).map(ProductTypeMapper::toDomain);
    }
}
