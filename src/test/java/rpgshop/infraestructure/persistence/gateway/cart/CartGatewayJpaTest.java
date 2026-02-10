package rpgshop.infraestructure.persistence.gateway.cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.domain.entity.cart.Cart;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.infraestructure.persistence.entity.cart.CartJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;
import rpgshop.infraestructure.persistence.repository.cart.CartRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartGatewayJpaTest {
    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartGatewayJpa cartGatewayJpa;

    @Test
    void shouldSaveCart() {
        final UUID cartId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final Instant now = Instant.now();

        final Customer customer = Customer.builder()
            .id(customerId)
            .name("Test Customer")
            .build();

        final Cart cart = Cart.builder()
            .id(cartId)
            .customer(customer)
            .items(List.of())
            .createdAt(now)
            .updatedAt(now)
            .build();

        final CustomerJpaEntity customerJpaEntity = CustomerJpaEntity.builder()
            .id(customerId)
            .name("Test Customer")
            .build();

        final CartJpaEntity savedEntity = CartJpaEntity.builder()
            .id(cartId)
            .customer(customerJpaEntity)
            .items(List.of())
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(cartRepository.save(any(CartJpaEntity.class))).thenReturn(savedEntity);

        final Cart result = cartGatewayJpa.save(cart);

        assertNotNull(result);
        assertEquals(cartId, result.id());
        verify(cartRepository, times(1)).save(argThat(entity ->
            entity.getId().equals(cartId)
        ));
    }

    @Test
    void shouldFindById() {
        final UUID cartId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final Instant now = Instant.now();

        final CustomerJpaEntity customerJpaEntity = CustomerJpaEntity.builder()
            .id(customerId)
            .name("Test Customer")
            .build();

        final CartJpaEntity cartEntity = CartJpaEntity.builder()
            .id(cartId)
            .customer(customerJpaEntity)
            .items(List.of())
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartEntity));

        final Optional<Cart> result = cartGatewayJpa.findById(cartId);

        assertTrue(result.isPresent());
        assertEquals(cartId, result.get().id());
        verify(cartRepository, times(1)).findById(cartId);
    }

    @Test
    void shouldReturnEmptyWhenCartNotFoundById() {
        final UUID cartId = UUID.randomUUID();

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        final Optional<Cart> result = cartGatewayJpa.findById(cartId);

        assertTrue(result.isEmpty());
        verify(cartRepository, times(1)).findById(cartId);
    }

    @Test
    void shouldFindByCustomerId() {
        final UUID cartId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final Instant now = Instant.now();

        final CustomerJpaEntity customerJpaEntity = CustomerJpaEntity.builder()
            .id(customerId)
            .name("Test Customer")
            .build();

        final CartJpaEntity cartEntity = CartJpaEntity.builder()
            .id(cartId)
            .customer(customerJpaEntity)
            .items(List.of())
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cartEntity));

        final Optional<Cart> result = cartGatewayJpa.findByCustomerId(customerId);

        assertTrue(result.isPresent());
        assertEquals(customerId, result.get().customer().id());
        verify(cartRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    void shouldReturnEmptyWhenCartNotFoundByCustomerId() {
        final UUID customerId = UUID.randomUUID();

        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        final Optional<Cart> result = cartGatewayJpa.findByCustomerId(customerId);

        assertTrue(result.isEmpty());
        verify(cartRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    void shouldReturnTrueWhenCartExistsByCustomerId() {
        final UUID customerId = UUID.randomUUID();

        when(cartRepository.existsByCustomerId(customerId)).thenReturn(true);

        final boolean result = cartGatewayJpa.existsByCustomerId(customerId);

        assertTrue(result);
        verify(cartRepository, times(1)).existsByCustomerId(customerId);
    }

    @Test
    void shouldReturnFalseWhenCartNotExistsByCustomerId() {
        final UUID customerId = UUID.randomUUID();

        when(cartRepository.existsByCustomerId(customerId)).thenReturn(false);

        final boolean result = cartGatewayJpa.existsByCustomerId(customerId);

        assertFalse(result);
        verify(cartRepository, times(1)).existsByCustomerId(customerId);
    }
}
