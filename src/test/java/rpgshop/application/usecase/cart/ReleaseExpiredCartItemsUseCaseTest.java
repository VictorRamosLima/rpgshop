package rpgshop.application.usecase.cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.gateway.cart.CartItemGateway;
import rpgshop.domain.entity.cart.CartItem;
import rpgshop.domain.entity.product.Product;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReleaseExpiredCartItemsUseCaseTest {
    @Mock
    private CartItemGateway cartItemGateway;

    @InjectMocks
    private ReleaseExpiredCartItemsUseCase useCase;

    @Test
    void shouldDeleteOnlyItemsWithCartIdAndReturnFoundAmount() {
        final UUID cartId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final Product product = Product.builder().id(productId).build();

        final CartItem withCart = CartItem.builder()
            .id(UUID.randomUUID())
            .cartId(cartId)
            .product(product)
            .quantity(1)
            .build();
        final CartItem withoutCart = CartItem.builder()
            .id(UUID.randomUUID())
            .cartId(null)
            .product(product)
            .quantity(1)
            .build();

        when(cartItemGateway.findExpiredBlockedItems(any(Instant.class)))
            .thenReturn(List.of(withCart, withoutCart));

        final int released = useCase.execute();

        assertEquals(2, released);
        verify(cartItemGateway).deleteByCartIdAndProductId(cartId, productId);
    }

    @Test
    void shouldReturnZeroWhenNoExpiredItemsAreFound() {
        when(cartItemGateway.findExpiredBlockedItems(any(Instant.class))).thenReturn(List.of());

        final int released = useCase.execute();

        assertEquals(0, released);
        verify(cartItemGateway, never())
            .deleteByCartIdAndProductId(any(UUID.class), any(UUID.class));
    }
}
