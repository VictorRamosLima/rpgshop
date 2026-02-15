package rpgshop.infraestructure.persistence.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
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
import rpgshop.domain.entity.user.constant.UserType;

import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "users", indexes = {
    @Index(name = "idx_users_email", columnList = "email"),
    @Index(name = "idx_users_user_type", columnList = "user_type"),
    @Index(name = "idx_users_user_type_id", columnList = "user_type, user_type_id"),
    @Index(name = "idx_users_is_active", columnList = "is_active")
})
public final class UserJpaEntity {
    @Id
    @UuidGenerator(style = VERSION_7)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(STRING)
    @Column(name = "user_type", nullable = false, updatable = false)
    private UserType userType;

    @Column(name = "user_type_id", nullable = false, updatable = false)
    private UUID userTypeId;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "deactivated_at")
    private Instant deactivatedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PreUpdate
    private void onUpdate() {
        if (!this.isActive && this.deactivatedAt == null) {
            this.deactivatedAt = Instant.now();
        }
    }
}
