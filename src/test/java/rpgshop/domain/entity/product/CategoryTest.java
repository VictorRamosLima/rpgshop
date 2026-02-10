package rpgshop.domain.entity.product;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryTest {
    @Test
    void shouldCreateCategory() {
        final UUID id = UUID.randomUUID();
        final String name = "RPG Books";
        final String description = "Books for role-playing games";
        final List<Product> products = List.of();
        final boolean isActive = true;
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final Category category = Category.builder()
            .id(id)
            .name(name)
            .description(description)
            .products(products)
            .isActive(isActive)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deactivatedAt(null)
            .build();

        assertEquals(id, category.id());
        assertEquals(name, category.name());
        assertEquals(description, category.description());
        assertEquals(products, category.products());
        assertTrue(category.isActive());
        assertEquals(createdAt, category.createdAt());
        assertEquals(updatedAt, category.updatedAt());
        assertNull(category.deactivatedAt());
    }

    @Test
    void shouldCreateCategoryWithNullValues() {
        final Category category = Category.builder().build();

        assertNull(category.id());
        assertNull(category.name());
        assertNull(category.description());
        assertNull(category.products());
        assertFalse(category.isActive());
        assertNull(category.createdAt());
        assertNull(category.updatedAt());
        assertNull(category.deactivatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final String originalName = "RPG Books";
        final String newName = "Board Games";

        final Category original = Category.builder().name(originalName).build();
        final Category modified = original.toBuilder().name(newName).build();

        assertEquals(originalName, original.name());
        assertEquals(newName, modified.name());
    }
}

