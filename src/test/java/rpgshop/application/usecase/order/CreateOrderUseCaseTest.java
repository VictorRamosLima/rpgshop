package rpgshop.application.usecase.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.order.CreateOrderCommand;
import rpgshop.application.command.order.PaymentInfo;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.cart.CartGateway;
import rpgshop.application.gateway.cart.CartItemGateway;
import rpgshop.application.gateway.coupon.CouponGateway;
import rpgshop.application.gateway.customer.AddressGateway;
import rpgshop.application.gateway.customer.CreditCardGateway;
import rpgshop.application.gateway.customer.CustomerGateway;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.cart.Cart;
import rpgshop.domain.entity.cart.CartItem;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.CardBrand;
import rpgshop.domain.entity.customer.CreditCard;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderPayment;
import rpgshop.domain.entity.product.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {
    @Mock
    private OrderGateway orderGateway;

    @Mock
    private CartGateway cartGateway;

    @Mock
    private CartItemGateway cartItemGateway;

    @Mock
    private CustomerGateway customerGateway;

    @Mock
    private AddressGateway addressGateway;

    @Mock
    private CreditCardGateway creditCardGateway;

    @Mock
    private CouponGateway couponGateway;

    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private CreateOrderUseCase useCase;

    @Test
    void shouldPrioritizeCouponsByHighestValue() {
        final TestFixture fixture = buildFixture();
        final Coupon lowerCoupon = buildCoupon(fixture.customer, new BigDecimal("30.00"));
        final Coupon higherCoupon = buildCoupon(fixture.customer, new BigDecimal("80.00"));

        configureCommonStubs(fixture);
        when(cartItemGateway.deleteAllByCartId(fixture.cart.id())).thenReturn(1);
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(couponGateway.findById(lowerCoupon.id())).thenReturn(Optional.of(lowerCoupon));
        when(couponGateway.findById(higherCoupon.id())).thenReturn(Optional.of(higherCoupon));

        final List<PaymentInfo> payments = List.of(
            new PaymentInfo(fixture.card.id(), null, new BigDecimal("30.00")),
            new PaymentInfo(null, lowerCoupon.id(), null),
            new PaymentInfo(null, higherCoupon.id(), null)
        );

        final CreateOrderCommand command = new CreateOrderCommand(
            fixture.customer.id(),
            fixture.address.id(),
            payments
        );
        final Order order = useCase.execute(command);

        assertEquals(2, order.payments().size());
        assertEquals(higherCoupon.id(), order.payments().get(0).coupon().id());
        assertEquals(lowerCoupon.id(), order.payments().get(1).coupon().id());
        assertTrue(order.payments().stream().noneMatch(payment -> payment.creditCard() != null));
    }

    @Test
    void shouldAllowCardAmountLowerThanTenWhenCombinedWithCoupon() {
        final TestFixture fixture = buildFixture();
        final Coupon coupon = buildCoupon(fixture.customer, new BigDecimal("100.00"));

        configureCommonStubs(fixture);
        when(cartItemGateway.deleteAllByCartId(fixture.cart.id())).thenReturn(1);
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(couponGateway.findById(coupon.id())).thenReturn(Optional.of(coupon));

        final List<PaymentInfo> payments = List.of(
            new PaymentInfo(fixture.card.id(), null, new BigDecimal("5.50")),
            new PaymentInfo(null, coupon.id(), null)
        );

        final CreateOrderCommand command = new CreateOrderCommand(
            fixture.customer.id(),
            fixture.address.id(),
            payments
        );
        final Order order = useCase.execute(command);
        final OrderPayment cardPayment = order.payments().stream()
            .filter(payment -> payment.creditCard() != null)
            .findFirst()
            .orElseThrow();

        assertEquals(new BigDecimal("5.50"), cardPayment.amount());
    }

    @Test
    void shouldRejectCardAmountLowerThanTenWithoutCoupon() {
        final TestFixture fixture = buildFixture();
        configureCommonStubs(fixture);

        final List<PaymentInfo> payments = List.of(
            new PaymentInfo(fixture.card.id(), null, new BigDecimal("5.00"))
        );

        final CreateOrderCommand command = new CreateOrderCommand(
            fixture.customer.id(),
            fixture.address.id(),
            payments
        );
        final BusinessRuleException exception = assertThrows(
            BusinessRuleException.class,
            () -> useCase.execute(command)
        );

        assertTrue(exception.getMessage().contains("minimo R$ 10,00"));
    }

    private void configureCommonStubs(final TestFixture fixture) {
        when(customerGateway.findById(fixture.customer.id())).thenReturn(Optional.of(fixture.customer));
        when(addressGateway.existsByIdAndCustomerIdOrWithoutCustomer(fixture.address.id(), fixture.customer.id())).thenReturn(true);
        when(addressGateway.findById(fixture.address.id())).thenReturn(Optional.of(fixture.address));
        when(cartGateway.findByCustomerId(fixture.customer.id())).thenReturn(Optional.of(fixture.cart));
        when(cartItemGateway.findByCartId(fixture.cart.id())).thenReturn(List.of(fixture.cartItem));
        when(productGateway.findById(fixture.product.id())).thenReturn(Optional.of(fixture.product));
        when(creditCardGateway.existsByIdAndCustomerId(fixture.card.id(), fixture.customer.id())).thenReturn(true);
        when(creditCardGateway.findById(fixture.card.id())).thenReturn(Optional.of(fixture.card));
    }

    private TestFixture buildFixture() {
        final UUID customerId = UUID.randomUUID();
        final Customer customer = Customer.builder()
            .id(customerId)
            .name("Cliente Teste")
            .build();

        final Address address = Address.builder()
            .id(UUID.randomUUID())
            .purpose(AddressPurpose.DELIVERY)
            .residenceType(ResidenceType.HOUSE)
            .streetType(StreetType.STREET)
            .street("Rua A")
            .number("10")
            .neighborhood("Centro")
            .zipCode("01000000")
            .city("Sao Paulo")
            .state("SP")
            .country("Brasil")
            .isActive(true)
            .build();

        final Product product = Product.builder()
            .id(UUID.randomUUID())
            .name("Livro")
            .salePrice(new BigDecimal("100.00"))
            .weight(new BigDecimal("1.00"))
            .stockQuantity(10)
            .statusChanges(List.of())
            .build();

        final Cart cart = Cart.builder()
            .id(UUID.randomUUID())
            .customer(customer)
            .build();

        final CartItem cartItem = CartItem.builder()
            .id(UUID.randomUUID())
            .cartId(cart.id())
            .product(product)
            .quantity(1)
            .isBlocked(true)
            .blockedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(300))
            .build();

        final CreditCard card = CreditCard.builder()
            .id(UUID.randomUUID())
            .cardNumber("4111111111111111")
            .printedName("CLIENTE TESTE")
            .cardBrand(CardBrand.builder().id(UUID.randomUUID()).name("VISA").isActive(true).build())
            .securityCode("123")
            .isPreferred(true)
            .isActive(true)
            .build();

        return new TestFixture(customer, address, product, cart, cartItem, card);
    }

    private Coupon buildCoupon(final Customer customer, final BigDecimal value) {
        return Coupon.builder()
            .id(UUID.randomUUID())
            .code("CP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
            .type(CouponType.EXCHANGE)
            .value(value)
            .customer(customer)
            .isUsed(false)
            .expiresAt(Instant.now().plusSeconds(3600))
            .build();
    }

    private record TestFixture(
        Customer customer,
        Address address,
        Product product,
        Cart cart,
        CartItem cartItem,
        CreditCard card
    ) {}
}
