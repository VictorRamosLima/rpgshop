package rpgshop.application.usecase.log;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.log.CreateTransactionLogCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.log.TransactionLogGateway;
import rpgshop.domain.entity.log.TransactionLog;

import java.time.Instant;
import java.util.UUID;

@Service
public class CreateTransactionLogUseCase {
    private final TransactionLogGateway transactionLogGateway;

    public CreateTransactionLogUseCase(final TransactionLogGateway transactionLogGateway) {
        this.transactionLogGateway = transactionLogGateway;
    }

    @Nonnull
    @Transactional
    public TransactionLog execute(@Nonnull final CreateTransactionLogCommand command) {
        if (command.entityName() == null || command.entityName().isBlank()) {
            throw new BusinessRuleException("O nome da entidade e obrigatorio");
        }
        if (command.entityId() == null) {
            throw new BusinessRuleException("O ID da entidade e obrigatorio");
        }
        if (command.operation() == null) {
            throw new BusinessRuleException("O tipo de operacao e obrigatorio");
        }
        if (command.responsibleUser() == null || command.responsibleUser().isBlank()) {
            throw new BusinessRuleException("O usuario responsavel e obrigatorio");
        }

        final TransactionLog log = TransactionLog.builder()
            .id(UUID.randomUUID())
            .entityName(command.entityName())
            .entityId(command.entityId())
            .operation(command.operation())
            .responsibleUser(command.responsibleUser())
            .previousData(command.previousData())
            .newData(command.newData())
            .timestamp(Instant.now())
            .build();

        return transactionLogGateway.save(log);
    }
}
