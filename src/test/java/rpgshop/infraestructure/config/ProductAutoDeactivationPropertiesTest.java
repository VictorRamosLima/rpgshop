package rpgshop.infraestructure.config;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductAutoDeactivationPropertiesTest {
    @Test
    void shouldInstantiateAndExposeConfiguredValues() {
        final ProductAutoDeactivationProperties properties = new ProductAutoDeactivationProperties();

        properties.setEnabled(false);
        properties.setThreshold(new BigDecimal("75.00"));

        assertFalse(properties.isEnabled());
        assertEquals(new BigDecimal("75.00"), properties.getThreshold());
    }

    @Test
    void shouldExposeDefaultValues() {
        final ProductAutoDeactivationProperties properties = new ProductAutoDeactivationProperties();

        assertTrue(properties.isEnabled());
        assertEquals(new BigDecimal("50.00"), properties.getThreshold());
    }
}
