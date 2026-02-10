package rpgshop.infraestructure.persistence.entity.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import rpgshop.domain.entity.customer.constant.Gender;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "customers", indexes = {
    @Index(name = "idx_customers_is_active", columnList = "is_active"),
    @Index(name = "idx_customers_gender", columnList = "gender")
})
public final class CustomerJpaEntity {
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

    @Enumerated(STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "cpf", nullable = false, updatable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Builder.Default
    @Column(name = "ranking", nullable = false, precision = 5, scale = 2)
    private BigDecimal ranking = BigDecimal.ZERO;

    @Column(name = "customer_code", nullable = false, updatable = false, unique = true)
    private String customerCode;

    @Builder.Default
    @OneToMany(mappedBy = "customer", cascade = ALL, orphanRemoval = true, fetch = LAZY)
    private List<PhoneJpaEntity> phones = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "customer", cascade = ALL, orphanRemoval = true, fetch = LAZY)
    private List<AddressJpaEntity> addresses = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "customer", cascade = ALL, orphanRemoval = true, fetch = LAZY)
    private List<CreditCardJpaEntity> creditCards = new ArrayList<>();

    @PreUpdate
    private void onUpdate() {
        if (!this.isActive) {
            this.deactivatedAt = Instant.now();
        }
    }
}
