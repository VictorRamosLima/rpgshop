package rpgshop.presentation.controller.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import rpgshop.application.command.order.CreateOrderCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.coupon.CouponGateway;
import rpgshop.application.gateway.customer.AddressGateway;
import rpgshop.application.gateway.customer.CardBrandGateway;
import rpgshop.application.gateway.customer.CreditCardGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.usecase.customer.CreateCreditCardUseCase;
import rpgshop.application.usecase.order.ApproveOrderUseCase;
import rpgshop.application.usecase.order.CreateOrderUseCase;
import rpgshop.application.usecase.order.DeliverOrderUseCase;
import rpgshop.application.usecase.order.DispatchOrderUseCase;
import rpgshop.application.usecase.order.QueryOrdersUseCase;
import rpgshop.application.usecase.order.RejectOrderUseCase;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.CardBrand;
import rpgshop.domain.entity.customer.CreditCard;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @Mock
    private CreateOrderUseCase createOrderUseCase;
    @Mock
    private ApproveOrderUseCase approveOrderUseCase;
    @Mock
    private RejectOrderUseCase rejectOrderUseCase;
    @Mock
    private DispatchOrderUseCase dispatchOrderUseCase;
    @Mock
    private DeliverOrderUseCase deliverOrderUseCase;
    @Mock
    private QueryOrdersUseCase queryOrdersUseCase;
    @Mock
    private AddressGateway addressGateway;
    @Mock
    private CreditCardGateway creditCardGateway;
    @Mock
    private CouponGateway couponGateway;
    @Mock
    private CardBrandGateway cardBrandGateway;
    @Mock
    private CreateCreditCardUseCase createCreditCardUseCase;
    @Mock
    private CustomerGateway customerGateway;
    @Mock
    private Model model;

    @InjectMocks
    private OrderController controller;

    @Test
    void shouldListOrdersByStatus() {
        final Page<Order> orders = new PageImpl<>(List.of(Order.builder().id(UUID.randomUUID()).build()));
        when(queryOrdersUseCase.findByStatus(OrderStatus.PROCESSING, PageRequest.of(1, 10))).thenReturn(orders);

        final String view = controller.list(OrderStatus.PROCESSING, 1, model);

        assertEquals("order/list", view);
        verify(queryOrdersUseCase).findByStatus(OrderStatus.PROCESSING, PageRequest.of(1, 10));
        verify(model).addAttribute("orders", orders);
        verify(model).addAttribute(eq("statuses"), any());
    }

    @Test
    void shouldRedirectToCustomersWhenCheckoutCustomerDoesNotExist() {
        final UUID customerId = UUID.randomUUID();
        when(customerGateway.findById(customerId)).thenReturn(Optional.empty());

        final String view = controller.showCheckout(customerId, model);

        assertEquals("redirect:/customers", view);
        verify(customerGateway).findById(customerId);
    }

    @Test
    void shouldRenderCheckoutWhenCustomerExists() {
        final UUID customerId = UUID.randomUUID();
        when(customerGateway.findById(customerId)).thenReturn(Optional.of(Customer.builder().id(customerId).build()));
        when(addressGateway.findByCustomerIdAndPurpose(customerId, AddressPurpose.DELIVERY)).thenReturn(List.of());
        when(addressGateway.findActiveByCustomerId(customerId))
            .thenReturn(List.of(Address.builder().id(UUID.randomUUID()).build()));
        when(creditCardGateway.findActiveByCustomerId(customerId))
            .thenReturn(List.of(CreditCard.builder().id(UUID.randomUUID()).build()));
        when(couponGateway.findAvailableByCustomerId(eq(customerId), any(Instant.class))).thenReturn(List.of());
        when(cardBrandGateway.findActive()).thenReturn(List.of(CardBrand.builder().id(UUID.randomUUID()).build()));

        final String view = controller.showCheckout(customerId, model);

        assertEquals("order/checkout", view);
        verify(model).addAttribute("customerId", customerId);
        verify(model).addAttribute(eq("deliveryAddresses"), any());
        verify(model).addAttribute(eq("creditCards"), any());
        verify(model).addAttribute(eq("availableCoupons"), any());
        verify(model).addAttribute(eq("cardBrands"), any());
    }

    @Test
    void shouldCheckoutUsingExistingAddressAndRedirect() {
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final UUID cardId = UUID.randomUUID();
        final UUID orderId = UUID.randomUUID();
        when(addressGateway.existsByIdAndCustomerId(addressId, customerId)).thenReturn(true);
        when(createOrderUseCase.execute(any())).thenReturn(Order.builder().id(orderId).build());

        final String view = controller.checkout(
            customerId,
            "existing",
            addressId.toString(),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            false,
            List.of(cardId.toString()),
            List.of(""),
            List.of("45.00"),
            false,
            null,
            null,
            null,
            null,
            false,
            null,
            null,
            model
        );

        assertEquals("redirect:/orders/" + orderId, view);
        final ArgumentCaptor<CreateOrderCommand> captor = ArgumentCaptor.forClass(CreateOrderCommand.class);
        verify(createOrderUseCase).execute(captor.capture());
        assertEquals(customerId, captor.getValue().customerId());
        assertEquals(addressId, captor.getValue().deliveryAddressId());
        assertEquals(1, captor.getValue().payments().size());
        assertEquals(cardId, captor.getValue().payments().getFirst().creditCardId());
        assertEquals(new BigDecimal("45.00"), captor.getValue().payments().getFirst().amount());
    }

    @Test
    void shouldRenderCheckoutWithErrorWhenCheckoutFails() {
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final UUID cardId = UUID.randomUUID();
        when(addressGateway.existsByIdAndCustomerId(addressId, customerId)).thenReturn(true);
        when(createOrderUseCase.execute(any())).thenThrow(new BusinessRuleException("pagamento invalido"));
        when(addressGateway.findByCustomerIdAndPurpose(customerId, AddressPurpose.DELIVERY)).thenReturn(List.of());
        when(addressGateway.findActiveByCustomerId(customerId))
            .thenReturn(List.of(Address.builder().id(UUID.randomUUID()).build()));
        when(creditCardGateway.findActiveByCustomerId(customerId))
            .thenReturn(List.of(CreditCard.builder().id(UUID.randomUUID()).build()));
        when(couponGateway.findAvailableByCustomerId(eq(customerId), any(Instant.class))).thenReturn(List.of());
        when(cardBrandGateway.findActive()).thenReturn(List.of(CardBrand.builder().id(UUID.randomUUID()).build()));

        final String view = controller.checkout(
            customerId,
            "existing",
            addressId.toString(),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            false,
            List.of(cardId.toString()),
            List.of(""),
            List.of("45.00"),
            false,
            null,
            null,
            null,
            null,
            false,
            null,
            null,
            model
        );

        assertEquals("order/checkout", view);
        verify(model).addAttribute("error", "pagamento invalido");
        verify(model).addAttribute("customerId", customerId);
    }
}
