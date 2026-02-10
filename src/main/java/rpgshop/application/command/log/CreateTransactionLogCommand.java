package rpgshop.application.command.log;

import rpgshop.domain.entity.log.constant.OperationType;

import java.util.UUID;

public record CreateTransactionLogCommand(
    String entityName,
    UUID entityId,
    OperationType operation,
    String responsibleUser,
    String previousData,
    String newData
) {}
