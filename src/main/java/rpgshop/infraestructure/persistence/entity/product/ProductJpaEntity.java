package rpgshop.infraestructure.persistence.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "products", indexes = {
    @Index(name = "idx_products_is_active", columnList = "is_active"),
    @Index(name = "idx_products_type_id", columnList = "type_id"),
    @Index(name = "idx_products_pricing_group_id", columnList = "pricing_group_id"),
    @Index(name = "idx_products_sale_price", columnList = "sale_price"),
    @Index(name = "idx_products_name_lower", columnList = "name"),
    @Index(
        name = "idx_products_auto_inactivation",
        columnList = "is_active, stock_quantity, sale_price",
        options = "WHERE is_active = TRUE AND stock_quantity = 0"
    )
})
public final class ProductJpaEntity {
    @Id
    @UuidGenerator(style = VERSION_7)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "deactivated_at")
    private Instant deactivatedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private ProductTypeJpaEntity productType;

    @ManyToMany
    @JoinTable(
        name = "product_categories",
        joinColumns = @JoinColumn(name = "product_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "category_id", nullable = false),
        indexes = {
            @Index(name = "idx_product_categories_category_id", columnList = "category_id"),
            @Index(name = "idx_product_categories_product_id", columnList = "product_id")
        }
    )
    private List<CategoryJpaEntity> categories;

    @Column(name = "height", precision = 10, scale = 2)
    private BigDecimal height;

    @Column(name = "width", precision = 10, scale = 2)
    private BigDecimal width;

    @Column(name = "depth", precision = 10, scale = 2)
    private BigDecimal depth;

    @Column(name = "weight", precision = 10, scale = 3)
    private BigDecimal weight;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "pricing_group_id", nullable = false)
    private PricingGroupJpaEntity pricingGroup;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "sku", unique = true)
    private String sku;

    @Column(name = "sale_price", precision = 12, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "cost_price", precision = 12, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @OneToMany(mappedBy = "product", cascade = ALL, orphanRemoval = true, fetch = LAZY)
    private List<StatusChangeJpaEntity> statusChanges;

    @Column(name = "minimum_sale_threshold", precision = 12, scale = 2)
    private BigDecimal minimumSaleThreshold;

    @PreUpdate
    private void onUpdate() {
        if (!this.isActive) {
            this.deactivatedAt = Instant.now();
        }
    }
}
