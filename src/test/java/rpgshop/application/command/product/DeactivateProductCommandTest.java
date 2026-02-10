package rpgshop.application.command.product;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeactivateProductCommandTest {
    @Test
    void shouldCreateDeactivateProductCommand() {
        final UUID productId = UUID.randomUUID();
        final String reason = "Product discontinued";
        final StatusChangeCategory category = StatusChangeCategory.DISCONTINUED;

        final DeactivateProductCommand command = new DeactivateProductCommand(productId, reason, category);

        assertEquals(productId, command.productId());
        assertEquals(reason, command.reason());
        assertEquals(category, command.category());
    }
}

