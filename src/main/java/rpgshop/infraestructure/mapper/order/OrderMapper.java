package rpgshop.infraestructure.mapper.order;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import rpgshop.application.exception.IllegalInstantiationException;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;
import rpgshop.domain.entity.order.OrderPayment;
import rpgshop.infraestructure.mapper.customer.AddressMapper;
import rpgshop.infraestructure.mapper.customer.CustomerMapper;
import rpgshop.infraestructure.persistence.entity.order.OrderJpaEntity;

import java.util.ArrayList;
import java.util.Collections;

public final class OrderMapper {
    private OrderMapper() {
        throw new IllegalInstantiationException(this.getClass());
    }

    @Nonnull
    public static Order toDomain(final OrderJpaEntity entity) {
        Assert.notNull(entity, "'OrderJpaEntity' should not be null");

        var items = entity.getItems() != null
            ? entity.getItems().stream().map(OrderItemMapper::toDomain).toList()
            : Collections.<OrderItem>emptyList();

        var payments = entity.getPayments() != null
            ? entity.getPayments().stream().map(OrderPaymentMapper::toDomain).toList()
            : Collections.<OrderPayment>emptyList();

        return Order.builder()
            .id(entity.getId())
            .customer(CustomerMapper.toDomain(entity.getCustomer()))
            .status(entity.getStatus())
            .deliveryAddress(AddressMapper.toDomain(entity.getDeliveryAddress()))
            .freightCost(entity.getFreightCost())
            .subtotal(entity.getSubtotal())
            .total(entity.getTotal())
            .purchasedAt(entity.getPurchasedAt())
            .dispatchedAt(entity.getDispatchedAt())
            .deliveredAt(entity.getDeliveredAt())
            .items(items)
            .payments(payments)
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    @Nonnull
    public static OrderJpaEntity toEntity(final Order domain) {
        Assert.notNull(domain, "'Order' should not be null");

        var entity = OrderJpaEntity.builder()
            .id(domain.id())
            .customer(CustomerMapper.toEntity(domain.customer()))
            .status(domain.status())
            .deliveryAddress(AddressMapper.toEntity(domain.deliveryAddress()))
            .freightCost(domain.freightCost())
            .subtotal(domain.subtotal())
            .total(domain.total())
            .purchasedAt(domain.purchasedAt())
            .dispatchedAt(domain.dispatchedAt())
            .deliveredAt(domain.deliveredAt())
            .createdAt(domain.createdAt())
            .updatedAt(domain.updatedAt())
            .build();

        if (domain.items() != null) {
            var items = new ArrayList<>(domain.items().stream()
                .map(OrderItemMapper::toEntity).toList());
            items.forEach(item -> item.setOrder(entity));
            entity.setItems(items);
        }

        if (domain.payments() != null) {
            var payments = new ArrayList<>(domain.payments().stream()
                .map(OrderPaymentMapper::toEntity).toList());
            payments.forEach(payment -> payment.setOrder(entity));
            entity.setPayments(payments);
        }

        return entity;
    }
}
