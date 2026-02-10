package rpgshop.application.command.analysis;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalesAnalysisFilterTest {
    @Test
    void shouldCreateSalesAnalysisFilter() {
        UUID productId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        Instant startDate = Instant.now();
        Instant endDate = Instant.now().plusSeconds(3600);

        final SalesAnalysisFilter filter = new SalesAnalysisFilter(
            productId,
            categoryId,
            startDate,
            endDate
        );

        assertEquals(productId, filter.productId());
        assertEquals(categoryId, filter.categoryId());
        assertEquals(startDate, filter.startDate());
        assertEquals(endDate, filter.endDate());
    }
}

