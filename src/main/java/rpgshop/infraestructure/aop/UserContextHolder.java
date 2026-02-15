package rpgshop.infraestructure.aop;

import java.util.UUID;

public final class UserContextHolder {
    private static final ThreadLocal<UUID> currentUserId = new ThreadLocal<>();

    private UserContextHolder() {}

    public static void setCurrentUserId(final UUID userId) {
        currentUserId.set(userId);
    }

    public static UUID getCurrentUserId() {
        return currentUserId.get();
    }

    public static void clear() {
        currentUserId.remove();
    }
}
