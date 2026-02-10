package rpgshop.application.exception;

import java.util.UUID;

public final class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(final String entity, final UUID id) {
        super("'%s' not found with id '%s'".formatted(entity, id));
    }

    public EntityNotFoundException(final String message) {
        super(message);
    }
}
