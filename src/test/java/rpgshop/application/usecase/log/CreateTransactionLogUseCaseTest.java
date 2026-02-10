package rpgshop.application.usecase.log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.log.CreateTransactionLogCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.log.TransactionLogGateway;
import rpgshop.domain.entity.log.TransactionLog;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static rpgshop.domain.entity.log.constant.OperationType.UPDATE;

@ExtendWith(MockitoExtension.class)
class CreateTransactionLogUseCaseTest {
    @Mock
    private TransactionLogGateway transactionLogGateway;

    @InjectMocks
    private CreateTransactionLogUseCase useCase;

    @Test
    void shouldCreateTransactionLogWhenCommandIsValid() {
        final CreateTransactionLogCommand command = new CreateTransactionLogCommand(
            "Order",
            UUID.randomUUID(),
            UPDATE,
            "admin",
            "{\"status\":\"old\"}",
            "{\"status\":\"new\"}"
        );

        when(transactionLogGateway.save(any(TransactionLog.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        final TransactionLog saved = useCase.execute(command);

        assertEquals("Order", saved.entityName());
        assertEquals(UPDATE, saved.operation());
        assertEquals("admin", saved.responsibleUser());
    }

    @Test
    void shouldThrowWhenEntityNameIsMissing() {
        final CreateTransactionLogCommand command = new CreateTransactionLogCommand(
            " ",
            UUID.randomUUID(),
            UPDATE,
            "admin",
            null,
            null
        );

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }
}
