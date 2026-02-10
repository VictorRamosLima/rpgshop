package rpgshop.infraestructure.persistence.entity.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.hibernate.annotations.UuidGenerator;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;
import rpgshop.domain.entity.product.constant.StatusChangeType;

import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "status_changes", indexes = {
    @Index(name = "idx_status_changes_product_id", columnList = "product_id")
})
public final class StatusChangeJpaEntity {
    @Id
    @UuidGenerator(style = VERSION_7)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Enumerated(STRING)
    @Column(name = "category", nullable = false)
    private StatusChangeCategory category;

    @Enumerated(STRING)
    @Column(name = "type", nullable = false)
    private StatusChangeType type;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private ProductJpaEntity product;
}
