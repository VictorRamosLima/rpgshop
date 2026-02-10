package rpgshop.domain.entity.log;

import lombok.Builder;
import rpgshop.domain.entity.log.constant.OperationType;

import java.time.Instant;
import java.util.UUID;

@Builder(toBuilder = true)
public record TransactionLog(
    UUID id,
    String entityName,
    UUID entityId,
    OperationType operation,
    String responsibleUser,
    Instant timestamp,
    String previousData,
    String newData
) {}
