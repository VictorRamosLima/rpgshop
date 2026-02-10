package rpgshop.infraestructure.persistence.repository.cart;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import rpgshop.infraestructure.persistence.entity.cart.CartItemJpaEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = CartItemJpaEntity.class, idClass = UUID.class)
public interface CartItemRepository {
    @Nonnull
    CartItemJpaEntity save(@Nonnull final CartItemJpaEntity entity);

    @Nonnull
    Optional<CartItemJpaEntity> findById(@Nonnull final UUID id);

    @Nonnull
    List<CartItemJpaEntity> findByCartId(@Nonnull final UUID cartId);

    @Nonnull
    Optional<CartItemJpaEntity> findByCartIdAndProductId(@Nonnull final UUID cartId, @Nonnull final UUID productId);

    @Query("""
        SELECT ci FROM CartItemJpaEntity ci
        WHERE ci.isBlocked = true
            AND ci.expiresAt IS NOT NULL
            AND ci.expiresAt <= :now
        """)
    List<CartItemJpaEntity> findExpiredBlockedItems(@Param("now") @Nonnull final Instant now);

    @Modifying
    @Query("DELETE FROM CartItemJpaEntity ci WHERE ci.cart.id = :cartId AND ci.product.id = :productId")
    int deleteByCartIdAndProductId(
        @Param("cartId") @Nonnull final UUID cartId,
        @Param("productId") @Nonnull final UUID productId
    );

    @Modifying
    @Query("DELETE FROM CartItemJpaEntity ci WHERE ci.cart.id = :cartId")
    int deleteAllByCartId(@Param("cartId") @Nonnull final UUID cartId);

    @Query("""
        SELECT COALESCE(SUM(ci.quantity), 0) FROM CartItemJpaEntity ci
        WHERE ci.product.id = :productId AND ci.isBlocked = true
        """)
    int sumBlockedQuantityByProductId(@Param("productId") @Nonnull final UUID productId);
}
