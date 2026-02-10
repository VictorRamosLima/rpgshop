package rpgshop.infraestructure.config;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardOperatorPropertiesTest {
    @Test
    void shouldInstantiateAndExposeConfiguredValues() {
        final CardOperatorProperties properties = new CardOperatorProperties();

        properties.setEnabled(false);
        properties.setMaxAmountPerCard(new BigDecimal("8000.00"));
        properties.setRejectedCardSuffixes(List.of("1111", "2222"));

        assertFalse(properties.isEnabled());
        assertEquals(new BigDecimal("8000.00"), properties.getMaxAmountPerCard());
        assertEquals(List.of("1111", "2222"), properties.getRejectedCardSuffixes());
    }

    @Test
    void shouldExposeDefaultValues() {
        final CardOperatorProperties properties = new CardOperatorProperties();

        assertTrue(properties.isEnabled());
        assertEquals(new BigDecimal("5000.00"), properties.getMaxAmountPerCard());
        assertEquals(List.of("0000"), properties.getRejectedCardSuffixes());
    }
}
