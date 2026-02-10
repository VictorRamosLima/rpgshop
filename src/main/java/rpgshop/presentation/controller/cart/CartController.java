package rpgshop.presentation.controller.cart;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rpgshop.application.command.cart.AddCartItemCommand;
import rpgshop.application.command.cart.UpdateCartItemCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.usecase.cart.AddCartItemUseCase;
import rpgshop.application.usecase.cart.ClearCartUseCase;
import rpgshop.application.usecase.cart.RemoveCartItemUseCase;
import rpgshop.application.usecase.cart.UpdateCartItemQuantityUseCase;
import rpgshop.application.usecase.cart.ViewCartUseCase;
import rpgshop.domain.entity.cart.Cart;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/cart")
public final class CartController {
    private final ViewCartUseCase viewCartUseCase;
    private final AddCartItemUseCase addCartItemUseCase;
    private final UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase;
    private final RemoveCartItemUseCase removeCartItemUseCase;
    private final ClearCartUseCase clearCartUseCase;

    public CartController(
        final ViewCartUseCase viewCartUseCase,
        final AddCartItemUseCase addCartItemUseCase,
        final UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase,
        final RemoveCartItemUseCase removeCartItemUseCase,
        final ClearCartUseCase clearCartUseCase
    ) {
        this.viewCartUseCase = viewCartUseCase;
        this.addCartItemUseCase = addCartItemUseCase;
        this.updateCartItemQuantityUseCase = updateCartItemQuantityUseCase;
        this.removeCartItemUseCase = removeCartItemUseCase;
        this.clearCartUseCase = clearCartUseCase;
    }

    @GetMapping("/{customerId}")
    public String viewCart(@PathVariable final UUID customerId, final Model model) {
        final Optional<Cart> cart = viewCartUseCase.execute(customerId);
        cart.ifPresent(c -> model.addAttribute("cart", c));
        model.addAttribute("customerId", customerId);
        return "cart/view";
    }

    @PostMapping("/{customerId}/add")
    public String addItem(
        @PathVariable final UUID customerId,
        @RequestParam final UUID productId,
        @RequestParam final int quantity,
        final Model model
    ) {
        try {
            final var command = new AddCartItemCommand(customerId, productId, quantity);
            addCartItemUseCase.execute(command);
            return "redirect:/cart/" + customerId;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("customerId", customerId);
            return viewCart(customerId, model);
        }
    }

    @PostMapping("/{customerId}/update")
    public String updateItem(
        @PathVariable final UUID customerId,
        @RequestParam final UUID productId,
        @RequestParam final int quantity,
        final Model model
    ) {
        try {
            final var command = new UpdateCartItemCommand(customerId, productId, quantity);
            updateCartItemQuantityUseCase.execute(command);
            return "redirect:/cart/" + customerId;
        } catch (BusinessRuleException e) {
            model.addAttribute("error", e.getMessage());
            return viewCart(customerId, model);
        }
    }

    @PostMapping("/{customerId}/remove")
    public String removeItem(
        @PathVariable final UUID customerId,
        @RequestParam final UUID productId
    ) {
        removeCartItemUseCase.execute(customerId, productId);
        return "redirect:/cart/" + customerId;
    }

    @PostMapping("/{customerId}/clear")
    public String clearCart(@PathVariable final UUID customerId) {
        clearCartUseCase.execute(customerId);
        return "redirect:/cart/" + customerId;
    }
}
