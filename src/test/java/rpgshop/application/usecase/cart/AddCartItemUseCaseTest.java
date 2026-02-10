package rpgshop.application.usecase.cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.cart.AddCartItemCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.cart.CartGateway;
import rpgshop.application.gateway.cart.CartItemGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.cart.Cart;
import rpgshop.domain.entity.cart.CartItem;
import rpgshop.domain.entity.customer.Customer;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddCartItemUseCaseTest {
    @Mock
    private CartBlockingProperties cartBlockingProperties;

    @Mock
    private CartGateway cartGateway;

    @Mock
    private CartItemGateway cartItemGateway;

    @Mock
    private ProductGateway productGateway;

    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private AddCartItemUseCase useCase;

    @Test
    void shouldCreateCartAndSaveNewBlockedItemWhenCustomerHasNoCart() {
        final UUID customerId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final AddCartItemCommand command = new AddCartItemCommand(customerId, productId, 2);

        final Customer customer = Customer.builder().id(customerId).isActive(true).build();
        final Product product = Product.builder()
            .id(productId)
            .name("Livro")
            .stockQuantity(10)
            .statusChanges(List.of())
            .build();
        final Cart createdCart = Cart.builder()
            .id(UUID.randomUUID())
            .customer(customer)
            .items(List.of())
            .build();

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(customer));
        when(productGateway.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemGateway.sumBlockedQuantityByProductId(productId)).thenReturn(1);
        when(cartGateway.findByCustomerId(customerId)).thenReturn(Optional.empty());
        when(cartGateway.save(any(Cart.class))).thenReturn(createdCart);
        when(cartItemGateway.findByCartIdAndProductId(createdCart.id(), productId)).thenReturn(Optional.empty());
        when(cartBlockingProperties.getTimeoutMinutes()).thenReturn(30);
        when(cartGateway.findById(createdCart.id())).thenReturn(Optional.of(createdCart));

        final Cart result = useCase.execute(command);

        assertEquals(createdCart.id(), result.id());
        verify(cartItemGateway, times(1)).save(
            argThat(item ->
                item.quantity() == 2
                    && item.isBlocked()
                    && item.product().id().equals(productId)
                    && item.blockedAt() != null
                    && item.expiresAt() != null
                    && item.expiresAt().isAfter(item.blockedAt())
            ),
            eq(createdCart.id())
        );
    }

    @Test
    void shouldUpdateExistingItemWhenProductAlreadyInCart() {
        final UUID customerId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final AddCartItemCommand command = new AddCartItemCommand(customerId, productId, 3);

        final Customer customer = Customer.builder().id(customerId).isActive(true).build();
        final Product product = Product.builder()
            .id(productId)
            .name("Livro")
            .stockQuantity(12)
            .statusChanges(List.of())
            .build();
        final Cart cart = Cart.builder()
            .id(UUID.randomUUID())
            .customer(customer)
            .items(List.of())
            .build();
        final CartItem existingItem = CartItem.builder()
            .id(UUID.randomUUID())
            .cartId(cart.id())
            .product(product)
            .quantity(2)
            .build();

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(customer));
        when(productGateway.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemGateway.sumBlockedQuantityByProductId(productId)).thenReturn(2);
        when(cartGateway.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
        when(cartItemGateway.findByCartIdAndProductId(cart.id(), productId)).thenReturn(Optional.of(existingItem));
        when(cartBlockingProperties.getTimeoutMinutes()).thenReturn(20);
        when(cartGateway.findById(cart.id())).thenReturn(Optional.of(cart));

        final Cart result = useCase.execute(command);

        assertEquals(cart.id(), result.id());
        verify(cartItemGateway, times(1)).save(
            argThat(item ->
                item.id().equals(existingItem.id())
                    && item.quantity() == 5
                    && item.isBlocked()
                    && item.blockedAt() != null
                    && item.expiresAt() != null
            ),
            eq(cart.id())
        );
    }

    @Test
    void shouldThrowWhenRequestedQuantityExceedsAvailableStock() {
        final UUID customerId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final AddCartItemCommand command = new AddCartItemCommand(customerId, productId, 3);

        final Customer customer = Customer.builder().id(customerId).isActive(true).build();
        final Product product = Product.builder()
            .id(productId)
            .name("Livro")
            .stockQuantity(5)
            .statusChanges(List.of())
            .build();

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(customer));
        when(productGateway.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemGateway.sumBlockedQuantityByProductId(productId)).thenReturn(4);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
        verify(cartGateway, never()).save(any(Cart.class));
    }

    @Test
    void shouldThrowWhenBlockingTimeoutIsInvalid() {
        final UUID customerId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final AddCartItemCommand command = new AddCartItemCommand(customerId, productId, 1);

        final Customer customer = Customer.builder().id(customerId).isActive(true).build();
        final Product product = Product.builder()
            .id(productId)
            .name("Livro")
            .stockQuantity(5)
            .statusChanges(List.of())
            .build();
        final Cart cart = Cart.builder()
            .id(UUID.randomUUID())
            .customer(customer)
            .items(List.of())
            .build();

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(customer));
        when(productGateway.findById(productId)).thenReturn(Optional.of(product));
        when(cartItemGateway.sumBlockedQuantityByProductId(productId)).thenReturn(0);
        when(cartGateway.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
        when(cartItemGateway.findByCartIdAndProductId(cart.id(), productId)).thenReturn(Optional.empty());
        when(cartBlockingProperties.getTimeoutMinutes()).thenReturn(0);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
        verify(cartItemGateway, never()).save(any(CartItem.class), eq(cart.id()));
    }
}
