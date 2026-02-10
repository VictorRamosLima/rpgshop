package rpgshop.infraestructure.persistence.entity.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import rpgshop.domain.entity.log.constant.OperationType;
import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static org.hibernate.annotations.UuidGenerator.Style.VERSION_7;
import static org.hibernate.type.SqlTypes.JSON;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "transaction_logs", indexes = {
    @Index(name = "idx_transaction_logs_timestamp", columnList = "timestamp"),
    @Index(name = "idx_transaction_logs_entity_name_entity_id", columnList = "entity_name, entity_id"),
    @Index(name = "idx_transaction_logs_operation", columnList = "operation"),
    @Index(name = "idx_transaction_logs_responsible_user", columnList = "responsible_user")
})
public final class TransactionLogJpaEntity {
    @Id
    @UuidGenerator(style = VERSION_7)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "entity_name", nullable = false, updatable = false)
    private String entityName;

    @Column(name = "entity_id", nullable = false, updatable = false)
    private UUID entityId;

    @Enumerated(STRING)
    @Column(name = "operation", nullable = false, updatable = false)
    private OperationType operation;

    @Column(name = "responsible_user", nullable = false, updatable = false)
    private String responsibleUser;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private Instant timestamp;

    @JdbcTypeCode(JSON)
    @Column(name = "previous_data", updatable = false, columnDefinition = "JSONB")
    private JsonNode previousData;

    @JdbcTypeCode(JSON)
    @Column(name = "new_data", nullable = false, updatable = false, columnDefinition = "JSONB")
    private JsonNode newData;

    @PrePersist
    private void onPrePersist() {
        this.timestamp = Instant.now();
    }
}
