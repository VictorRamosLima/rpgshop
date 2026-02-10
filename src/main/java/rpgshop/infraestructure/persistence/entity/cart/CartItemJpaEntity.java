package rpgshop.infraestructure.persistence.entity.cart;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;

import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "cart_items", indexes = {
    @Index(name = "idx_cart_items_cart_id", columnList = "cart_id"),
    @Index(name = "idx_cart_items_cart_id_product_id", columnList = "cart_id, product_id"),
    @Index(
        name = "idx_cart_items_blocked_expires",
        columnList = "is_blocked, expires_at",
        options = "WHERE is_blocked = TRUE AND expires_at IS NOT NULL"
    ),
    @Index(
        name = "idx_cart_items_product_id_is_blocked",
        columnList = "product_id, is_blocked",
        options = "WHERE is_blocked = TRUE"
    )
})
public final class CartItemJpaEntity {
    @Id
    @UuidGenerator(style = VERSION_7)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cart_id", nullable = false, updatable = false)
    private CartJpaEntity cart;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private ProductJpaEntity product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Builder.Default
    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked = true;

    @Column(name = "blocked_at")
    private Instant blockedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;
}
