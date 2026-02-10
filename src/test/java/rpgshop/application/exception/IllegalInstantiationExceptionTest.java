package rpgshop.application.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IllegalInstantiationExceptionTest {
    @Test
    void shouldCreateMessageWithClassSimpleName() {
        final IllegalInstantiationException exception = new IllegalInstantiationException(String.class);
        assertEquals("Instantiating class 'String' is illegal.", exception.getMessage());
    }
}
