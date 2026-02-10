package rpgshop.presentation.controller.cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import rpgshop.application.command.cart.AddCartItemCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.usecase.cart.AddCartItemUseCase;
import rpgshop.application.usecase.cart.ClearCartUseCase;
import rpgshop.application.usecase.cart.RemoveCartItemUseCase;
import rpgshop.application.usecase.cart.UpdateCartItemQuantityUseCase;
import rpgshop.application.usecase.cart.ViewCartUseCase;
import rpgshop.domain.entity.cart.Cart;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {
    @Mock
    private ViewCartUseCase viewCartUseCase;
    @Mock
    private AddCartItemUseCase addCartItemUseCase;
    @Mock
    private UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase;
    @Mock
    private RemoveCartItemUseCase removeCartItemUseCase;
    @Mock
    private ClearCartUseCase clearCartUseCase;
    @Mock
    private Model model;

    @InjectMocks
    private CartController controller;

    @Test
    void shouldRenderCartViewAndAddAttributes() {
        final UUID customerId = UUID.randomUUID();
        final Cart cart = Cart.builder().id(UUID.randomUUID()).build();
        when(viewCartUseCase.execute(customerId)).thenReturn(Optional.of(cart));

        final String view = controller.viewCart(customerId, model);

        assertEquals("cart/view", view);
        verify(viewCartUseCase).execute(customerId);
        verify(model).addAttribute("cart", cart);
        verify(model).addAttribute("customerId", customerId);
    }

    @Test
    void shouldExecuteAddItemAndRedirect() {
        final UUID customerId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();

        final String view = controller.addItem(customerId, productId, 2, model);

        assertEquals("redirect:/cart/" + customerId, view);
        verify(addCartItemUseCase).execute(new AddCartItemCommand(customerId, productId, 2));
    }

    @Test
    void shouldReturnCartViewWithErrorWhenAddItemFails() {
        final UUID customerId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        when(viewCartUseCase.execute(customerId)).thenReturn(Optional.empty());
        when(addCartItemUseCase.execute(any())).thenThrow(new BusinessRuleException("erro ao adicionar"));

        final String view = controller.addItem(customerId, productId, 3, model);

        assertEquals("cart/view", view);
        verify(model).addAttribute("error", "erro ao adicionar");
        verify(model, times(2)).addAttribute("customerId", customerId);
        verify(viewCartUseCase).execute(customerId);
    }

    @Test
    void shouldRemoveItemAndRedirect() {
        final UUID customerId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();

        final String view = controller.removeItem(customerId, productId);

        assertEquals("redirect:/cart/" + customerId, view);
        verify(removeCartItemUseCase).execute(customerId, productId);
    }

    @Test
    void shouldClearCartAndRedirect() {
        final UUID customerId = UUID.randomUUID();

        final String view = controller.clearCart(customerId);

        assertEquals("redirect:/cart/" + customerId, view);
        verify(clearCartUseCase).execute(eq(customerId));
    }
}
