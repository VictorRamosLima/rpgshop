package rpgshop.domain.entity.customer;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardBrandTest {
    @Test
    void shouldCreateCardBrand() {
        final UUID id = UUID.randomUUID();
        final String name = "Visa";
        final boolean isActive = true;
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final CardBrand cardBrand = CardBrand.builder()
            .id(id)
            .name(name)
            .isActive(isActive)
            .deactivatedAt(null)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, cardBrand.id());
        assertEquals(name, cardBrand.name());
        assertTrue(cardBrand.isActive());
        assertNull(cardBrand.deactivatedAt());
        assertEquals(createdAt, cardBrand.createdAt());
        assertEquals(updatedAt, cardBrand.updatedAt());
    }

    @Test
    void shouldCreateCardBrandWithNullValues() {
        final CardBrand cardBrand = CardBrand.builder().build();

        assertNull(cardBrand.id());
        assertNull(cardBrand.name());
        assertFalse(cardBrand.isActive());
        assertNull(cardBrand.deactivatedAt());
        assertNull(cardBrand.createdAt());
        assertNull(cardBrand.updatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final String originalName = "Visa";
        final String newName = "Mastercard";

        final CardBrand original = CardBrand.builder().name(originalName).build();
        final CardBrand modified = original.toBuilder().name(newName).build();

        assertEquals(originalName, original.name());
        assertEquals(newName, modified.name());
    }
}

