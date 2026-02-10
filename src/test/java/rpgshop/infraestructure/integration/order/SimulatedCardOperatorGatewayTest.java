package rpgshop.infraestructure.integration.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.order.CardOperatorDecision;
import rpgshop.domain.entity.customer.CreditCard;
import rpgshop.domain.entity.order.OrderPayment;
import rpgshop.infraestructure.config.CardOperatorProperties;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimulatedCardOperatorGatewayTest {
    @Mock
    private CardOperatorProperties properties;

    @InjectMocks
    private SimulatedCardOperatorGateway gateway;

    private UUID customerId;
    private BigDecimal orderTotal;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        orderTotal = new BigDecimal("100.00");
    }

    @Test
    void shouldReturnApprovedWhenOperatorIsDisabled() {
        when(properties.isEnabled()).thenReturn(false);

        final List<OrderPayment> payments = List.of(createPayment("1234567890123456", new BigDecimal("100.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, payments);

        assertTrue(decision.approved());
        assertEquals("Validacao de operadora desativada", decision.reason());
        verify(properties).isEnabled();
    }

    @Test
    void shouldRejectWhenPaymentsIsNull() {
        when(properties.isEnabled()).thenReturn(true);

        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, null);

        assertFalse(decision.approved());
        assertEquals("Nenhum pagamento informado para a operadora", decision.reason());
    }

    @Test
    void shouldRejectWhenPaymentsIsEmpty() {
        when(properties.isEnabled()).thenReturn(true);

        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, Collections.emptyList());

        assertFalse(decision.approved());
        assertEquals("Nenhum pagamento informado para a operadora", decision.reason());
    }

    @Test
    void shouldRejectWhenCustomerIdIsNull() {
        when(properties.isEnabled()).thenReturn(true);

        final List<OrderPayment> payments = List.of(createPayment("1234567890123456", new BigDecimal("100.00")));
        final CardOperatorDecision decision = gateway.authorize(null, orderTotal, payments);

        assertFalse(decision.approved());
        assertEquals("Cliente invalido para autorizacao na operadora", decision.reason());
    }

    @Test
    void shouldRejectWhenOrderTotalIsNull() {
        when(properties.isEnabled()).thenReturn(true);

        final List<OrderPayment> payments = List.of(createPayment("1234567890123456", new BigDecimal("100.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, null, payments);

        assertFalse(decision.approved());
        assertEquals("Valor total invalido para autorizacao na operadora", decision.reason());
    }

    @Test
    void shouldRejectWhenOrderTotalIsZero() {
        when(properties.isEnabled()).thenReturn(true);

        final List<OrderPayment> payments = List.of(createPayment("1234567890123456", new BigDecimal("100.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, BigDecimal.ZERO, payments);

        assertFalse(decision.approved());
        assertEquals("Valor total invalido para autorizacao na operadora", decision.reason());
    }

    @Test
    void shouldRejectWhenOrderTotalIsNegative() {
        when(properties.isEnabled()).thenReturn(true);

        final List<OrderPayment> payments = List.of(createPayment("1234567890123456", new BigDecimal("100.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, new BigDecimal("-1.00"), payments);

        assertFalse(decision.approved());
        assertEquals("Valor total invalido para autorizacao na operadora", decision.reason());
    }

    @Test
    void shouldRejectWhenCardNumberIsTooShort() {
        when(properties.isEnabled()).thenReturn(true);

        final List<OrderPayment> payments = List.of(createPayment("123456789012", new BigDecimal("100.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, payments);

        assertFalse(decision.approved());
        assertEquals("Operadora rejeitou cartao com numero invalido", decision.reason());
    }

    @Test
    void shouldRejectWhenCardNumberIsTooLong() {
        when(properties.isEnabled()).thenReturn(true);

        final List<OrderPayment> payments = List.of(createPayment("12345678901234567890", new BigDecimal("100.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, payments);

        assertFalse(decision.approved());
        assertEquals("Operadora rejeitou cartao com numero invalido", decision.reason());
    }

    @Test
    void shouldRejectWhenAmountExceedsMaxAmountPerCard() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getMaxAmountPerCard()).thenReturn(new BigDecimal("5000.00"));

        final List<OrderPayment> payments = List.of(createPayment("1234567890123456", new BigDecimal("6000.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, payments);

        assertFalse(decision.approved());
        assertEquals("Operadora rejeitou valor acima do limite por cartao (R$ 5000.00)", decision.reason());
    }

    @Test
    void shouldUseDefaultMaxAmountWhenPropertyIsNull() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getMaxAmountPerCard()).thenReturn(null);
        when(properties.getRejectedCardSuffixes()).thenReturn(Collections.emptyList());

        final List<OrderPayment> payments = List.of(createPayment("1234567890123456", new BigDecimal("4000.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, payments);

        assertTrue(decision.approved());
    }

    @Test
    void shouldUseDefaultMaxAmountWhenPropertyIsZeroOrNegative() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getMaxAmountPerCard()).thenReturn(BigDecimal.ZERO);
        when(properties.getRejectedCardSuffixes()).thenReturn(Collections.emptyList());

        final List<OrderPayment> payments = List.of(createPayment("1234567890123456", new BigDecimal("4000.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, payments);

        assertTrue(decision.approved());
    }

    @Test
    void shouldRejectWhenCardEndsWithBlockedSuffix() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getMaxAmountPerCard()).thenReturn(new BigDecimal("5000.00"));
        when(properties.getRejectedCardSuffixes()).thenReturn(List.of("0000"));

        final List<OrderPayment> payments = List.of(createPayment("1234567890120000", new BigDecimal("100.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, payments);

        assertFalse(decision.approved());
        assertEquals("Operadora rejeitou o cartao informado", decision.reason());
    }

    @Test
    void shouldApproveWhenAllValidationsPass() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getMaxAmountPerCard()).thenReturn(new BigDecimal("5000.00"));
        when(properties.getRejectedCardSuffixes()).thenReturn(List.of("0000"));

        final List<OrderPayment> payments = List.of(createPayment("1234567890123456", new BigDecimal("100.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, payments);

        assertTrue(decision.approved());
        assertEquals("Pagamento aprovado pela operadora", decision.reason());
    }

    @Test
    void shouldSkipPaymentWithoutCreditCard() {
        when(properties.isEnabled()).thenReturn(true);

        final OrderPayment paymentWithoutCard = OrderPayment.builder()
            .id(UUID.randomUUID())
            .creditCard(null)
            .amount(new BigDecimal("100.00"))
            .build();

        final List<OrderPayment> payments = List.of(paymentWithoutCard);
        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, payments);

        assertTrue(decision.approved());
    }

    @Test
    void shouldIgnoreBlankSuffixes() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getMaxAmountPerCard()).thenReturn(new BigDecimal("5000.00"));
        when(properties.getRejectedCardSuffixes()).thenReturn(List.of("", "   ", "---"));

        final List<OrderPayment> payments = List.of(createPayment("1234567890123456", new BigDecimal("100.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, payments);

        assertTrue(decision.approved());
    }

    @Test
    void shouldIgnoreNullSuffixInList() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getMaxAmountPerCard()).thenReturn(new BigDecimal("5000.00"));
        final List<String> suffixesWithNull = new java.util.ArrayList<>();
        suffixesWithNull.add(null);
        suffixesWithNull.add("---");
        when(properties.getRejectedCardSuffixes()).thenReturn(suffixesWithNull);

        final List<OrderPayment> payments = List.of(createPayment("1234567890123456", new BigDecimal("100.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, payments);

        assertTrue(decision.approved());
    }

    @Test
    void shouldNotBlockWhenRejectedSuffixesIsNull() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getMaxAmountPerCard()).thenReturn(new BigDecimal("5000.00"));
        when(properties.getRejectedCardSuffixes()).thenReturn(null);

        final List<OrderPayment> payments = List.of(createPayment("1234567890123456", new BigDecimal("100.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, payments);

        assertTrue(decision.approved());
    }

    @Test
    void shouldNormalizeCardNumberWithSpecialCharacters() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getMaxAmountPerCard()).thenReturn(new BigDecimal("5000.00"));
        when(properties.getRejectedCardSuffixes()).thenReturn(Collections.emptyList());

        final List<OrderPayment> payments = List.of(createPayment("1234-5678-9012-3456", new BigDecimal("100.00")));
        final CardOperatorDecision decision = gateway.authorize(customerId, orderTotal, payments);

        assertTrue(decision.approved());
    }

    private OrderPayment createPayment(final String cardNumber, final BigDecimal amount) {
        final CreditCard creditCard = CreditCard.builder()
            .id(UUID.randomUUID())
            .cardNumber(cardNumber)
            .printedName("Test User")
            .build();

        return OrderPayment.builder()
            .id(UUID.randomUUID())
            .creditCard(creditCard)
            .amount(amount)
            .build();
    }
}

