package rpgshop.infraestructure.persistence.repository.product;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

@RepositoryDefinition(domainClass = ProductJpaEntity.class, idClass = UUID.class)
public interface ProductRepository {
    ProductJpaEntity save(final ProductJpaEntity entity);
    Optional<ProductJpaEntity> findById(final UUID id);
    void deleteById(final UUID id);
    boolean existsById(final UUID id);

    @Query("""
        SELECT DISTINCT p FROM ProductJpaEntity p
        LEFT JOIN p.productType t
        LEFT JOIN p.pricingGroup pg
        LEFT JOIN p.categories c
        WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:productTypeId IS NULL OR t.id = :productTypeId)
            AND (:categoryId IS NULL OR c.id = :categoryId)
            AND (:pricingGroupId IS NULL OR pg.id = :pricingGroupId)
            AND (:sku IS NULL OR p.sku = :sku)
            AND (:barcode IS NULL OR p.barcode = :barcode)
            AND (:isActive IS NULL OR p.isActive = :isActive)
            AND (:minPrice IS NULL OR p.salePrice >= :minPrice)
            AND (:maxPrice IS NULL OR p.salePrice <= :maxPrice)
        """)
    Page<ProductJpaEntity> findByFilters(
        @Param("name") @Nullable final String name,
        @Param("productTypeId") @Nullable final UUID productTypeId,
        @Param("categoryId") @Nullable final UUID categoryId,
        @Param("pricingGroupId") @Nullable final UUID pricingGroupId,
        @Param("sku") @Nullable final String sku,
        @Param("barcode") @Nullable final String barcode,
        @Param("isActive") @Nullable final Boolean isActive,
        @Param("minPrice") @Nullable final BigDecimal minPrice,
        @Param("maxPrice") @Nullable final BigDecimal maxPrice,
        @Nullable final Pageable pageable
    );

    Optional<ProductJpaEntity> findBySku(@Nonnull final String sku);

    Optional<ProductJpaEntity> findByBarcode(@Nonnull final String barcode);

    boolean existsBySku(@Nonnull final String sku);

    boolean existsByBarcode(@Nonnull final String barcode);

    @Lock(PESSIMISTIC_WRITE)
    @Query("""
        SELECT p.id FROM ProductJpaEntity p
        WHERE p.isActive = true
            AND p.stockQuantity = 0
            AND (p.salePrice IS NULL OR p.salePrice < :threshold)
        """)
    List<UUID> findEligibleForAutoInactivation(@Param("threshold") @Nonnull final BigDecimal threshold);

    @Modifying
    @Query("""
        UPDATE ProductJpaEntity p
        SET p.isActive = false,
        p.deactivatedAt = :now,
        p.updatedAt = :now
        WHERE p.id IN :ids
        """)
    int bulkDeactivateByIds(final List<UUID> ids, final Instant now);

    Page<ProductJpaEntity> findByIsActiveTrue(final Pageable pageable);

    Page<ProductJpaEntity> findByIsActiveFalse(final Pageable pageable);

    Page<ProductJpaEntity> findByStockQuantityGreaterThan(final Integer quantity, final Pageable pageable);

    Page<ProductJpaEntity> findByStockQuantityEquals(final Integer quantity, final Pageable pageable);

    Page<ProductJpaEntity> findByTypeId(final UUID typeId, final Pageable pageable);

    Page<ProductJpaEntity> findByPricingGroupId(final UUID pricingGroupId, final Pageable pageable);

    @Query("SELECT p FROM ProductJpaEntity p JOIN p.categories c WHERE c.id = :categoryId")
    Page<ProductJpaEntity> findByCategoryId(@Param("categoryId") final UUID categoryId, final Pageable pageable);

    @Query("""
        SELECT MAX(se.costValue)
        FROM StockEntryJpaEntity se
        WHERE se.product.id = :productId
        """)
    Optional<BigDecimal> findMaxCostByProductId(@Param("productId") final UUID productId);
}
