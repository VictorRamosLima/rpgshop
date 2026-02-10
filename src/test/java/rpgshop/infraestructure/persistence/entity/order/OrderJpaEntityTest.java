package rpgshop.infraestructure.persistence.entity.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import rpgshop.domain.entity.order.constant.OrderStatus;
import rpgshop.infraestructure.persistence.entity.customer.AddressJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rpgshop.domain.entity.order.constant.OrderStatus.PROCESSING;

@DisplayName("OrderJpaEntity")
class OrderJpaEntityTest {
    @Nested
    @DisplayName("Builder")
    class BuilderTests {
        @Test
        @DisplayName("should create entity with all fields")
        void shouldCreateEntityWithAllFields() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final String orderCode = "ORD-2024-001";
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final OrderStatus status = OrderStatus.APPROVED;
            final AddressJpaEntity deliveryAddress = AddressJpaEntity.builder().build();
            final BigDecimal freightCost = new BigDecimal("15.00");
            final BigDecimal subtotal = new BigDecimal("100.00");
            final BigDecimal total = new BigDecimal("115.00");
            final Instant purchasedAt = Instant.now();
            final Instant dispatchedAt = Instant.now();
            final Instant deliveredAt = Instant.now();
            final List<OrderItemJpaEntity> items = new ArrayList<>();
            final List<OrderPaymentJpaEntity> payments = new ArrayList<>();

            final OrderJpaEntity entity = OrderJpaEntity.builder()
                .id(id)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .orderCode(orderCode)
                .customer(customer)
                .status(status)
                .deliveryAddress(deliveryAddress)
                .freightCost(freightCost)
                .subtotal(subtotal)
                .total(total)
                .purchasedAt(purchasedAt)
                .dispatchedAt(dispatchedAt)
                .deliveredAt(deliveredAt)
                .items(items)
                .payments(payments)
                .build();

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(orderCode, entity.getOrderCode());
            assertEquals(customer, entity.getCustomer());
            assertEquals(status, entity.getStatus());
            assertEquals(deliveryAddress, entity.getDeliveryAddress());
            assertEquals(freightCost, entity.getFreightCost());
            assertEquals(subtotal, entity.getSubtotal());
            assertEquals(total, entity.getTotal());
            assertEquals(purchasedAt, entity.getPurchasedAt());
            assertEquals(dispatchedAt, entity.getDispatchedAt());
            assertEquals(deliveredAt, entity.getDeliveredAt());
            assertEquals(items, entity.getItems());
            assertEquals(payments, entity.getPayments());
        }

        @Test
        @DisplayName("should create entity with all order status types")
        void shouldCreateEntityWithAllOrderStatusTypes() {
            for (OrderStatus status : OrderStatus.values()) {
                final OrderJpaEntity entity = OrderJpaEntity.builder()
                    .status(status)
                    .build();

                assertEquals(status, entity.getStatus());
            }
        }

        @Test
        @DisplayName("should create entity with default status as PROCESSING")
        void shouldCreateEntityWithDefaultStatusAsProcessing() {
            final OrderJpaEntity entity = OrderJpaEntity.builder().build();
            assertEquals(PROCESSING, entity.getStatus());
        }

        @Test
        @DisplayName("should create entity with default empty lists")
        void shouldCreateEntityWithDefaultEmptyLists() {
            final OrderJpaEntity entity = OrderJpaEntity.builder().build();

            assertNotNull(entity.getItems());
            assertNotNull(entity.getPayments());
            assertTrue(entity.getItems().isEmpty());
            assertTrue(entity.getPayments().isEmpty());
        }

        @Test
        @DisplayName("should create entity with null values")
        void shouldCreateEntityWithNullValues() {
            final OrderJpaEntity entity = OrderJpaEntity.builder().build();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getOrderCode());
            assertNull(entity.getCustomer());
            assertNull(entity.getDeliveryAddress());
            assertNull(entity.getFreightCost());
            assertNull(entity.getSubtotal());
            assertNull(entity.getTotal());
            assertNull(entity.getPurchasedAt());
            assertNull(entity.getDispatchedAt());
            assertNull(entity.getDeliveredAt());
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor")
    class NoArgsConstructorTests {
        @Test
        @DisplayName("should create empty entity")
        void shouldCreateEmptyEntity() {
            final OrderJpaEntity entity = new OrderJpaEntity();

            assertNull(entity.getId());
            assertNull(entity.getCreatedAt());
            assertNull(entity.getUpdatedAt());
            assertNull(entity.getOrderCode());
            assertNull(entity.getCustomer());
            assertEquals(PROCESSING, entity.getStatus());
            assertNull(entity.getDeliveryAddress());
            assertNull(entity.getFreightCost());
            assertNull(entity.getSubtotal());
            assertNull(entity.getTotal());
            assertNull(entity.getPurchasedAt());
            assertNull(entity.getDispatchedAt());
            assertNull(entity.getDeliveredAt());
            assertNotNull(entity.getItems());
            assertNotNull(entity.getPayments());
        }
    }

    @Nested
    @DisplayName("AllArgsConstructor")
    class AllArgsConstructorTests {
        @Test
        @DisplayName("should create entity with all args")
        void shouldCreateEntityWithAllArgs() {
            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final String orderCode = "ORD-2024-002";
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final OrderStatus status = OrderStatus.DELIVERED;
            final AddressJpaEntity deliveryAddress = AddressJpaEntity.builder().build();
            final BigDecimal freightCost = new BigDecimal("20.00");
            final BigDecimal subtotal = new BigDecimal("200.00");
            final BigDecimal total = new BigDecimal("220.00");
            final Instant purchasedAt = Instant.now();
            final Instant dispatchedAt = Instant.now();
            final Instant deliveredAt = Instant.now();
            final List<OrderItemJpaEntity> items = new ArrayList<>();
            final List<OrderPaymentJpaEntity> payments = new ArrayList<>();

            final OrderJpaEntity entity = new OrderJpaEntity(
                id, createdAt, updatedAt, orderCode, customer, status, deliveryAddress,
                freightCost, subtotal, total, purchasedAt, dispatchedAt, deliveredAt, items, payments
            );

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(orderCode, entity.getOrderCode());
            assertEquals(customer, entity.getCustomer());
            assertEquals(status, entity.getStatus());
            assertEquals(deliveryAddress, entity.getDeliveryAddress());
            assertEquals(freightCost, entity.getFreightCost());
            assertEquals(subtotal, entity.getSubtotal());
            assertEquals(total, entity.getTotal());
            assertEquals(purchasedAt, entity.getPurchasedAt());
            assertEquals(dispatchedAt, entity.getDispatchedAt());
            assertEquals(deliveredAt, entity.getDeliveredAt());
            assertEquals(items, entity.getItems());
            assertEquals(payments, entity.getPayments());
        }
    }

    @Nested
    @DisplayName("Setters")
    class SetterTests {
        @Test
        @DisplayName("should set all fields")
        void shouldSetAllFields() {
            final OrderJpaEntity entity = new OrderJpaEntity();

            final UUID id = UUID.randomUUID();
            final Instant createdAt = Instant.now();
            final Instant updatedAt = Instant.now();
            final String orderCode = "ORD-2024-003";
            final CustomerJpaEntity customer = CustomerJpaEntity.builder().build();
            final OrderStatus status = OrderStatus.IN_TRANSIT;
            final AddressJpaEntity deliveryAddress = AddressJpaEntity.builder().build();
            final BigDecimal freightCost = new BigDecimal("10.00");
            final BigDecimal subtotal = new BigDecimal("50.00");
            final BigDecimal total = new BigDecimal("60.00");
            final Instant purchasedAt = Instant.now();
            final Instant dispatchedAt = Instant.now();
            final Instant deliveredAt = Instant.now();
            final List<OrderItemJpaEntity> items = new ArrayList<>();
            final List<OrderPaymentJpaEntity> payments = new ArrayList<>();

            entity.setId(id);
            entity.setCreatedAt(createdAt);
            entity.setUpdatedAt(updatedAt);
            entity.setOrderCode(orderCode);
            entity.setCustomer(customer);
            entity.setStatus(status);
            entity.setDeliveryAddress(deliveryAddress);
            entity.setFreightCost(freightCost);
            entity.setSubtotal(subtotal);
            entity.setTotal(total);
            entity.setPurchasedAt(purchasedAt);
            entity.setDispatchedAt(dispatchedAt);
            entity.setDeliveredAt(deliveredAt);
            entity.setItems(items);
            entity.setPayments(payments);

            assertEquals(id, entity.getId());
            assertEquals(createdAt, entity.getCreatedAt());
            assertEquals(updatedAt, entity.getUpdatedAt());
            assertEquals(orderCode, entity.getOrderCode());
            assertEquals(customer, entity.getCustomer());
            assertEquals(status, entity.getStatus());
            assertEquals(deliveryAddress, entity.getDeliveryAddress());
            assertEquals(freightCost, entity.getFreightCost());
            assertEquals(subtotal, entity.getSubtotal());
            assertEquals(total, entity.getTotal());
            assertEquals(purchasedAt, entity.getPurchasedAt());
            assertEquals(dispatchedAt, entity.getDispatchedAt());
            assertEquals(deliveredAt, entity.getDeliveredAt());
            assertEquals(items, entity.getItems());
            assertEquals(payments, entity.getPayments());
        }
    }
}
