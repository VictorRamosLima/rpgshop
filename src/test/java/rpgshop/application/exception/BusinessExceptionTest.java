package rpgshop.application.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusinessExceptionTest {
    @Test
    void shouldCreateBusinessExceptionWithMessageAndCause() {
        final RuntimeException cause = new RuntimeException("origem");
        final TestBusinessException exception = new TestBusinessException("Falha", cause);

        assertEquals("Falha", exception.getMessage());
        assertSame(cause, exception.getCause());
        assertTrue(exception.getStackTrace().length > 0);
    }

    private static final class TestBusinessException extends BusinessException {
        private TestBusinessException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
