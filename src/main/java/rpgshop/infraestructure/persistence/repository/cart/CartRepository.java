package rpgshop.infraestructure.persistence.repository.cart;

import jakarta.annotation.Nonnull;
import org.springframework.data.repository.RepositoryDefinition;
import rpgshop.infraestructure.persistence.entity.cart.CartJpaEntity;

import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = CartJpaEntity.class, idClass = UUID.class)
public interface CartRepository {
    @Nonnull
    CartJpaEntity save(@Nonnull final CartJpaEntity entity);

    @Nonnull
    Optional<CartJpaEntity> findById(@Nonnull final UUID id);

    @Nonnull
    Optional<CartJpaEntity> findByCustomerId(@Nonnull final UUID customerId);

    boolean existsByCustomerId(@Nonnull final UUID customerId);
}
