package rpgshop.infraestructure.persistence.entity.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
import rpgshop.domain.entity.customer.constant.PhoneType;

import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "phones", indexes = {
    @Index(name = "idx_phones_customer_id_is_active", columnList = "customer_id, is_active"),
    @Index(
        name = "idx_phones_customer_id_area_code_number",
        columnList = "customer_id, area_code, number"
    )
})
public final class PhoneJpaEntity {
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

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id", nullable = false, updatable = false)
    private CustomerJpaEntity customer;

    @Enumerated(STRING)
    @Column(name = "type", nullable = false)
    private PhoneType type;

    @Column(name = "area_code", nullable = false, length = 3)
    private String areaCode;

    @Column(name = "number", nullable = false, length = 10)
    private String number;

    @PreUpdate
    private void onUpdate() {
        if (!this.isActive) {
            this.deactivatedAt = Instant.now();
        }
    }
}
