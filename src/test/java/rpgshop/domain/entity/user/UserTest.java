package rpgshop.domain.entity.user;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserTest {
    @Test
    void shouldCreateUser() {
        final UUID id = UUID.randomUUID();
        final String name = "Admin";
        final String email = "admin@rpgshop.com";
        final Instant now = Instant.now();

        final User user = User.builder()
            .id(id)
            .name(name)
            .email(email)
            .createdAt(now)
            .updatedAt(now)
            .build();

        assertEquals(id, user.id());
        assertEquals(name, user.name());
        assertEquals(email, user.email());
        assertEquals(now, user.createdAt());
        assertEquals(now, user.updatedAt());
    }

    @Test
    void shouldCreateUserWithNullValues() {
        final User user = User.builder().build();

        assertNull(user.id());
        assertNull(user.name());
        assertNull(user.email());
        assertNull(user.createdAt());
        assertNull(user.updatedAt());
    }

    @Test
    void shouldCreateUserWithToBuilder() {
        final User original = User.builder()
            .id(UUID.randomUUID())
            .name("Original")
            .email("original@rpgshop.com")
            .build();

        final User updated = original.toBuilder()
            .name("Updated")
            .build();

        assertEquals(original.id(), updated.id());
        assertEquals("Updated", updated.name());
        assertEquals(original.email(), updated.email());
    }
}
