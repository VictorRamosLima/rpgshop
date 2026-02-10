package rpgshop.application.usecase.cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.cart.UpdateCartItemCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.cart.CartGateway;
import rpgshop.application.gateway.cart.CartItemGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.cart.Cart;
import rpgshop.domain.entity.cart.CartItem;
import rpgshop.domain.entity.product.Product;
import rpgshop.infraestructure.config.CartBlockingProperties;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCartItemQuantityUseCaseTest {
    @Mock
    private CartBlockingProperties cartBlockingProperties;

    @Mock
    private CartGateway cartGateway;

    @Mock
    private CartItemGateway cartItemGateway;

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private UpdateCartItemQuantityUseCase useCase;

    @Test
    void shouldUpdateItemQuantityAndRefreshBlockingInfo() {
        final UUID customerId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final UpdateCartItemCommand command = new UpdateCartItemCommand(customerId, productId, 4);

        final Product product = Product.builder()
            .id(productId)
            .name("Livro")
            .stockQuantity(10)
            .statusChanges(List.of())
            .build();
        final Cart cart = Cart.builder().id(UUID.randomUUID()).build();
        final CartItem item = CartItem.builder()
            .id(UUID.randomUUID())
            .cartId(cart.id())
            .product(product)
            .quantity(2)
            .build();

        when(cartGateway.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
        when(cartItemGateway.findByCartIdAndProductId(cart.id(), productId)).thenReturn(Optional.of(item));
        when(productGateway.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemGateway.sumBlockedQuantityByProductId(productId)).thenReturn(5);
        when(cartBlockingProperties.getTimeoutMinutes()).thenReturn(15);
        when(cartGateway.findById(cart.id())).thenReturn(Optional.of(cart));

        final Cart result = useCase.execute(command);

        assertEquals(cart.id(), result.id());
        verify(cartItemGateway).save(
            argThat(updated ->
                updated.id().equals(item.id())
                    && updated.quantity() == 4
                    && updated.isBlocked()
                    && updated.blockedAt() != null
                    && updated.expiresAt() != null
            ),
            eq(cart.id())
        );
    }

    @Test
    void shouldThrowWhenRequestedQuantityIsInvalid() {
        final UpdateCartItemCommand command = new UpdateCartItemCommand(UUID.randomUUID(), UUID.randomUUID(), 0);
        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldThrowWhenRequestedQuantityExceedsAvailableStock() {
        final UUID customerId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final UpdateCartItemCommand command = new UpdateCartItemCommand(customerId, productId, 4);

        final Product product = Product.builder()
            .id(productId)
            .name("Livro")
            .stockQuantity(10)
            .statusChanges(List.of())
            .build();
        final Cart cart = Cart.builder().id(UUID.randomUUID()).build();
        final CartItem item = CartItem.builder()
            .id(UUID.randomUUID())
            .cartId(cart.id())
            .product(product)
            .quantity(2)
            .build();

        when(cartGateway.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
        when(cartItemGateway.findByCartIdAndProductId(cart.id(), productId)).thenReturn(Optional.of(item));
        when(productGateway.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemGateway.sumBlockedQuantityByProductId(productId)).thenReturn(9);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
        verify(cartItemGateway, never()).save(any(CartItem.class), eq(cart.id()));
    }

    @Test
    void shouldThrowWhenBlockingTimeoutIsInvalid() {
        final UUID customerId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final UpdateCartItemCommand command = new UpdateCartItemCommand(customerId, productId, 2);

        final Product product = Product.builder()
            .id(productId)
            .name("Livro")
            .stockQuantity(10)
            .statusChanges(List.of())
            .build();
        final Cart cart = Cart.builder().id(UUID.randomUUID()).build();
        final CartItem item = CartItem.builder()
            .id(UUID.randomUUID())
            .cartId(cart.id())
            .product(product)
            .quantity(1)
            .build();

        when(cartGateway.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
        when(cartItemGateway.findByCartIdAndProductId(cart.id(), productId)).thenReturn(Optional.of(item));
        when(productGateway.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemGateway.sumBlockedQuantityByProductId(productId)).thenReturn(1);
        when(cartBlockingProperties.getTimeoutMinutes()).thenReturn(0);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
        verify(cartItemGateway, never()).save(any(CartItem.class), eq(cart.id()));
    }
}
