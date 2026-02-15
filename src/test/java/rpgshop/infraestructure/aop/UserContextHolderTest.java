package rpgshop.infraestructure.aop;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserContextHolderTest {
    @AfterEach
    void tearDown() {
        UserContextHolder.clear();
    }

    @Test
    void shouldReturnNullWhenNoUserIsSet() {
        assertNull(UserContextHolder.getCurrentUserId());
    }

    @Test
    void shouldReturnUserIdWhenSet() {
        final UUID userId = UUID.randomUUID();
        UserContextHolder.setCurrentUserId(userId);

        assertEquals(userId, UserContextHolder.getCurrentUserId());
    }

    @Test
    void shouldClearUserId() {
        final UUID userId = UUID.randomUUID();
        UserContextHolder.setCurrentUserId(userId);

        UserContextHolder.clear();

        assertNull(UserContextHolder.getCurrentUserId());
    }

    @Test
    void shouldOverwritePreviousUserId() {
        final UUID firstUserId = UUID.randomUUID();
        final UUID secondUserId = UUID.randomUUID();

        UserContextHolder.setCurrentUserId(firstUserId);
        assertEquals(firstUserId, UserContextHolder.getCurrentUserId());

        UserContextHolder.setCurrentUserId(secondUserId);
        assertEquals(secondUserId, UserContextHolder.getCurrentUserId());
    }
}
