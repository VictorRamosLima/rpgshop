package rpgshop.infraestructure.persistence.repository.product;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.RepositoryDefinition;
import rpgshop.infraestructure.persistence.entity.product.PricingGroupJpaEntity;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = PricingGroupJpaEntity.class, idClass = UUID.class)
public interface PricingGroupRepository {
    @Nonnull
    Optional<PricingGroupJpaEntity> findById(@Nonnull final UUID id);
}
