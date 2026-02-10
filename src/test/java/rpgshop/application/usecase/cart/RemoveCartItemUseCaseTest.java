package rpgshop.application.usecase.cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.cart.CartGateway;
import rpgshop.application.gateway.cart.CartItemGateway;
import rpgshop.domain.entity.cart.Cart;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemoveCartItemUseCaseTest {
    @Mock
    private CartGateway cartGateway;

    @Mock
    private CartItemGateway cartItemGateway;

    @InjectMocks
    private RemoveCartItemUseCase useCase;

    @Test
    void shouldDeleteItemAndReturnUpdatedCart() {
        final UUID customerId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final Cart cart = Cart.builder().id(UUID.randomUUID()).build();
        final Cart updated = cart.toBuilder().build();

        when(cartGateway.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
        when(cartGateway.findById(cart.id())).thenReturn(Optional.of(updated));

        final Cart result = useCase.execute(customerId, productId);

        assertEquals(updated.id(), result.id());
        verify(cartItemGateway).deleteByCartIdAndProductId(cart.id(), productId);
    }

    @Test
    void shouldThrowWhenCartDoesNotExist() {
        final UUID customerId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        when(cartGateway.findByCustomerId(customerId)).thenReturn(Optional.empty());

        assertThrows(BusinessRuleException.class, () -> useCase.execute(customerId, productId));
    }
}
