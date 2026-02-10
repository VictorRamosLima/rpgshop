package rpgshop.infraestructure.persistence.entity.order;

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
import rpgshop.infraestructure.persistence.entity.coupon.CouponJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CreditCardJpaEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "order_payments", indexes = {
    @Index(name = "idx_order_payments_order_id", columnList = "order_id"),
    @Index(
        name = "idx_order_payments_coupon_id",
        columnList = "coupon_id",
        options = "WHERE coupon_id IS NOT NULL"
    )
})
public final class OrderPaymentJpaEntity {
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
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private OrderJpaEntity order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "credit_card_id", updatable = false)
    private CreditCardJpaEntity creditCard;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "coupon_id", updatable = false)
    private CouponJpaEntity coupon;

    @Column(name = "amount", nullable = false, updatable = false, precision = 12, scale = 2)
    private BigDecimal amount;
}
