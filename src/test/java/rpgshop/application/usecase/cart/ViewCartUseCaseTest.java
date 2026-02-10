package rpgshop.application.usecase.cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.gateway.cart.CartGateway;
import rpgshop.domain.entity.cart.Cart;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ViewCartUseCaseTest {
    @Mock
    private CartGateway cartGateway;

    @InjectMocks
    private ViewCartUseCase useCase;

    @Test
    void shouldReturnCartFromGateway() {
        final UUID customerId = UUID.randomUUID();
        final Cart cart = Cart.builder().id(UUID.randomUUID()).build();
        when(cartGateway.findByCustomerId(customerId)).thenReturn(Optional.of(cart));

        final Optional<Cart> result = useCase.execute(customerId);

        assertTrue(result.isPresent());
        assertEquals(cart.id(), result.orElseThrow().id());
        verify(cartGateway).findByCustomerId(customerId);
    }
}
