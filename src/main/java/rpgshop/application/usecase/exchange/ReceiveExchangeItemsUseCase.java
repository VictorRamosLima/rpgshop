package rpgshop.application.usecase.exchange;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.exchange.ReceiveExchangeItemsCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.coupon.CouponGateway;
import rpgshop.application.gateway.exchange.ExchangeRequestGateway;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.application.usecase.stock.CreateStockReentryUseCase;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class ReceiveExchangeItemsUseCase {
    private final ExchangeRequestGateway exchangeRequestGateway;
    private final OrderGateway orderGateway;
    private final CouponGateway couponGateway;
    private final CreateStockReentryUseCase createStockReentryUseCase;

    public ReceiveExchangeItemsUseCase(
        final ExchangeRequestGateway exchangeRequestGateway,
        final OrderGateway orderGateway,
        final CouponGateway couponGateway,
        final CreateStockReentryUseCase createStockReentryUseCase
    ) {
        this.exchangeRequestGateway = exchangeRequestGateway;
        this.orderGateway = orderGateway;
        this.couponGateway = couponGateway;
        this.createStockReentryUseCase = createStockReentryUseCase;
    }

    @Nonnull
    @Transactional
    public ExchangeRequest execute(@Nonnull final ReceiveExchangeItemsCommand command) {
        final ExchangeRequest request = exchangeRequestGateway.findById(command.exchangeRequestId())
            .orElseThrow(() -> new EntityNotFoundException("ExchangeRequest", command.exchangeRequestId()));

        if (request.status() != ExchangeStatus.AUTHORIZED) {
            throw new BusinessRuleException("Somente solicitacoes de troca autorizadas podem receber itens");
        }

        if (command.returnToStock()) {
            if (command.supplierId() == null) {
                throw new BusinessRuleException("Fornecedor e obrigatorio para reentrada em estoque");
            }
            if (command.costValue() == null || command.costValue().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessRuleException("Valor de custo e obrigatorio para reentrada em estoque");
            }

            createStockReentryUseCase.execute(
                request.orderItem().product().id(),
                request.quantity(),
                command.costValue(),
                command.supplierId()
            );
        }

        final Coupon generatedCoupon = generateExchangeCoupon(request);

        final ExchangeRequest completed = request.toBuilder()
            .status(ExchangeStatus.COMPLETED)
            .receivedAt(Instant.now())
            .returnToStock(command.returnToStock())
            .coupon(generatedCoupon)
            .build();

        final ExchangeRequest saved = exchangeRequestGateway.save(completed);

        final Order order = orderGateway.findById(request.order().id())
            .orElseThrow(() -> new EntityNotFoundException("Order", request.order().id()));

        if (order.status() != OrderStatus.EXCHANGE_AUTHORIZED && order.status() != OrderStatus.IN_EXCHANGE) {
            throw new BusinessRuleException("Somente pedidos em troca podem ser atualizados para TROCADO");
        }

        final Order updatedOrder = order.toBuilder()
            .status(OrderStatus.EXCHANGED)
            .build();
        orderGateway.save(updatedOrder);

        return saved;
    }

    private Coupon generateExchangeCoupon(final ExchangeRequest request) {
        final BigDecimal couponValue = request.orderItem().unitPrice()
            .multiply(BigDecimal.valueOf(request.quantity()));

        final Coupon coupon = Coupon.builder()
            .id(UUID.randomUUID())
            .code("TROCA-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
            .type(CouponType.EXCHANGE)
            .value(couponValue)
            .customer(request.order().customer())
            .isUsed(false)
            .expiresAt(Instant.now().plus(90, ChronoUnit.DAYS))
            .build();

        return couponGateway.save(coupon);
    }
}
