package rpgshop.infraestructure.persistence.repository.product;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import rpgshop.infraestructure.persistence.entity.product.PricingGroupJpaEntity;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = PricingGroupJpaEntity.class, idClass = UUID.class)
public interface PricingGroupRepository {
    @Nonnull
    PricingGroupJpaEntity save(@Nonnull final PricingGroupJpaEntity entity);

    @Nonnull
    Optional<PricingGroupJpaEntity> findById(@Nonnull final UUID id);

    void deleteById(@Nonnull final UUID id);

    boolean existsById(@Nonnull final UUID id);

    @Nonnull
    Page<PricingGroupJpaEntity> findByIsActiveTrue(@Nonnull final Pageable pageable);

    Optional<PricingGroupJpaEntity> findByNameIgnoreCase(@Nonnull final String name);

    boolean existsByNameIgnoreCase(@Nonnull final String name);

    @Nonnull
    Page<PricingGroupJpaEntity> findByNameContainingIgnoreCase(
        @Nonnull final String name,
        @Nonnull final Pageable pageable
    );
}
