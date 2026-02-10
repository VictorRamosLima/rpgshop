package rpgshop.infraestructure.persistence.gateway.cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.domain.entity.cart.CartItem;
import rpgshop.domain.entity.product.Product;
import rpgshop.infraestructure.persistence.entity.cart.CartItemJpaEntity;
import rpgshop.infraestructure.persistence.entity.cart.CartJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;
import rpgshop.infraestructure.persistence.mapper.cart.CartItemMapper;
import rpgshop.infraestructure.persistence.repository.cart.CartItemRepository;
import rpgshop.infraestructure.persistence.repository.cart.CartRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartItemGatewayJpaTest {
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartItemGatewayJpa cartItemGatewayJpa;

    private CartItem createCartItem(final UUID id, final UUID cartId, final UUID productId) {
        final Instant now = Instant.now();
        return CartItem.builder()
            .id(id)
            .cartId(cartId)
            .product(Product.builder()
                .id(productId)
                .name("Test Product")
                .build())
            .quantity(2)
            .isBlocked(true)
            .blockedAt(now)
            .expiresAt(now.plusSeconds(3600))
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private CartItemJpaEntity createCartItemJpaEntity(final UUID id, final UUID productId) {
        final Instant now = Instant.now();
        return CartItemJpaEntity.builder()
            .id(id)
            .product(ProductJpaEntity.builder()
                .id(productId)
                .name("Test Product")
                .build())
            .quantity(2)
            .isBlocked(true)
            .blockedAt(now)
            .expiresAt(now.plusSeconds(3600))
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    @Test
    void shouldSaveCartItemWithCart() {
        final UUID cartItemId = UUID.randomUUID();
        final UUID cartId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();

        final CartItem cartItem = createCartItem(cartItemId, cartId, productId);
        final CartItemJpaEntity entityToSave = createCartItemJpaEntity(cartItemId, productId);
        final CartItemJpaEntity savedEntity = createCartItemJpaEntity(cartItemId, productId);
        final CartJpaEntity cartEntity = CartJpaEntity.builder().id(cartId).build();

        try (MockedStatic<CartItemMapper> mapperMock = mockStatic(CartItemMapper.class)) {
            mapperMock.when(() -> CartItemMapper.toEntity(cartItem)).thenReturn(entityToSave);
            mapperMock.when(() -> CartItemMapper.toDomain(savedEntity)).thenReturn(cartItem);

            when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartEntity));
            when(cartItemRepository.save(entityToSave)).thenReturn(savedEntity);

            final CartItem result = cartItemGatewayJpa.save(cartItem, cartId);

            assertNotNull(result);
            assertEquals(cartItemId, result.id());
            verify(cartRepository, times(1)).findById(cartId);
            verify(cartItemRepository, times(1)).save(entityToSave);
        }
    }

    @Test
    void shouldSaveCartItemWithoutCartWhenCartNotFound() {
        final UUID cartItemId = UUID.randomUUID();
        final UUID cartId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();

        final CartItem cartItem = createCartItem(cartItemId, cartId, productId);
        final CartItemJpaEntity entityToSave = createCartItemJpaEntity(cartItemId, productId);
        final CartItemJpaEntity savedEntity = createCartItemJpaEntity(cartItemId, productId);

        try (MockedStatic<CartItemMapper> mapperMock = mockStatic(CartItemMapper.class)) {
            mapperMock.when(() -> CartItemMapper.toEntity(cartItem)).thenReturn(entityToSave);
            mapperMock.when(() -> CartItemMapper.toDomain(savedEntity)).thenReturn(cartItem);

            when(cartRepository.findById(cartId)).thenReturn(Optional.empty());
            when(cartItemRepository.save(entityToSave)).thenReturn(savedEntity);

            final CartItem result = cartItemGatewayJpa.save(cartItem, cartId);

            assertNotNull(result);
            assertEquals(cartItemId, result.id());
            verify(cartRepository, times(1)).findById(cartId);
            verify(cartItemRepository, times(1)).save(entityToSave);
        }
    }

    @Test
    void shouldFindById() {
        final UUID cartItemId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();

        final CartItemJpaEntity entity = createCartItemJpaEntity(cartItemId, productId);
        final CartItem cartItem = createCartItem(cartItemId, null, productId);

        try (MockedStatic<CartItemMapper> mapperMock = mockStatic(CartItemMapper.class)) {
            mapperMock.when(() -> CartItemMapper.toDomain(entity)).thenReturn(cartItem);

            when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(entity));

            final Optional<CartItem> result = cartItemGatewayJpa.findById(cartItemId);

            assertTrue(result.isPresent());
            assertEquals(cartItemId, result.get().id());
            verify(cartItemRepository, times(1)).findById(cartItemId);
        }
    }

    @Test
    void shouldReturnEmptyWhenCartItemNotFoundById() {
        final UUID cartItemId = UUID.randomUUID();

        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.empty());

        final Optional<CartItem> result = cartItemGatewayJpa.findById(cartItemId);

        assertTrue(result.isEmpty());
        verify(cartItemRepository, times(1)).findById(cartItemId);
    }

    @Test
    void shouldFindByCartId() {
        final UUID cartId = UUID.randomUUID();
        final UUID cartItemId1 = UUID.randomUUID();
        final UUID cartItemId2 = UUID.randomUUID();
        final UUID productId1 = UUID.randomUUID();
        final UUID productId2 = UUID.randomUUID();

        final CartItemJpaEntity entity1 = createCartItemJpaEntity(cartItemId1, productId1);
        final CartItemJpaEntity entity2 = createCartItemJpaEntity(cartItemId2, productId2);
        final CartItem cartItem1 = createCartItem(cartItemId1, cartId, productId1);
        final CartItem cartItem2 = createCartItem(cartItemId2, cartId, productId2);

        try (MockedStatic<CartItemMapper> mapperMock = mockStatic(CartItemMapper.class)) {
            mapperMock.when(() -> CartItemMapper.toDomain(entity1)).thenReturn(cartItem1);
            mapperMock.when(() -> CartItemMapper.toDomain(entity2)).thenReturn(cartItem2);

            when(cartItemRepository.findByCartId(cartId)).thenReturn(List.of(entity1, entity2));

            final List<CartItem> result = cartItemGatewayJpa.findByCartId(cartId);

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(cartItemRepository, times(1)).findByCartId(cartId);
        }
    }

    @Test
    void shouldReturnEmptyListWhenNoCartItemsFoundByCartId() {
        final UUID cartId = UUID.randomUUID();

        when(cartItemRepository.findByCartId(cartId)).thenReturn(List.of());

        final List<CartItem> result = cartItemGatewayJpa.findByCartId(cartId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(cartItemRepository, times(1)).findByCartId(cartId);
    }

    @Test
    void shouldFindByCartIdAndProductId() {
        final UUID cartId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final UUID cartItemId = UUID.randomUUID();

        final CartItemJpaEntity entity = createCartItemJpaEntity(cartItemId, productId);
        final CartItem cartItem = createCartItem(cartItemId, cartId, productId);

        try (MockedStatic<CartItemMapper> mapperMock = mockStatic(CartItemMapper.class)) {
            mapperMock.when(() -> CartItemMapper.toDomain(entity)).thenReturn(cartItem);

            when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.of(entity));

            final Optional<CartItem> result = cartItemGatewayJpa.findByCartIdAndProductId(cartId, productId);

            assertTrue(result.isPresent());
            assertEquals(cartItemId, result.get().id());
            verify(cartItemRepository, times(1)).findByCartIdAndProductId(cartId, productId);
        }
    }

    @Test
    void shouldReturnEmptyWhenCartItemNotFoundByCartIdAndProductId() {
        final UUID cartId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();

        when(cartItemRepository.findByCartIdAndProductId(cartId, productId)).thenReturn(Optional.empty());

        final Optional<CartItem> result = cartItemGatewayJpa.findByCartIdAndProductId(cartId, productId);

        assertTrue(result.isEmpty());
        verify(cartItemRepository, times(1)).findByCartIdAndProductId(cartId, productId);
    }

    @Test
    void shouldDeleteByCartIdAndProductId() {
        final UUID cartId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();

        cartItemGatewayJpa.deleteByCartIdAndProductId(cartId, productId);

        verify(cartItemRepository, times(1)).deleteByCartIdAndProductId(cartId, productId);
    }

    @Test
    void shouldDeleteAllByCartId() {
        final UUID cartId = UUID.randomUUID();
        final int deletedCount = 5;

        when(cartItemRepository.deleteAllByCartId(cartId)).thenReturn(deletedCount);

        final int result = cartItemGatewayJpa.deleteAllByCartId(cartId);

        assertEquals(deletedCount, result);
        verify(cartItemRepository, times(1)).deleteAllByCartId(cartId);
    }

    @Test
    void shouldReturnZeroWhenNoItemsDeletedByCartId() {
        final UUID cartId = UUID.randomUUID();

        when(cartItemRepository.deleteAllByCartId(cartId)).thenReturn(0);

        final int result = cartItemGatewayJpa.deleteAllByCartId(cartId);

        assertEquals(0, result);
        verify(cartItemRepository, times(1)).deleteAllByCartId(cartId);
    }

    @Test
    void shouldFindExpiredBlockedItems() {
        final Instant now = Instant.now();
        final UUID cartItemId1 = UUID.randomUUID();
        final UUID cartItemId2 = UUID.randomUUID();
        final UUID productId1 = UUID.randomUUID();
        final UUID productId2 = UUID.randomUUID();

        final CartItemJpaEntity entity1 = createCartItemJpaEntity(cartItemId1, productId1);
        final CartItemJpaEntity entity2 = createCartItemJpaEntity(cartItemId2, productId2);
        final CartItem cartItem1 = createCartItem(cartItemId1, null, productId1);
        final CartItem cartItem2 = createCartItem(cartItemId2, null, productId2);

        try (MockedStatic<CartItemMapper> mapperMock = mockStatic(CartItemMapper.class)) {
            mapperMock.when(() -> CartItemMapper.toDomain(entity1)).thenReturn(cartItem1);
            mapperMock.when(() -> CartItemMapper.toDomain(entity2)).thenReturn(cartItem2);

            when(cartItemRepository.findExpiredBlockedItems(now)).thenReturn(List.of(entity1, entity2));

            final List<CartItem> result = cartItemGatewayJpa.findExpiredBlockedItems(now);

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(cartItemRepository, times(1)).findExpiredBlockedItems(now);
        }
    }

    @Test
    void shouldReturnEmptyListWhenNoExpiredBlockedItems() {
        final Instant now = Instant.now();

        when(cartItemRepository.findExpiredBlockedItems(now)).thenReturn(List.of());

        final List<CartItem> result = cartItemGatewayJpa.findExpiredBlockedItems(now);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(cartItemRepository, times(1)).findExpiredBlockedItems(now);
    }

    @Test
    void shouldSumBlockedQuantityByProductId() {
        final UUID productId = UUID.randomUUID();
        final int totalBlockedQuantity = 15;

        when(cartItemRepository.sumBlockedQuantityByProductId(productId)).thenReturn(totalBlockedQuantity);

        final int result = cartItemGatewayJpa.sumBlockedQuantityByProductId(productId);

        assertEquals(totalBlockedQuantity, result);
        verify(cartItemRepository, times(1)).sumBlockedQuantityByProductId(productId);
    }

    @Test
    void shouldReturnZeroWhenNoBlockedQuantityByProductId() {
        final UUID productId = UUID.randomUUID();

        when(cartItemRepository.sumBlockedQuantityByProductId(productId)).thenReturn(0);

        final int result = cartItemGatewayJpa.sumBlockedQuantityByProductId(productId);

        assertEquals(0, result);
        verify(cartItemRepository, times(1)).sumBlockedQuantityByProductId(productId);
    }
}

