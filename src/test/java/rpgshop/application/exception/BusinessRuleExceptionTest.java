package rpgshop.application.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusinessRuleExceptionTest {
    @Test
    void shouldCreateBusinessRuleExceptionWithMessage() {
        final BusinessRuleException exception = new BusinessRuleException("Regra invalida");

        assertEquals("Regra invalida", exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception.getStackTrace().length > 0);
    }
}
