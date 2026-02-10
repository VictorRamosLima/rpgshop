package rpgshop.infraestructure.persistence.gateway.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.domain.entity.order.OrderPayment;
import rpgshop.infraestructure.persistence.entity.order.OrderJpaEntity;
import rpgshop.infraestructure.persistence.entity.order.OrderPaymentJpaEntity;
import rpgshop.infraestructure.persistence.repository.order.OrderPaymentRepository;
import rpgshop.infraestructure.persistence.repository.order.OrderRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderPaymentGatewayJpaTest {
    @Mock
    private OrderPaymentRepository orderPaymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderPaymentGatewayJpa orderPaymentGatewayJpa;

    @Test
    void shouldSaveOrderPayment() {
        final UUID paymentId = UUID.randomUUID();
        final UUID orderId = UUID.randomUUID();
        final Instant now = Instant.now();

        final OrderPayment payment = OrderPayment.builder()
            .id(paymentId)
            .amount(new BigDecimal("100.00"))
            .createdAt(now)
            .updatedAt(now)
            .build();

        final OrderJpaEntity orderEntity = OrderJpaEntity.builder().id(orderId).build();

        final OrderPaymentJpaEntity savedEntity = OrderPaymentJpaEntity.builder()
            .id(paymentId)
            .order(orderEntity)
            .amount(new BigDecimal("100.00"))
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderPaymentRepository.save(any(OrderPaymentJpaEntity.class))).thenReturn(savedEntity);

        final OrderPayment result = orderPaymentGatewayJpa.save(payment, orderId);

        assertNotNull(result);
        assertEquals(paymentId, result.id());
        assertEquals(new BigDecimal("100.00"), result.amount());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderPaymentRepository, times(1)).save(any(OrderPaymentJpaEntity.class));
    }

    @Test
    void shouldSaveOrderPaymentEvenWhenOrderNotFound() {
        final UUID paymentId = UUID.randomUUID();
        final UUID orderId = UUID.randomUUID();
        final Instant now = Instant.now();

        final OrderPayment payment = OrderPayment.builder()
            .id(paymentId)
            .amount(new BigDecimal("100.00"))
            .createdAt(now)
            .updatedAt(now)
            .build();

        final OrderPaymentJpaEntity savedEntity = OrderPaymentJpaEntity.builder()
            .id(paymentId)
            .amount(new BigDecimal("100.00"))
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        when(orderPaymentRepository.save(any(OrderPaymentJpaEntity.class))).thenReturn(savedEntity);

        final OrderPayment result = orderPaymentGatewayJpa.save(payment, orderId);

        assertNotNull(result);
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderPaymentRepository, times(1)).save(any(OrderPaymentJpaEntity.class));
    }

    @Test
    void shouldFindByOrderId() {
        final UUID orderId = UUID.randomUUID();
        final UUID paymentId = UUID.randomUUID();
        final Instant now = Instant.now();

        final OrderJpaEntity orderEntity = OrderJpaEntity.builder().id(orderId).build();

        final OrderPaymentJpaEntity entity = OrderPaymentJpaEntity.builder()
            .id(paymentId)
            .order(orderEntity)
            .amount(new BigDecimal("100.00"))
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(orderPaymentRepository.findByOrderId(orderId)).thenReturn(List.of(entity));

        final List<OrderPayment> result = orderPaymentGatewayJpa.findByOrderId(orderId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(paymentId, result.getFirst().id());
        verify(orderPaymentRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void shouldReturnEmptyListWhenNoPaymentsFoundByOrderId() {
        final UUID orderId = UUID.randomUUID();

        when(orderPaymentRepository.findByOrderId(orderId)).thenReturn(List.of());

        final List<OrderPayment> result = orderPaymentGatewayJpa.findByOrderId(orderId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderPaymentRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void shouldSumAmountByOrderId() {
        final UUID orderId = UUID.randomUUID();
        final BigDecimal totalAmount = new BigDecimal("250.00");

        when(orderPaymentRepository.sumAmountByOrderId(orderId)).thenReturn(totalAmount);

        final BigDecimal result = orderPaymentGatewayJpa.sumAmountByOrderId(orderId);

        assertEquals(totalAmount, result);
        verify(orderPaymentRepository, times(1)).sumAmountByOrderId(orderId);
    }

    @Test
    void shouldReturnNullWhenNoAmountSummedByOrderId() {
        final UUID orderId = UUID.randomUUID();

        when(orderPaymentRepository.sumAmountByOrderId(orderId)).thenReturn(null);

        final BigDecimal result = orderPaymentGatewayJpa.sumAmountByOrderId(orderId);

        assertNull(result);
        verify(orderPaymentRepository, times(1)).sumAmountByOrderId(orderId);
    }

    @Test
    void shouldReturnTrueWhenPromotionalCouponExistsByOrderId() {
        final UUID orderId = UUID.randomUUID();

        when(orderPaymentRepository.existsPromotionalCouponByOrderId(orderId)).thenReturn(true);

        final boolean result = orderPaymentGatewayJpa.existsPromotionalCouponByOrderId(orderId);

        assertTrue(result);
        verify(orderPaymentRepository, times(1)).existsPromotionalCouponByOrderId(orderId);
    }

    @Test
    void shouldReturnFalseWhenNoPromotionalCouponExistsByOrderId() {
        final UUID orderId = UUID.randomUUID();

        when(orderPaymentRepository.existsPromotionalCouponByOrderId(orderId)).thenReturn(false);

        final boolean result = orderPaymentGatewayJpa.existsPromotionalCouponByOrderId(orderId);

        assertFalse(result);
        verify(orderPaymentRepository, times(1)).existsPromotionalCouponByOrderId(orderId);
    }
}

