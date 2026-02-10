package rpgshop.infraestructure.persistence.entity.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import rpgshop.domain.entity.order.constant.OrderStatus;
import rpgshop.infraestructure.persistence.entity.customer.AddressJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;
import static rpgshop.domain.entity.order.constant.OrderStatus.PROCESSING;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "orders", indexes = {
    @Index(name = "idx_orders_customer_id", columnList = "customer_id"),
    @Index(name = "idx_orders_status", columnList = "status"),
    @Index(name = "idx_orders_customer_id_purchased_at", columnList = "customer_id, purchased_at"),
    @Index(
        name = "idx_orders_status_purchased_at",
        columnList = "status, purchased_at",
        options = "WHERE status != 'REJECTED'"
    )
})
public final class OrderJpaEntity {
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

    @Column(name = "order_code", nullable = false, updatable = false, unique = true)
    private String orderCode;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id", nullable = false, updatable = false)
    private CustomerJpaEntity customer;

    @Builder.Default
    @Enumerated(STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = PROCESSING;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private AddressJpaEntity deliveryAddress;

    @Column(name = "freight_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal freightCost;

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "purchased_at")
    private Instant purchasedAt;

    @Column(name = "dispatched_at")
    private Instant dispatchedAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true, fetch = LAZY)
    private List<OrderItemJpaEntity> items = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true, fetch = LAZY)
    private List<OrderPaymentJpaEntity> payments = new ArrayList<>();
}
