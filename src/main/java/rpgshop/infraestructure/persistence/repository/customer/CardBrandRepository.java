package rpgshop.infraestructure.persistence.repository.customer;

import jakarta.annotation.Nonnull;
import rpgshop.infraestructure.persistence.entity.customer.CardBrandJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = CardBrandJpaEntity.class, idClass = UUID.class)
public interface CardBrandRepository {
    @Nonnull
    CardBrandJpaEntity save(@Nonnull final CardBrandJpaEntity entity);

    @Nonnull
    Optional<CardBrandJpaEntity> findById(@Nonnull final UUID id);

    void deleteById(@Nonnull final UUID id);

    boolean existsById(@Nonnull final UUID id);

    @Nonnull
    Page<CardBrandJpaEntity> findByIsActiveTrue(@Nonnull final Pageable pageable);

    @Nonnull
    Optional<CardBrandJpaEntity> findByNameIgnoreCase(@Nonnull final String name);

    boolean existsByNameIgnoreCase(@Nonnull final String name);
}
