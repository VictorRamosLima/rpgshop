package rpgshop.application.command.cart;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddCartItemCommandTest {
    @Test
    void shouldCreateAddCartItemCommand() {
        final UUID customerId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final int quantity = 5;

        final AddCartItemCommand command = new AddCartItemCommand(customerId, productId, quantity);

        assertEquals(customerId, command.customerId());
        assertEquals(productId, command.productId());
        assertEquals(quantity, command.quantity());
    }
}
