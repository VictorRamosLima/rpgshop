package rpgshop.infraestructure.persistence.entity.supplier;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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

import java.time.Instant;
import java.util.UUID;

import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "suppliers", indexes = {
    @Index(name = "idx_suppliers_is_active", columnList = "is_active")
})
public final class SupplierJpaEntity {
    @Id
    @UuidGenerator(style = VERSION_7)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "deactivated_at")
    private Instant deactivatedAt;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "legal_name")
    private String legalName;

    @Column(name = "cnpj", unique = true)
    private String cnpj;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @PreUpdate
    private void onUpdate() {
        if (!this.isActive) {
            this.deactivatedAt = Instant.now();
        }
    }
}
