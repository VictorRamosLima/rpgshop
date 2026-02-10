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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClearCartUseCaseTest {
    @Mock
    private CartGateway cartGateway;

    @Mock
    private CartItemGateway cartItemGateway;

    @InjectMocks
    private ClearCartUseCase useCase;

    @Test
    void shouldDeleteAllCartItemsWhenCartExists() {
        final UUID customerId = UUID.randomUUID();
        final Cart cart = Cart.builder().id(UUID.randomUUID()).build();
        when(cartGateway.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

        useCase.execute(customerId);

        verify(cartItemGateway).deleteAllByCartId(cart.id());
    }

    @Test
    void shouldThrowWhenCartDoesNotExist() {
        final UUID customerId = UUID.randomUUID();
        when(cartGateway.findByCustomerId(customerId)).thenReturn(Optional.empty());

        assertThrows(BusinessRuleException.class, () -> useCase.execute(customerId));

        verifyNoInteractions(cartItemGateway);
    }
}
