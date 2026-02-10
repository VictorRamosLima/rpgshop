package rpgshop.infraestructure.persistence.repository.product;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.RepositoryDefinition;
import rpgshop.infraestructure.persistence.entity.product.CategoryJpaEntity;

import java.util.List;
import java.util.UUID;

@RepositoryDefinition(domainClass = CategoryJpaEntity.class, idClass = UUID.class)
public interface CategoryRepository {
    @Nonnull
    List<CategoryJpaEntity> findByIdIn(@Nonnull final List<UUID> ids);
}
