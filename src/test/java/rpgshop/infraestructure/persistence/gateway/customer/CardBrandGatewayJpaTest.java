package rpgshop.infraestructure.persistence.gateway.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.domain.entity.customer.CardBrand;
import rpgshop.infraestructure.persistence.entity.customer.CardBrandJpaEntity;
import rpgshop.infraestructure.persistence.repository.customer.CardBrandRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardBrandGatewayJpaTest {
    @Mock
    private CardBrandRepository cardBrandRepository;

    @InjectMocks
    private CardBrandGatewayJpa cardBrandGatewayJpa;

    @Test
    void shouldFindById() {
        final UUID brandId = UUID.randomUUID();
        final Instant now = Instant.now();

        final CardBrandJpaEntity entity = CardBrandJpaEntity.builder()
            .id(brandId)
            .name("Visa")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(cardBrandRepository.findById(brandId)).thenReturn(Optional.of(entity));

        final Optional<CardBrand> result = cardBrandGatewayJpa.findById(brandId);

        assertTrue(result.isPresent());
        assertEquals(brandId, result.get().id());
        assertEquals("Visa", result.get().name());
        verify(cardBrandRepository, times(1)).findById(brandId);
    }

    @Test
    void shouldReturnEmptyWhenCardBrandNotFoundById() {
        final UUID brandId = UUID.randomUUID();

        when(cardBrandRepository.findById(brandId)).thenReturn(Optional.empty());

        final Optional<CardBrand> result = cardBrandGatewayJpa.findById(brandId);

        assertTrue(result.isEmpty());
        verify(cardBrandRepository, times(1)).findById(brandId);
    }

    @Test
    void shouldFindActive() {
        final UUID brandId1 = UUID.randomUUID();
        final UUID brandId2 = UUID.randomUUID();
        final Instant now = Instant.now();

        final CardBrandJpaEntity entity1 = CardBrandJpaEntity.builder()
            .id(brandId1)
            .name("Mastercard")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final CardBrandJpaEntity entity2 = CardBrandJpaEntity.builder()
            .id(brandId2)
            .name("Visa")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(cardBrandRepository.findByIsActiveTrueOrderByNameAsc()).thenReturn(List.of(entity1, entity2));

        final List<CardBrand> result = cardBrandGatewayJpa.findActive();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Mastercard", result.get(0).name());
        assertEquals("Visa", result.get(1).name());
        verify(cardBrandRepository, times(1)).findByIsActiveTrueOrderByNameAsc();
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveCardBrands() {
        when(cardBrandRepository.findByIsActiveTrueOrderByNameAsc()).thenReturn(List.of());

        final List<CardBrand> result = cardBrandGatewayJpa.findActive();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(cardBrandRepository, times(1)).findByIsActiveTrueOrderByNameAsc();
    }

    @Test
    void shouldReturnTrueWhenExistsByName() {
        final String name = "Visa";

        when(cardBrandRepository.existsByNameIgnoreCase(name)).thenReturn(true);

        final boolean result = cardBrandGatewayJpa.existsByName(name);

        assertTrue(result);
        verify(cardBrandRepository, times(1)).existsByNameIgnoreCase(name);
    }

    @Test
    void shouldReturnFalseWhenNotExistsByName() {
        final String name = "UnknownBrand";

        when(cardBrandRepository.existsByNameIgnoreCase(name)).thenReturn(false);

        final boolean result = cardBrandGatewayJpa.existsByName(name);

        assertFalse(result);
        verify(cardBrandRepository, times(1)).existsByNameIgnoreCase(name);
    }
}
