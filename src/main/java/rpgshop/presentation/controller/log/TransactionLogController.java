package rpgshop.presentation.controller.log;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rpgshop.application.usecase.log.QueryTransactionLogsUseCase;
import rpgshop.domain.entity.log.TransactionLog;
import rpgshop.domain.entity.log.constant.OperationType;

import java.time.Instant;
import java.util.UUID;

@Controller
@RequestMapping("/logs")
public class TransactionLogController {
    private final QueryTransactionLogsUseCase queryTransactionLogsUseCase;

    public TransactionLogController(final QueryTransactionLogsUseCase queryTransactionLogsUseCase) {
        this.queryTransactionLogsUseCase = queryTransactionLogsUseCase;
    }

    @GetMapping
    public String list(
        @RequestParam(required = false) String entityName,
        @RequestParam(required = false) UUID entityId,
        @RequestParam(required = false) OperationType operation,
        @RequestParam(required = false) String responsibleUser,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(defaultValue = "0") int page,
        Model model
    ) {
        final Instant start = startDate != null && !startDate.isBlank()
            ? Instant.parse(startDate + "T00:00:00Z") : null;
        final Instant end = endDate != null && !endDate.isBlank()
            ? Instant.parse(endDate + "T23:59:59Z") : null;

        final Page<TransactionLog> logs = queryTransactionLogsUseCase.findByFilters(
            entityName, entityId, operation, responsibleUser, start, end, PageRequest.of(page, 20)
        );
        model.addAttribute("logs", logs);
        model.addAttribute("operations", OperationType.values());
        return "log/list";
    }
}
