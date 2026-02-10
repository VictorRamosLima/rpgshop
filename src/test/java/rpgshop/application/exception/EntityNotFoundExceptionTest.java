package rpgshop.application.exception;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityNotFoundExceptionTest {
    @Test
    void shouldFormatMessageWithEntityAndId() {
        final UUID id = UUID.randomUUID();
        final EntityNotFoundException exception = new EntityNotFoundException("Product", id);
        assertEquals("'Product' not found with id '%s'".formatted(id), exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithExplicitMessage() {
        final EntityNotFoundException exception = new EntityNotFoundException("Nao encontrado");
        assertEquals("Nao encontrado", exception.getMessage());
    }
}
