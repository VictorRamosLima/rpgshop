package rpgshop.application.usecase.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.order.CardOperatorDecision;
import rpgshop.application.gateway.coupon.CouponGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.gateway.order.CardOperatorGateway;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderPayment;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static rpgshop.domain.entity.coupon.constant.CouponType.EXCHANGE;
import static rpgshop.domain.entity.order.constant.OrderStatus.PROCESSING;

@ExtendWith(MockitoExtension.class)
class ApproveOrderUseCaseTest {
    @Mock
    private OrderGateway orderGateway;

    @Mock
    private CardOperatorGateway cardOperatorGateway;

    @Mock
    private ProductGateway productGateway;

    @Mock
    private CouponGateway couponGateway;

    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private ApproveOrderUseCase useCase;

    @Test
    void shouldGenerateChangeCouponWhenCouponPaymentsExceedOrderTotal() {
        final UUID customerId = UUID.randomUUID();
        final UUID orderId = UUID.randomUUID();

        final Customer customer = Customer.builder()
            .id(customerId)
            .name("Cliente Teste")
            .ranking(ZERO)
            .build();

        final Coupon usedCoupon = Coupon.builder()
            .id(UUID.randomUUID())
            .code("TROCA-ABCD1234")
            .type(EXCHANGE)
            .value(new BigDecimal("120.00"))
            .customer(customer)
            .isUsed(false)
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();

        final Order order = Order.builder()
            .id(orderId)
            .customer(customer)
            .status(PROCESSING)
            .total(new BigDecimal("100.00"))
            .items(List.of())
            .payments(List.of(
                OrderPayment.builder()
                    .id(UUID.randomUUID())
                    .coupon(usedCoupon)
                    .amount(new BigDecimal("120.00"))
                    .build()
            ))
            .build();

        when(orderGateway.findById(orderId)).thenReturn(Optional.of(order));
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cardOperatorGateway.authorize(eq(customerId), eq(order.total()), eq(order.payments())))
            .thenReturn(new CardOperatorDecision(true, "OK"));

        when(couponGateway.findById(usedCoupon.id())).thenReturn(Optional.of(usedCoupon));
        when(couponGateway.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(couponGateway.existsByCode(anyString())).thenReturn(false);

        when(customerGateway.findById(customerId)).thenReturn(Optional.of(customer));
        when(customerGateway.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(orderId);

        final ArgumentCaptor<Coupon> couponCaptor = ArgumentCaptor.forClass(Coupon.class);
        verify(couponGateway, times(2)).save(couponCaptor.capture());

        final Coupon updatedOriginalCoupon = couponCaptor.getAllValues().get(0);
        final Coupon generatedChangeCoupon = couponCaptor.getAllValues().get(1);

        assertTrue(updatedOriginalCoupon.isUsed());
        assertEquals(new BigDecimal("20.00"), generatedChangeCoupon.value());
        assertEquals(EXCHANGE, generatedChangeCoupon.type());
        assertTrue(generatedChangeCoupon.code().startsWith("TROCO-"));
    }
}
