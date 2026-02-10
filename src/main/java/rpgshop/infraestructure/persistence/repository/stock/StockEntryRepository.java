package rpgshop.infraestructure.persistence.repository.stock;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import rpgshop.infraestructure.persistence.entity.stock.StockEntryJpaEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RepositoryDefinition(domainClass = StockEntryJpaEntity.class, idClass = UUID.class)
public interface StockEntryRepository {
    @Nonnull
    StockEntryJpaEntity save(@Nonnull final StockEntryJpaEntity entity);

    @Nonnull
    Optional<StockEntryJpaEntity> findById(@Nonnull final UUID id);

    void deleteById(@Nonnull final UUID id);

    boolean existsById(@Nonnull final UUID id);

    @Nonnull
    Page<StockEntryJpaEntity> findByProductId(
        @Nonnull final UUID productId,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    Page<StockEntryJpaEntity> findBySupplierId(
        @Nonnull final UUID supplierId,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    Page<StockEntryJpaEntity> findByEntryDateBetween(
        @Nonnull final LocalDate startDate,
        @Nonnull final LocalDate endDate,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    Page<StockEntryJpaEntity> findByProductIdAndEntryDateBetween(
        @Nonnull final UUID productId,
        @Nonnull final LocalDate startDate,
        @Nonnull final LocalDate endDate,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    Page<StockEntryJpaEntity> findByIsReentryTrue(@Nonnull final Pageable pageable);

    @Nonnull
    Page<StockEntryJpaEntity> findByProductIdAndIsReentryTrue(
        @Nonnull final UUID productId,
        @Nonnull final Pageable pageable
    );

    @Nonnull
    @Query("SELECT MAX(se.costValue) FROM StockEntryJpaEntity se WHERE se.product.id = :productId")
    Optional<BigDecimal> findMaxCostValueByProductId(@Param("productId") @Nonnull final UUID productId);

    @Query("SELECT COALESCE(SUM(se.quantity), 0) FROM StockEntryJpaEntity se WHERE se.product.id = :productId")
    int sumQuantityByProductId(@Param("productId") @Nonnull final UUID productId);
}
