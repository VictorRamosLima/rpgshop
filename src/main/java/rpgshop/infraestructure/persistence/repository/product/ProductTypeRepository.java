package rpgshop.infraestructure.persistence.repository.product;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import rpgshop.infraestructure.persistence.entity.product.ProductTypeJpaEntity;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = ProductTypeJpaEntity.class, idClass = UUID.class)
public interface ProductTypeRepository {
    @Nonnull
    ProductTypeJpaEntity save(@Nonnull final ProductTypeJpaEntity entity);

    @Nonnull
    Optional<ProductTypeJpaEntity> findById(@Nonnull final UUID id);

    void deleteById(@Nonnull final UUID id);

    boolean existsById(@Nonnull final UUID id);

    @Nonnull
    Page<ProductTypeJpaEntity> findByIsActiveTrue(@Nonnull final Pageable pageable);

    @Nonnull
    Optional<ProductTypeJpaEntity> findByNameIgnoreCase(@Nonnull final String name);

    boolean existsByNameIgnoreCase(@Nonnull final String name);

    @Nonnull
    Page<ProductTypeJpaEntity> findByNameContainingIgnoreCase(
        @Nonnull final String name,
        @Nonnull final Pageable pageable
    );
}
