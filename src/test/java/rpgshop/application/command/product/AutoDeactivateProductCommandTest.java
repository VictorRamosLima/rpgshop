package rpgshop.application.command.product;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AutoDeactivateProductCommandTest {
    @Test
    void shouldCreateAutoDeactivateProductCommand() {
        final String reason = "Low sales performance";
        final StatusChangeCategory category = StatusChangeCategory.OUT_OF_MARKET;
        final Instant when = Instant.now();

        final AutoDeactivateProductCommand command = new AutoDeactivateProductCommand(reason, category, when);

        assertEquals(reason, command.reason());
        assertEquals(category, command.category());
        assertEquals(when, command.when());
    }
}

