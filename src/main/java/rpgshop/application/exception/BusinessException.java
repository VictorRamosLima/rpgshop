package rpgshop.application.exception;

public abstract class BusinessException extends RuntimeException {
    public BusinessException(final String message) {
        this(message, null);
    }

    public BusinessException(final String message, final Throwable throwable) {
        super(message, throwable, false, true);
    }
}
