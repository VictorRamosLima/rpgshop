package rpgshop.infraestructure.persistence.gateway.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rpgshop.domain.entity.order.OrderItem;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.infraestructure.persistence.entity.order.OrderItemJpaEntity;
import rpgshop.infraestructure.persistence.entity.order.OrderJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.PricingGroupJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductTypeJpaEntity;
import rpgshop.infraestructure.persistence.repository.order.OrderItemRepository;
import rpgshop.infraestructure.persistence.repository.order.OrderRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderItemGatewayJpaTest {
    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderItemGatewayJpa orderItemGatewayJpa;

    private ProductJpaEntity createProductJpaEntity(UUID productId, Instant now) {
        final UUID typeId = UUID.randomUUID();
        final UUID pricingId = UUID.randomUUID();

        final ProductTypeJpaEntity typeJpa = ProductTypeJpaEntity.builder()
            .id(typeId).name("Type").isActive(true).createdAt(now).updatedAt(now).build();
        final PricingGroupJpaEntity pricingJpa = PricingGroupJpaEntity.builder()
            .id(pricingId).name("Pricing").marginPercentage(BigDecimal.TEN).isActive(true)
            .createdAt(now).updatedAt(now).build();

        return ProductJpaEntity.builder()
            .id(productId).name("Product").productType(typeJpa).pricingGroup(pricingJpa)
            .stockQuantity(10).createdAt(now).updatedAt(now).build();
    }

    private Product createProduct(UUID productId, Instant now) {
        final UUID typeId = UUID.randomUUID();
        final UUID pricingId = UUID.randomUUID();

        final ProductType type = ProductType.builder()
            .id(typeId).name("Type").isActive(true).createdAt(now).updatedAt(now).build();
        final PricingGroup pricing = PricingGroup.builder()
            .id(pricingId).name("Pricing").marginPercentage(BigDecimal.TEN).isActive(true)
            .createdAt(now).updatedAt(now).build();

        return Product.builder()
            .id(productId).name("Product").productType(type).pricingGroup(pricing)
            .stockQuantity(10).createdAt(now).updatedAt(now).build();
    }

    @Test
    void shouldSaveOrderItem() {
        final UUID orderItemId = UUID.randomUUID();
        final UUID orderId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final Instant now = Instant.now();

        final Product product = createProduct(productId, now);
        final OrderItem orderItem = OrderItem.builder()
            .id(orderItemId).product(product).quantity(2)
            .unitPrice(new BigDecimal("50.00")).totalPrice(new BigDecimal("100.00"))
            .createdAt(now).updatedAt(now).build();

        final OrderJpaEntity orderEntity = OrderJpaEntity.builder().id(orderId).build();
        final ProductJpaEntity productEntity = createProductJpaEntity(productId, now);

        final OrderItemJpaEntity savedEntity = OrderItemJpaEntity.builder()
            .id(orderItemId).order(orderEntity).product(productEntity).quantity(2)
            .unitPrice(new BigDecimal("50.00")).totalPrice(new BigDecimal("100.00"))
            .createdAt(now).updatedAt(now).build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderEntity));
        when(orderItemRepository.save(any(OrderItemJpaEntity.class))).thenReturn(savedEntity);

        final OrderItem result = orderItemGatewayJpa.save(orderItem, orderId);

        assertNotNull(result);
        assertEquals(orderItemId, result.id());
        assertEquals(2, result.quantity());
        verify(orderRepository, times(1)).findById(orderId);
        verify(orderItemRepository, times(1)).save(any(OrderItemJpaEntity.class));
    }

    @Test
    void shouldFindById() {
        final UUID orderItemId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final Instant now = Instant.now();

        final ProductJpaEntity productEntity = createProductJpaEntity(productId, now);

        final OrderItemJpaEntity entity = OrderItemJpaEntity.builder()
            .id(orderItemId).product(productEntity).quantity(2)
            .unitPrice(new BigDecimal("50.00")).totalPrice(new BigDecimal("100.00"))
            .createdAt(now).updatedAt(now).build();

        when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(entity));

        final Optional<OrderItem> result = orderItemGatewayJpa.findById(orderItemId);

        assertTrue(result.isPresent());
        assertEquals(orderItemId, result.get().id());
        verify(orderItemRepository, times(1)).findById(orderItemId);
    }

    @Test
    void shouldReturnEmptyWhenOrderItemNotFoundById() {
        final UUID orderItemId = UUID.randomUUID();

        when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.empty());

        assertTrue(orderItemGatewayJpa.findById(orderItemId).isEmpty());
        verify(orderItemRepository, times(1)).findById(orderItemId);
    }

    @Test
    void shouldFindByOrderId() {
        final UUID orderId = UUID.randomUUID();
        final UUID orderItemId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final Instant now = Instant.now();

        final ProductJpaEntity productEntity = createProductJpaEntity(productId, now);

        final OrderItemJpaEntity entity = OrderItemJpaEntity.builder()
            .id(orderItemId).product(productEntity).quantity(2)
            .unitPrice(new BigDecimal("50.00")).totalPrice(new BigDecimal("100.00"))
            .createdAt(now).updatedAt(now).build();

        when(orderItemRepository.findByOrderId(orderId)).thenReturn(List.of(entity));

        final List<OrderItem> result = orderItemGatewayJpa.findByOrderId(orderId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderItemId, result.getFirst().id());
        verify(orderItemRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void shouldFindDeliveredItemsByOrderId() {
        final UUID orderId = UUID.randomUUID();
        final UUID orderItemId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final Instant now = Instant.now();

        final ProductJpaEntity productEntity = createProductJpaEntity(productId, now);

        final OrderItemJpaEntity entity = OrderItemJpaEntity.builder()
            .id(orderItemId).product(productEntity).quantity(2)
            .unitPrice(new BigDecimal("50.00")).totalPrice(new BigDecimal("100.00"))
            .createdAt(now).updatedAt(now).build();

        when(orderItemRepository.findDeliveredItemsByOrderId(orderId)).thenReturn(List.of(entity));

        final List<OrderItem> result = orderItemGatewayJpa.findDeliveredItemsByOrderId(orderId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderItemRepository, times(1)).findDeliveredItemsByOrderId(orderId);
    }

    @Test
    void shouldFindByProductIdAndPeriod() {
        final UUID productId = UUID.randomUUID();
        final UUID orderItemId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Instant startDate = now.minusSeconds(86400);
        final Pageable pageable = PageRequest.of(0, 10);

        final ProductJpaEntity productEntity = createProductJpaEntity(productId, now);

        final OrderItemJpaEntity entity = OrderItemJpaEntity.builder()
            .id(orderItemId).product(productEntity).quantity(2)
            .unitPrice(new BigDecimal("50.00")).totalPrice(new BigDecimal("100.00"))
            .createdAt(now).updatedAt(now).build();

        final Page<OrderItemJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(orderItemRepository.findByProductIdAndPeriod(productId, startDate, now, pageable)).thenReturn(page);

        final Page<OrderItem> result = orderItemGatewayJpa.findByProductIdAndPeriod(productId, startDate, now, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderItemRepository, times(1)).findByProductIdAndPeriod(productId, startDate, now, pageable);
    }

    @Test
    void shouldFindByCategoryIdAndPeriod() {
        final UUID categoryId = UUID.randomUUID();
        final UUID orderItemId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Instant startDate = now.minusSeconds(86400);
        final Pageable pageable = PageRequest.of(0, 10);

        final ProductJpaEntity productEntity = createProductJpaEntity(productId, now);

        final OrderItemJpaEntity entity = OrderItemJpaEntity.builder()
            .id(orderItemId).product(productEntity).quantity(2)
            .unitPrice(new BigDecimal("50.00")).totalPrice(new BigDecimal("100.00"))
            .createdAt(now).updatedAt(now).build();

        final Page<OrderItemJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(orderItemRepository.findByCategoryIdAndPeriod(categoryId, startDate, now, pageable)).thenReturn(page);

        final Page<OrderItem> result = orderItemGatewayJpa.findByCategoryIdAndPeriod(categoryId, startDate, now, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderItemRepository, times(1)).findByCategoryIdAndPeriod(categoryId, startDate, now, pageable);
    }

    @Test
    void shouldSumQuantitySoldByProductIdAndPeriod() {
        final UUID productId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Instant startDate = now.minusSeconds(86400);

        when(orderItemRepository.sumQuantitySoldByProductIdAndPeriod(productId, startDate, now)).thenReturn(50L);

        final long result = orderItemGatewayJpa.sumQuantitySoldByProductIdAndPeriod(productId, startDate, now);

        assertEquals(50L, result);
        verify(orderItemRepository, times(1)).sumQuantitySoldByProductIdAndPeriod(productId, startDate, now);
    }

    @Test
    void shouldReturnZeroWhenNoQuantitySoldByProductIdAndPeriod() {
        final UUID productId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Instant startDate = now.minusSeconds(86400);

        when(orderItemRepository.sumQuantitySoldByProductIdAndPeriod(productId, startDate, now)).thenReturn(0L);

        final long result = orderItemGatewayJpa.sumQuantitySoldByProductIdAndPeriod(productId, startDate, now);

        assertEquals(0L, result);
        verify(orderItemRepository, times(1)).sumQuantitySoldByProductIdAndPeriod(productId, startDate, now);
    }
}
