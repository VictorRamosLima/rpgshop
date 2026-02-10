package rpgshop.presentation.controller.log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import rpgshop.application.usecase.log.QueryTransactionLogsUseCase;
import rpgshop.domain.entity.log.TransactionLog;
import rpgshop.domain.entity.log.constant.OperationType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionLogControllerTest {
    @Mock
    private QueryTransactionLogsUseCase queryTransactionLogsUseCase;
    @Mock
    private Model model;

    @InjectMocks
    private TransactionLogController controller;

    @Test
    void shouldQueryLogsWithParsedDateRange() {
        final UUID entityId = UUID.randomUUID();
        final Page<TransactionLog> logs = new PageImpl<>(List.of(TransactionLog.builder().id(UUID.randomUUID()).build()));
        when(queryTransactionLogsUseCase.findByFilters(
            eq("Order"),
            eq(entityId),
            eq(OperationType.UPDATE),
            eq("admin"),
            eq(Instant.parse("2026-01-01T00:00:00Z")),
            eq(Instant.parse("2026-01-31T23:59:59Z")),
            eq(PageRequest.of(1, 20))
        )).thenReturn(logs);

        final String view = controller.list(
            "Order",
            entityId,
            OperationType.UPDATE,
            "admin",
            "2026-01-01",
            "2026-01-31",
            1,
            model
        );

        assertEquals("log/list", view);
        verify(model).addAttribute("logs", logs);
        verify(model).addAttribute(eq("operations"), any());
    }
}
