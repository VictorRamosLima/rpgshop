package rpgshop.application.command.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardOperatorDecisionTest {
    @Test
    void shouldCreateCardOperatorDecision() {
        final boolean approved = true;
        final String reason = "Transaction approved";

        final CardOperatorDecision decision = new CardOperatorDecision(approved, reason);

        assertTrue(decision.approved());
        assertEquals(reason, decision.reason());
    }
}

