package rpgshop.infraestructure.persistence.repository.product;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.RepositoryDefinition;
import rpgshop.infraestructure.persistence.entity.product.ProductTypeJpaEntity;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = ProductTypeJpaEntity.class, idClass = UUID.class)
public interface ProductTypeRepository {
    @Nonnull
    Optional<ProductTypeJpaEntity> findById(@Nonnull final UUID id);
}
