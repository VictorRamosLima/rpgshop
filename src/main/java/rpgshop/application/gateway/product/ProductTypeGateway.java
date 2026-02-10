package rpgshop.application.gateway.product;

import rpgshop.domain.entity.product.ProductType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTypeGateway {
    Optional<ProductType> findById(final UUID id);
    List<ProductType> findActive();
}
