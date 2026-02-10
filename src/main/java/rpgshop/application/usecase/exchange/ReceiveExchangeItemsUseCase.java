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
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.coupon.Coupon;
import rpgshop.domain.entity.coupon.constant.CouponType;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;
import rpgshop.domain.entity.product.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class ReceiveExchangeItemsUseCase {
    private final ExchangeRequestGateway exchangeRequestGateway;
    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;
    private final CouponGateway couponGateway;

    public ReceiveExchangeItemsUseCase(
        final ExchangeRequestGateway exchangeRequestGateway,
        final OrderGateway orderGateway,
        final ProductGateway productGateway,
        final CouponGateway couponGateway
    ) {
        this.exchangeRequestGateway = exchangeRequestGateway;
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
        this.couponGateway = couponGateway;
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
            returnItemToStock(request);
        }

        generateExchangeCoupon(request);

        final ExchangeRequest completed = request.toBuilder()
            .status(ExchangeStatus.COMPLETED)
            .receivedAt(Instant.now())
            .build();

        final ExchangeRequest saved = exchangeRequestGateway.save(completed);

        final Order order = orderGateway.findById(request.order().id())
            .orElseThrow(() -> new EntityNotFoundException("Order", request.order().id()));

        final Order updatedOrder = order.toBuilder()
            .status(OrderStatus.EXCHANGED)
            .build();
        orderGateway.save(updatedOrder);

        return saved;
    }

    private void returnItemToStock(final ExchangeRequest request) {
        final Product product = productGateway.findById(request.orderItem().product().id())
            .orElseThrow(() -> new EntityNotFoundException("Product", request.orderItem().product().id()));

        final Product updated = product.toBuilder()
            .stockQuantity(product.stockQuantity() + request.quantity())
            .build();

        productGateway.save(updated);
    }

    private void generateExchangeCoupon(final ExchangeRequest request) {
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

        couponGateway.save(coupon);
    }
}
