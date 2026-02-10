package rpgshop.infraestructure.persistence.repository.customer;

import jakarta.annotation.Nonnull;
import rpgshop.infraestructure.persistence.entity.customer.CardBrandJpaEntity;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = CardBrandJpaEntity.class, idClass = UUID.class)
public interface CardBrandRepository {
    @Nonnull
    Optional<CardBrandJpaEntity> findById(@Nonnull final UUID id);

    boolean existsByNameIgnoreCase(@Nonnull final String name);
}
