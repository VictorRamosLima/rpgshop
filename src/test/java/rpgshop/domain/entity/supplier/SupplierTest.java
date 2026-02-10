package rpgshop.domain.entity.supplier;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierTest {
    @Test
    void shouldCreateSupplier() {
        final UUID id = UUID.randomUUID();
        final String name = "RPG Supplier";
        final String legalName = "RPG Supplier Ltda";
        final String cnpj = "12345678000199";
        final String email = "contact@rpgsupplier.com";
        final String phone = "11999999999";
        final boolean isActive = true;
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final Supplier supplier = Supplier.builder()
            .id(id)
            .name(name)
            .legalName(legalName)
            .cnpj(cnpj)
            .email(email)
            .phone(phone)
            .isActive(isActive)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deactivatedAt(null)
            .build();

        assertEquals(id, supplier.id());
        assertEquals(name, supplier.name());
        assertEquals(legalName, supplier.legalName());
        assertEquals(cnpj, supplier.cnpj());
        assertEquals(email, supplier.email());
        assertEquals(phone, supplier.phone());
        assertTrue(supplier.isActive());
        assertEquals(createdAt, supplier.createdAt());
        assertEquals(updatedAt, supplier.updatedAt());
        assertNull(supplier.deactivatedAt());
    }

    @Test
    void shouldCreateSupplierWithNullValues() {
        final Supplier supplier = Supplier.builder().build();

        assertNull(supplier.id());
        assertNull(supplier.name());
        assertNull(supplier.legalName());
        assertNull(supplier.cnpj());
        assertNull(supplier.email());
        assertNull(supplier.phone());
        assertFalse(supplier.isActive());
        assertNull(supplier.createdAt());
        assertNull(supplier.updatedAt());
        assertNull(supplier.deactivatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final String originalName = "RPG Supplier";
        final String newName = "Board Game Supplier";

        final Supplier original = Supplier.builder().name(originalName).build();
        final Supplier modified = original.toBuilder().name(newName).build();

        assertEquals(originalName, original.name());
        assertEquals(newName, modified.name());
    }
}
