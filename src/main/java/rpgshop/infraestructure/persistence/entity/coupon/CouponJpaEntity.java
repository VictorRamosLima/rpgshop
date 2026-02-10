package rpgshop.infraestructure.persistence.entity.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "coupons", indexes = {
    @Index(name = "idx_coupons_customer_id_type_is_used", columnList = "customer_id, type, is_used"),
    @Index(
        name = "idx_coupons_customer_id_is_used_expires_at",
        columnList = "customer_id, is_used, expires_at"
    )
})
public final class CouponJpaEntity {
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

    @Column(name = "code", nullable = false, updatable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private CouponType type;

    @Column(name = "value", nullable = false, updatable = false, precision = 12, scale = 2)
    private BigDecimal value;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id", updatable = false)
    private CustomerJpaEntity customer;

    @Builder.Default
    @Column(name = "is_used", nullable = false)
    private boolean isUsed = false;

    @Column(name = "used_at")
    private Instant usedAt;

    @Column(name = "expires_at", updatable = false)
    private Instant expiresAt;
}
