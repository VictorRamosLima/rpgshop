package rpgshop.infraestructure.persistence.gateway;

import org.springframework.stereotype.Component;
import rpgshop.application.gateway.order.OrderPaymentGateway;
import rpgshop.domain.entity.order.OrderPayment;
import rpgshop.infraestructure.mapper.order.OrderPaymentMapper;
import rpgshop.infraestructure.persistence.entity.order.OrderPaymentJpaEntity;
import rpgshop.infraestructure.persistence.repository.order.OrderPaymentRepository;
import rpgshop.infraestructure.persistence.repository.order.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class OrderPaymentGatewayJpa implements OrderPaymentGateway {
    private final OrderPaymentRepository orderPaymentRepository;
    private final OrderRepository orderRepository;

    public OrderPaymentGatewayJpa(final OrderPaymentRepository orderPaymentRepository, final OrderRepository orderRepository) {
        this.orderPaymentRepository = orderPaymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderPayment save(final OrderPayment payment, final UUID orderId) {
        final OrderPaymentJpaEntity entity = OrderPaymentMapper.toEntity(payment);
        orderRepository.findById(orderId).ifPresent(entity::setOrder);
        final OrderPaymentJpaEntity saved = orderPaymentRepository.save(entity);
        return OrderPaymentMapper.toDomain(saved);
    }

    @Override
    public List<OrderPayment> findByOrderId(final UUID orderId) {
        return orderPaymentRepository.findByOrderId(orderId)
            .stream().map(OrderPaymentMapper::toDomain).toList();
    }

    @Override
    public BigDecimal sumAmountByOrderId(final UUID orderId) {
        return orderPaymentRepository.sumAmountByOrderId(orderId);
    }

    @Override
    public boolean existsPromotionalCouponByOrderId(final UUID orderId) {
        return orderPaymentRepository.existsPromotionalCouponByOrderId(orderId);
    }
}
