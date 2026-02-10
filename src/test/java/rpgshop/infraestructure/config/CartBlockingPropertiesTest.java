package rpgshop.infraestructure.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CartBlockingPropertiesTest {
    @Test
    void shouldInstantiateAndExposeConfiguredValues() {
        final CartBlockingProperties properties = new CartBlockingProperties();

        properties.setTimeoutMinutes(45);
        properties.setAutoReleaseEnabled(false);

        assertEquals(45, properties.getTimeoutMinutes());
        assertFalse(properties.isAutoReleaseEnabled());
    }

    @Test
    void shouldExposeDefaultValues() {
        final CartBlockingProperties properties = new CartBlockingProperties();

        assertEquals(30, properties.getTimeoutMinutes());
        assertTrue(properties.isAutoReleaseEnabled());
    }
}
