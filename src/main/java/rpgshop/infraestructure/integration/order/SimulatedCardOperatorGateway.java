package rpgshop.infraestructure.integration.order;

import org.springframework.stereotype.Component;
import rpgshop.application.command.order.CardOperatorDecision;
import rpgshop.application.gateway.order.CardOperatorGateway;
import rpgshop.domain.entity.order.OrderPayment;
import rpgshop.infraestructure.config.CardOperatorProperties;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class SimulatedCardOperatorGateway implements CardOperatorGateway {
    private final CardOperatorProperties properties;

    public SimulatedCardOperatorGateway(final CardOperatorProperties properties) {
        this.properties = properties;
    }

    @Override
    public CardOperatorDecision authorize(
        final UUID customerId,
        final BigDecimal orderTotal,
        final List<OrderPayment> payments
    ) {
        if (!properties.isEnabled()) {
            return new CardOperatorDecision(true, "Validacao de operadora desativada");
        }

        if (payments == null || payments.isEmpty()) {
            return new CardOperatorDecision(false, "Nenhum pagamento informado para a operadora");
        }
        if (customerId == null) {
            return new CardOperatorDecision(false, "Cliente invalido para autorizacao na operadora");
        }
        if (orderTotal == null || orderTotal.compareTo(BigDecimal.ZERO) <= 0) {
            return new CardOperatorDecision(false, "Valor total invalido para autorizacao na operadora");
        }

        for (final OrderPayment payment : payments) {
            if (payment.creditCard() == null) {
                continue;
            }

            final String cardNumber = normalizeCardNumber(payment.creditCard().cardNumber());
            if (cardNumber.length() < 13 || cardNumber.length() > 19) {
                return new CardOperatorDecision(false, "Operadora rejeitou cartao com numero invalido");
            }

            if (payment.amount() != null && payment.amount().compareTo(maxAmountPerCard()) > 0) {
                return new CardOperatorDecision(
                    false,
                    "Operadora rejeitou valor acima do limite por cartao (R$ %s)".formatted(maxAmountPerCard())
                );
            }

            if (isBlockedCard(cardNumber)) {
                return new CardOperatorDecision(false, "Operadora rejeitou o cartao informado");
            }
        }

        return new CardOperatorDecision(true, "Pagamento aprovado pela operadora");
    }

    private BigDecimal maxAmountPerCard() {
        if (properties.getMaxAmountPerCard() == null || properties.getMaxAmountPerCard().compareTo(BigDecimal.ZERO) <= 0) {
            return new BigDecimal("5000.00");
        }
        return properties.getMaxAmountPerCard();
    }

    private boolean isBlockedCard(final String cardNumber) {
        if (properties.getRejectedCardSuffixes() == null || properties.getRejectedCardSuffixes().isEmpty()) {
            return false;
        }

        for (final String suffix : properties.getRejectedCardSuffixes()) {
            if (suffix == null || suffix.isBlank()) {
                continue;
            }
            final String normalizedSuffix = suffix.replaceAll("\\D", "");
            if (normalizedSuffix.isBlank()) {
                continue;
            }
            if (cardNumber.endsWith(normalizedSuffix)) {
                return true;
            }
        }

        return false;
    }

    private String normalizeCardNumber(final String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("\\D", "");
    }
}
