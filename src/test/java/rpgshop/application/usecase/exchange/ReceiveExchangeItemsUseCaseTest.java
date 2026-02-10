package rpgshop.application.usecase.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.exchange.ReceiveExchangeItemsCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.coupon.CouponGateway;
import rpgshop.application.gateway.exchange.ExchangeRequestGateway;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.application.usecase.stock.CreateStockReentryUseCase;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;
import rpgshop.domain.entity.order.constant.OrderStatus;
import rpgshop.domain.entity.product.Product;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiveExchangeItemsUseCaseTest {
    @Mock
    private ExchangeRequestGateway exchangeRequestGateway;

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private CouponGateway couponGateway;

    @Mock
    private CreateStockReentryUseCase createStockReentryUseCase;

    @InjectMocks
    private ReceiveExchangeItemsUseCase useCase;

    @Test
    void shouldCompleteExchangeGenerateCouponAndOptionallyReturnItemToStock() {
        final UUID exchangeId = UUID.randomUUID();
        final UUID supplierId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();

        final Customer customer = Customer.builder().id(UUID.randomUUID()).build();
        final Product product = Product.builder().id(productId).build();
        final OrderItem orderItem = OrderItem.builder()
            .id(UUID.randomUUID())
            .product(product)
            .quantity(2)
            .unitPrice(new BigDecimal("80.00"))
            .build();
        final Order order = Order.builder()
            .id(UUID.randomUUID())
            .customer(customer)
            .status(OrderStatus.EXCHANGE_AUTHORIZED)
            .build();
        final ExchangeRequest request = ExchangeRequest.builder()
            .id(exchangeId)
            .order(order)
            .orderItem(orderItem)
            .quantity(2)
            .status(ExchangeStatus.AUTHORIZED)
            .reason("Defeito")
            .build();
        final ReceiveExchangeItemsCommand command = new ReceiveExchangeItemsCommand(
            exchangeId,
            true,
            supplierId,
            new BigDecimal("20.00")
        );

        when(exchangeRequestGateway.findById(exchangeId)).thenReturn(Optional.of(request));
        when(couponGateway.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderGateway.findById(order.id())).thenReturn(Optional.of(order));
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(exchangeRequestGateway.save(any(ExchangeRequest.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        final ExchangeRequest completed = useCase.execute(command);

        assertEquals(ExchangeStatus.COMPLETED, completed.status());
        assertTrue(completed.returnToStock());
        assertNotNull(completed.coupon());
        assertEquals(new BigDecimal("160.00"), completed.coupon().value());
        verify(createStockReentryUseCase).execute(productId, 2, new BigDecimal("20.00"), supplierId);
        verify(orderGateway).save(eq(order.toBuilder().status(OrderStatus.EXCHANGED).build()));
    }

    @Test
    void shouldThrowWhenExchangeIsNotAuthorized() {
        final UUID exchangeId = UUID.randomUUID();
        final ExchangeRequest request = ExchangeRequest.builder()
            .id(exchangeId)
            .status(ExchangeStatus.REQUESTED)
            .build();

        when(exchangeRequestGateway.findById(exchangeId)).thenReturn(Optional.of(request));

        final ReceiveExchangeItemsCommand command = new ReceiveExchangeItemsCommand(exchangeId, false, null, null);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldThrowWhenReturnToStockWithoutSupplier() {
        final UUID exchangeId = UUID.randomUUID();
        final ExchangeRequest request = ExchangeRequest.builder()
            .id(exchangeId)
            .status(ExchangeStatus.AUTHORIZED)
            .order(Order.builder().id(UUID.randomUUID()).build())
            .orderItem(OrderItem.builder().product(Product.builder().id(UUID.randomUUID()).build()).build())
            .build();

        when(exchangeRequestGateway.findById(exchangeId)).thenReturn(Optional.of(request));

        final ReceiveExchangeItemsCommand command = new ReceiveExchangeItemsCommand(
            exchangeId,
            true,
            null,
            new BigDecimal("10.00")
        );

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }
}
