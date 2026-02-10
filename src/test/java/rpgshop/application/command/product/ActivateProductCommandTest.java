package rpgshop.application.command.product;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActivateProductCommandTest {
    @Test
    void shouldCreateActivateProductCommand() {
        final UUID productId = UUID.randomUUID();
        final String reason = "Product back in stock";
        final StatusChangeCategory category = StatusChangeCategory.RESTOCKED;

        final ActivateProductCommand command = new ActivateProductCommand(productId, reason, category);

        assertEquals(productId, command.productId());
        assertEquals(reason, command.reason());
        assertEquals(category, command.category());
    }
}

