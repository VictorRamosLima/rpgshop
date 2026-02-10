package rpgshop.domain.entity.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PricingGroupTest {
    @Test
    void shouldCreatePricingGroup() {
        final UUID id = UUID.randomUUID();
        final String name = "Standard";
        final BigDecimal marginPercentage = new BigDecimal("20.00");
        final boolean isActive = true;
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final PricingGroup pricingGroup = PricingGroup.builder()
            .id(id)
            .name(name)
            .marginPercentage(marginPercentage)
            .isActive(isActive)
            .deactivatedAt(null)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, pricingGroup.id());
        assertEquals(name, pricingGroup.name());
        assertEquals(marginPercentage, pricingGroup.marginPercentage());
        assertTrue(pricingGroup.isActive());
        assertNull(pricingGroup.deactivatedAt());
        assertEquals(createdAt, pricingGroup.createdAt());
        assertEquals(updatedAt, pricingGroup.updatedAt());
    }

    @Test
    void shouldCreatePricingGroupWithNullValues() {
        final PricingGroup pricingGroup = PricingGroup.builder().build();

        assertNull(pricingGroup.id());
        assertNull(pricingGroup.name());
        assertNull(pricingGroup.marginPercentage());
        assertFalse(pricingGroup.isActive());
        assertNull(pricingGroup.deactivatedAt());
        assertNull(pricingGroup.createdAt());
        assertNull(pricingGroup.updatedAt());
    }

    @Test
    void shouldUseToBuilder() {
        final String originalName = "Standard";
        final String newName = "Premium";

        final PricingGroup original = PricingGroup.builder().name(originalName).build();
        final PricingGroup modified = original.toBuilder().name(newName).build();

        assertEquals(originalName, original.name());
        assertEquals(newName, modified.name());
    }
}

