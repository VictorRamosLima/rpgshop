package rpgshop.infraestructure.persistence.gateway.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.infraestructure.persistence.entity.product.ProductTypeJpaEntity;
import rpgshop.infraestructure.persistence.repository.product.ProductTypeRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductTypeGatewayJpaTest {
    @Mock
    private ProductTypeRepository productTypeRepository;

    @InjectMocks
    private ProductTypeGatewayJpa productTypeGatewayJpa;

    @Test
    void shouldFindById() {
        final UUID typeId = UUID.randomUUID();
        final Instant now = Instant.now();

        final ProductTypeJpaEntity entity = ProductTypeJpaEntity.builder()
            .id(typeId)
            .name("RPG Book")
            .description("Books for role-playing games")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(productTypeRepository.findById(typeId)).thenReturn(Optional.of(entity));

        final Optional<ProductType> result = productTypeGatewayJpa.findById(typeId);

        assertTrue(result.isPresent());
        assertEquals(typeId, result.get().id());
        assertEquals("RPG Book", result.get().name());
        verify(productTypeRepository, times(1)).findById(typeId);
    }

    @Test
    void shouldReturnEmptyWhenProductTypeNotFoundById() {
        final UUID typeId = UUID.randomUUID();

        when(productTypeRepository.findById(typeId)).thenReturn(Optional.empty());

        final Optional<ProductType> result = productTypeGatewayJpa.findById(typeId);

        assertTrue(result.isEmpty());
        verify(productTypeRepository, times(1)).findById(typeId);
    }

    @Test
    void shouldFindActive() {
        final UUID typeId1 = UUID.randomUUID();
        final UUID typeId2 = UUID.randomUUID();
        final Instant now = Instant.now();

        final ProductTypeJpaEntity entity1 = ProductTypeJpaEntity.builder()
            .id(typeId1)
            .name("Accessory")
            .description("Gaming accessories")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final ProductTypeJpaEntity entity2 = ProductTypeJpaEntity.builder()
            .id(typeId2)
            .name("RPG Book")
            .description("Books for RPG")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(productTypeRepository.findByIsActiveTrueOrderByNameAsc()).thenReturn(List.of(entity1, entity2));

        final List<ProductType> result = productTypeGatewayJpa.findActive();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Accessory", result.get(0).name());
        assertEquals("RPG Book", result.get(1).name());
        verify(productTypeRepository, times(1)).findByIsActiveTrueOrderByNameAsc();
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveProductTypes() {
        when(productTypeRepository.findByIsActiveTrueOrderByNameAsc()).thenReturn(List.of());

        final List<ProductType> result = productTypeGatewayJpa.findActive();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productTypeRepository, times(1)).findByIsActiveTrueOrderByNameAsc();
    }
}
