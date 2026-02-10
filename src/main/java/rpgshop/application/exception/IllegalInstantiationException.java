package rpgshop.application.exception;

public final class IllegalInstantiationException extends RuntimeException {
    public IllegalInstantiationException(final Class<?> instantiatedClass) {
        super(String.format("Instantiating class '%s' is illegal.", instantiatedClass.getSimpleName()));
    }
}
