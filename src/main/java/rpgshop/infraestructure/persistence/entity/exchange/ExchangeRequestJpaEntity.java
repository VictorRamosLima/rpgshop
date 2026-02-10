package rpgshop.infraestructure.persistence.entity.exchange;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.infraestructure.persistence.entity.coupon.CouponJpaEntity;
import rpgshop.infraestructure.persistence.entity.order.OrderItemJpaEntity;
import rpgshop.infraestructure.persistence.entity.order.OrderJpaEntity;

import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;
import static rpgshop.domain.entity.exchange.constant.ExchangeStatus.REQUESTED;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "exchange_requests")
public final class ExchangeRequestJpaEntity {
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
    @JoinColumn(name = "order_item_id", nullable = false, updatable = false)
    private OrderItemJpaEntity orderItem;

    @Column(name = "quantity", nullable = false, updatable = false)
    private Integer quantity;

    @Builder.Default
    @Enumerated(STRING)
    @Column(name = "status", nullable = false)
    private ExchangeStatus status = REQUESTED;

    @Column(name = "reason", nullable = false, updatable = false)
    private String reason;

    @Column(name = "authorized_at")
    private Instant authorizedAt;

    @Column(name = "received_at")
    private Instant receivedAt;

    @Builder.Default
    @Column(name = "return_to_stock", nullable = false)
    private boolean returnToStock = false;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "coupon_id")
    private CouponJpaEntity coupon;
}
