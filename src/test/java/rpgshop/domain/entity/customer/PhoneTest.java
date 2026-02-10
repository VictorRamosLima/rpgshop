package rpgshop.domain.entity.customer;

import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.customer.constant.PhoneType;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PhoneTest {
    @Test
    void shouldCreatePhone() {
        final UUID id = UUID.randomUUID();
        final PhoneType type = PhoneType.MOBILE;
        final String areaCode = "11";
        final String number = "999999999";
        final boolean isActive = true;
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final Phone phone = Phone.builder()
            .id(id)
            .type(type)
            .areaCode(areaCode)
            .number(number)
            .isActive(isActive)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .deactivatedAt(null)
            .build();

        assertEquals(id, phone.id());
        assertEquals(type, phone.type());
        assertEquals(areaCode, phone.areaCode());
        assertEquals(number, phone.number());
        assertTrue(phone.isActive());
        assertEquals(createdAt, phone.createdAt());
        assertEquals(updatedAt, phone.updatedAt());
        assertNull(phone.deactivatedAt());
    }

    @Test
    void shouldCreatePhoneWithAllTypes() {
        for (PhoneType type : PhoneType.values()) {
            final Phone phone = Phone.builder().type(type).build();
            assertEquals(type, phone.type());
        }
    }

    @Test
    void shouldCreatePhoneWithNullValues() {
        final Phone phone = Phone.builder().build();

        assertNull(phone.id());
        assertNull(phone.type());
        assertNull(phone.areaCode());
        assertNull(phone.number());
        assertFalse(phone.isActive());
        assertNull(phone.createdAt());
        assertNull(phone.updatedAt());
        assertNull(phone.deactivatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final String originalNumber = "999999999";
        final String newNumber = "888888888";

        final Phone original = Phone.builder().number(originalNumber).build();
        final Phone modified = original.toBuilder().number(newNumber).build();

        assertEquals(originalNumber, original.number());
        assertEquals(newNumber, modified.number());
    }
}

