package rpgshop.domain.entity.product;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;
import rpgshop.domain.entity.product.constant.StatusChangeType;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatusChangeTest {
    @Test
    void shouldCreateStatusChange() {
        final UUID id = UUID.randomUUID();
        final String reason = "Out of stock";
        final StatusChangeCategory category = StatusChangeCategory.OUT_OF_MARKET;
        final StatusChangeType type = StatusChangeType.DEACTIVATE;
        final Instant createdAt = Instant.now();

        final StatusChange statusChange = StatusChange.builder()
            .id(id)
            .reason(reason)
            .category(category)
            .type(type)
            .createdAt(createdAt)
            .build();

        assertEquals(id, statusChange.id());
        assertEquals(reason, statusChange.reason());
        assertEquals(category, statusChange.category());
        assertEquals(type, statusChange.type());
        assertEquals(createdAt, statusChange.createdAt());
    }

    @Test
    void shouldCreateStatusChangeWithNullValues() {
        final StatusChange statusChange = StatusChange.builder().build();

        assertNull(statusChange.id());
        assertNull(statusChange.reason());
        assertNull(statusChange.category());
        assertNull(statusChange.type());
        assertNull(statusChange.createdAt());
    }

    @Test
    void shouldReturnTrueForIsActiveWhenTypeIsActivate() {
        final StatusChange statusChange = StatusChange.builder()
            .type(StatusChangeType.ACTIVATE)
            .build();

        assertTrue(statusChange.isActive());
    }

    @Test
    void shouldReturnFalseForIsActiveWhenTypeIsDeactivate() {
        final StatusChange statusChange = StatusChange.builder()
            .type(StatusChangeType.DEACTIVATE)
            .build();

        assertFalse(statusChange.isActive());
    }

    @Test
    void shouldCreateDeactivateStatusChange() {
        final String reason = "Product discontinued";
        final StatusChangeCategory category = StatusChangeCategory.DISCONTINUED;

        final StatusChange statusChange = StatusChange.deactivate(reason, category);

        assertEquals(reason, statusChange.reason());
        assertEquals(category, statusChange.category());
        assertEquals(StatusChangeType.DEACTIVATE, statusChange.type());
        assertFalse(statusChange.isActive());
    }

    @Test
    void shouldCreateActivateStatusChange() {
        final String reason = "Product back in stock";
        final StatusChangeCategory category = StatusChangeCategory.RESTOCKED;

        final StatusChange statusChange = StatusChange.activate(reason, category);

        assertEquals(reason, statusChange.reason());
        assertEquals(category, statusChange.category());
        assertEquals(StatusChangeType.ACTIVATE, statusChange.type());
        assertTrue(statusChange.isActive());
    }

    @Test
    void shouldCreateStatusChangeUsingStaticFactory() {
        final String reason = "Supplier resumed";
        final StatusChangeCategory category = StatusChangeCategory.SUPPLIER_RESUMED;
        final StatusChangeType type = StatusChangeType.ACTIVATE;

        final StatusChange statusChange = StatusChange.create(reason, category, type);

        assertEquals(reason, statusChange.reason());
        assertEquals(category, statusChange.category());
        assertEquals(type, statusChange.type());
    }

    @Test
    void shouldCreateStatusChangeWithAllCategories() {
        for (StatusChangeCategory category : StatusChangeCategory.values()) {
            final StatusChange statusChange = StatusChange.builder().category(category).build();
            assertEquals(category, statusChange.category());
        }
    }

    @Test
    void shouldCreateStatusChangeWithAllTypes() {
        for (StatusChangeType type : StatusChangeType.values()) {
            final StatusChange statusChange = StatusChange.builder().type(type).build();
            assertEquals(type, statusChange.type());
        }
    }
}

