package rpgshop.infraestructure.persistence.entity.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "credit_cards")
public final class CreditCardJpaEntity {
    @Id
    @UuidGenerator(style = VERSION_7)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "deactivated_at")
    private Instant deactivatedAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, updatable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id", nullable = false, updatable = false)
    private CustomerJpaEntity customer;

    @Column(name = "card_number", nullable = false, updatable = false)
    private String cardNumber;

    @Column(name = "printed_name", nullable = false)
    private String printedName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "card_brand_id", nullable = false)
    private CardBrandJpaEntity cardBrand;

    @Column(name = "security_code", nullable = false, updatable = false)
    private String securityCode;

    @Column(name = "is_preferred", nullable = false)
    private boolean isPreferred = false;

    @PreUpdate
    private void onUpdate() {
        if (!this.isActive) {
            this.deactivatedAt = Instant.now();
        }
    }
}
