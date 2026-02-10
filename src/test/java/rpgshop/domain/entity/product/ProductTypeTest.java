package rpgshop.domain.entity.product;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductTypeTest {
    @Test
    void shouldCreateProductType() {
        final UUID id = UUID.randomUUID();
        final String name = "Book";
        final String description = "Physical books and manuals";
        final boolean isActive = true;
        final Instant deactivatedAt = null;
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final ProductType productType = ProductType.builder()
            .id(id)
            .name(name)
            .description(description)
            .isActive(isActive)
            .deactivatedAt(deactivatedAt)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, productType.id());
        assertEquals(name, productType.name());
        assertEquals(description, productType.description());
        assertTrue(productType.isActive());
        assertNull(productType.deactivatedAt());
        assertEquals(createdAt, productType.createdAt());
        assertEquals(updatedAt, productType.updatedAt());
    }

    @Test
    void shouldCreateProductTypeWithNullValues() {
        final ProductType productType = ProductType.builder().build();

        assertNull(productType.id());
        assertNull(productType.name());
        assertNull(productType.description());
        assertFalse(productType.isActive());
        assertNull(productType.deactivatedAt());
        assertNull(productType.createdAt());
        assertNull(productType.updatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final String originalName = "Book";
        final String newName = "Accessory";

        final ProductType original = ProductType.builder().name(originalName).build();
        final ProductType modified = original.toBuilder().name(newName).build();

        assertEquals(originalName, original.name());
        assertEquals(newName, modified.name());
    }
}

