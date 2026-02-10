package rpgshop.infraestructure.persistence.repository.product;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import rpgshop.infraestructure.persistence.entity.product.CategoryJpaEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = CategoryJpaEntity.class, idClass = UUID.class)
public interface CategoryRepository {
    @Nonnull
    CategoryJpaEntity save(@Nonnull final CategoryJpaEntity entity);

    @Nonnull
    Optional<CategoryJpaEntity> findById(@Nonnull final UUID id);

    void deleteById(@Nonnull final UUID id);

    boolean existsById(@Nonnull final UUID id);

    @Nonnull
    Page<CategoryJpaEntity> findByIsActiveTrue(@Nonnull final Pageable pageable);

    @Nonnull
    Optional<CategoryJpaEntity> findByNameIgnoreCase(@Nonnull final String name);

    boolean existsByNameIgnoreCase(@Nonnull final String name);

    @Nonnull
    Page<CategoryJpaEntity> findByNameContainingIgnoreCase(
        @Nonnull final String name,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    List<CategoryJpaEntity> findByIdIn(@Nonnull final List<UUID> ids);

    @Nonnull
    @Query("SELECT c FROM CategoryJpaEntity c JOIN c.products p WHERE p.id = :productId")
    List<CategoryJpaEntity> findByProductId(@Param("productId") @Nonnull final UUID productId);
}
