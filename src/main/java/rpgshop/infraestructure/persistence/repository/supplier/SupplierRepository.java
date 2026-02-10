package rpgshop.infraestructure.persistence.repository.supplier;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.RepositoryDefinition;
import rpgshop.infraestructure.persistence.entity.supplier.SupplierJpaEntity;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = SupplierJpaEntity.class, idClass = UUID.class)
public interface SupplierRepository {
    @Nonnull
    SupplierJpaEntity save(@Nonnull final SupplierJpaEntity entity);

    @Nonnull
    Optional<SupplierJpaEntity> findById(@Nonnull final UUID id);

    @Nonnull
    Page<SupplierJpaEntity> findByIsActiveTrue(@Nonnull final Pageable pageable);

    boolean existsByCnpj(@Nonnull final String cnpj);
}
