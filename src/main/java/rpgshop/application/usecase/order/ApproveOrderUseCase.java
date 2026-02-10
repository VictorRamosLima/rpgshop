package rpgshop.application.usecase.order;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.order.OrderGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;
import rpgshop.domain.entity.order.constant.OrderStatus;
import rpgshop.domain.entity.product.Product;

import java.util.UUID;

@Service
public class ApproveOrderUseCase {
    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;

    public ApproveOrderUseCase(final OrderGateway orderGateway, final ProductGateway productGateway) {
        this.orderGateway = orderGateway;
        this.productGateway = productGateway;
    }

    @Nonnull
    @Transactional
    public Order execute(@Nonnull final UUID orderId) {
        final Order order = orderGateway.findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException("Order", orderId));

        if (order.status() != OrderStatus.PROCESSING) {
            throw new BusinessRuleException("Only orders with status PROCESSING can be approved");
        }

        for (final OrderItem item : order.items()) {
            final Product product = productGateway.findById(item.product().id())
                .orElseThrow(() -> new EntityNotFoundException("Product", item.product().id()));

            final Product updated = product.toBuilder()
                .stockQuantity(product.stockQuantity() - item.quantity())
                .build();
            productGateway.save(updated);
        }

        final Order approved = order.toBuilder()
            .status(OrderStatus.APPROVED)
            .build();

        return orderGateway.save(approved);
    }
}
