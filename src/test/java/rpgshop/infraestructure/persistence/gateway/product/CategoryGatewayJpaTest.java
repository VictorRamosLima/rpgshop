package rpgshop.infraestructure.persistence.gateway.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.domain.entity.product.Category;
import rpgshop.infraestructure.persistence.entity.product.CategoryJpaEntity;
import rpgshop.infraestructure.persistence.repository.product.CategoryRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryGatewayJpaTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryGatewayJpa categoryGatewayJpa;

    @Test
    void shouldFindAllByIds() {
        final UUID categoryId1 = UUID.randomUUID();
        final UUID categoryId2 = UUID.randomUUID();
        final Instant now = Instant.now();

        final CategoryJpaEntity entity1 = CategoryJpaEntity.builder()
            .id(categoryId1)
            .name("RPG Books")
            .description("Books for RPG games")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final CategoryJpaEntity entity2 = CategoryJpaEntity.builder()
            .id(categoryId2)
            .name("Board Games")
            .description("Board game category")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final List<UUID> ids = List.of(categoryId1, categoryId2);

        when(categoryRepository.findByIdIn(ids)).thenReturn(List.of(entity1, entity2));

        final List<Category> result = categoryGatewayJpa.findAllByIds(ids);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(categoryId1, result.get(0).id());
        assertEquals(categoryId2, result.get(1).id());
        verify(categoryRepository, times(1)).findByIdIn(ids);
    }

    @Test
    void shouldReturnEmptyListWhenNoCategoriesFoundByIds() {
        final List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());

        when(categoryRepository.findByIdIn(ids)).thenReturn(List.of());

        final List<Category> result = categoryGatewayJpa.findAllByIds(ids);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepository, times(1)).findByIdIn(ids);
    }

    @Test
    void shouldFindActive() {
        final UUID categoryId1 = UUID.randomUUID();
        final UUID categoryId2 = UUID.randomUUID();
        final Instant now = Instant.now();

        final CategoryJpaEntity entity1 = CategoryJpaEntity.builder()
            .id(categoryId1)
            .name("Board Games")
            .description("Board game category")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final CategoryJpaEntity entity2 = CategoryJpaEntity.builder()
            .id(categoryId2)
            .name("RPG Books")
            .description("Books for RPG games")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(categoryRepository.findByIsActiveTrueOrderByNameAsc()).thenReturn(List.of(entity1, entity2));

        final List<Category> result = categoryGatewayJpa.findActive();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Board Games", result.get(0).name());
        assertEquals("RPG Books", result.get(1).name());
        verify(categoryRepository, times(1)).findByIsActiveTrueOrderByNameAsc();
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveCategories() {
        when(categoryRepository.findByIsActiveTrueOrderByNameAsc()).thenReturn(List.of());

        final List<Category> result = categoryGatewayJpa.findActive();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepository, times(1)).findByIsActiveTrueOrderByNameAsc();
    }

    @Test
    void shouldReturnPartialListWhenSomeIdsNotFound() {
        final UUID categoryId1 = UUID.randomUUID();
        final UUID categoryId2 = UUID.randomUUID();
        final Instant now = Instant.now();

        final CategoryJpaEntity entity1 = CategoryJpaEntity.builder()
            .id(categoryId1)
            .name("RPG Books")
            .description("Books for RPG games")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final List<UUID> ids = List.of(categoryId1, categoryId2);

        when(categoryRepository.findByIdIn(ids)).thenReturn(List.of(entity1));

        final List<Category> result = categoryGatewayJpa.findAllByIds(ids);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoryId1, result.getFirst().id());
        verify(categoryRepository, times(1)).findByIdIn(ids);
    }
}
