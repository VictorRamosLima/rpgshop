package rpgshop.infraestructure.persistence.gateway.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.infraestructure.persistence.entity.product.PricingGroupJpaEntity;
import rpgshop.infraestructure.persistence.gateway.PricingGroupGatewayJpa;
import rpgshop.infraestructure.persistence.repository.product.PricingGroupRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PricingGroupGatewayJpaTest {
    @Mock
    private PricingGroupRepository pricingGroupRepository;

    @InjectMocks
    private PricingGroupGatewayJpa pricingGroupGatewayJpa;

    @Test
    void shouldFindById() {
        final UUID pricingGroupId = UUID.randomUUID();
        final Instant now = Instant.now();

        final PricingGroupJpaEntity entity = PricingGroupJpaEntity.builder()
            .id(pricingGroupId)
            .name("Standard Pricing")
            .marginPercentage(new BigDecimal("20.00"))
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(pricingGroupRepository.findById(pricingGroupId)).thenReturn(Optional.of(entity));

        final Optional<PricingGroup> result = pricingGroupGatewayJpa.findById(pricingGroupId);

        assertTrue(result.isPresent());
        assertEquals(pricingGroupId, result.get().id());
        assertEquals("Standard Pricing", result.get().name());
        assertEquals(new BigDecimal("20.00"), result.get().marginPercentage());
        verify(pricingGroupRepository, times(1)).findById(pricingGroupId);
    }

    @Test
    void shouldReturnEmptyWhenPricingGroupNotFoundById() {
        final UUID pricingGroupId = UUID.randomUUID();

        when(pricingGroupRepository.findById(pricingGroupId)).thenReturn(Optional.empty());

        final Optional<PricingGroup> result = pricingGroupGatewayJpa.findById(pricingGroupId);

        assertTrue(result.isEmpty());
        verify(pricingGroupRepository, times(1)).findById(pricingGroupId);
    }

    @Test
    void shouldFindActive() {
        final UUID pricingGroupId1 = UUID.randomUUID();
        final UUID pricingGroupId2 = UUID.randomUUID();
        final Instant now = Instant.now();

        final PricingGroupJpaEntity entity1 = PricingGroupJpaEntity.builder()
            .id(pricingGroupId1)
            .name("Basic Pricing")
            .marginPercentage(new BigDecimal("15.00"))
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final PricingGroupJpaEntity entity2 = PricingGroupJpaEntity.builder()
            .id(pricingGroupId2)
            .name("Premium Pricing")
            .marginPercentage(new BigDecimal("30.00"))
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(pricingGroupRepository.findByIsActiveTrueOrderByNameAsc()).thenReturn(List.of(entity1, entity2));

        final List<PricingGroup> result = pricingGroupGatewayJpa.findActive();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Basic Pricing", result.get(0).name());
        assertEquals("Premium Pricing", result.get(1).name());
        verify(pricingGroupRepository, times(1)).findByIsActiveTrueOrderByNameAsc();
    }

    @Test
    void shouldReturnEmptyListWhenNoActivePricingGroups() {
        when(pricingGroupRepository.findByIsActiveTrueOrderByNameAsc()).thenReturn(List.of());

        final List<PricingGroup> result = pricingGroupGatewayJpa.findActive();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(pricingGroupRepository, times(1)).findByIsActiveTrueOrderByNameAsc();
    }
}

