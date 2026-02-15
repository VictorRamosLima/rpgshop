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
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.Customer;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.Gender;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.constant.OrderStatus;
import rpgshop.infraestructure.persistence.entity.customer.AddressJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;
import rpgshop.infraestructure.persistence.entity.order.OrderJpaEntity;
import rpgshop.infraestructure.persistence.repository.order.OrderRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
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
class OrderGatewayJpaTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderGatewayJpa orderGatewayJpa;

    private CustomerJpaEntity createCustomerJpaEntity(UUID customerId, Instant now) {
        return CustomerJpaEntity.builder()
            .id(customerId)
            .gender(Gender.MALE)
            .name("Test Customer")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .cpf("12345678901")
            .email("test@example.com")

            .ranking(BigDecimal.ZERO)
            .customerCode("CUST001")
            .isActive(true)
            .phones(Collections.emptyList())
            .addresses(Collections.emptyList())
            .creditCards(Collections.emptyList())
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private AddressJpaEntity createAddressJpaEntity(UUID addressId, Instant now) {
        return AddressJpaEntity.builder()
            .id(addressId)
            .purpose(AddressPurpose.DELIVERY)
            .label("Home")
            .residenceType(ResidenceType.HOUSE)
            .streetType(StreetType.STREET)
            .street("Test Street")
            .number("123")
            .neighborhood("Test Neighborhood")
            .zipCode("12345-678")
            .city("Test City")
            .state("TS")
            .country("Brazil")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private Customer createCustomerDomain(UUID customerId, Instant now) {
        return Customer.builder()
            .id(customerId)
            .gender(Gender.MALE)
            .name("Test Customer")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .cpf("12345678901")
            .email("test@example.com")

            .ranking(BigDecimal.ZERO)
            .customerCode("CUST001")
            .isActive(true)
            .phones(Collections.emptyList())
            .addresses(Collections.emptyList())
            .creditCards(Collections.emptyList())
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private Address createAddressDomain(UUID addressId, Instant now) {
        return Address.builder()
            .id(addressId)
            .purpose(AddressPurpose.DELIVERY)
            .label("Home")
            .residenceType(ResidenceType.HOUSE)
            .streetType(StreetType.STREET)
            .street("Test Street")
            .number("123")
            .neighborhood("Test Neighborhood")
            .zipCode("12345-678")
            .city("Test City")
            .state("TS")
            .country("Brazil")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    @Test
    void shouldSaveOrder() {
        final UUID orderId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final Instant now = Instant.now();

        final Customer customer = createCustomerDomain(customerId, now);
        final Address address = createAddressDomain(addressId, now);

        final Order order = Order.builder()
            .id(orderId)
            .customer(customer)
            .deliveryAddress(address)
            .status(OrderStatus.PROCESSING)
            .freightCost(new BigDecimal("10.00"))
            .subtotal(new BigDecimal("90.00"))
            .total(new BigDecimal("100.00"))
            .createdAt(now)
            .updatedAt(now)
            .build();

        final CustomerJpaEntity customerEntity = createCustomerJpaEntity(customerId, now);
        final AddressJpaEntity addressEntity = createAddressJpaEntity(addressId, now);

        final OrderJpaEntity savedEntity = OrderJpaEntity.builder()
            .id(orderId)
            .customer(customerEntity)
            .deliveryAddress(addressEntity)
            .status(OrderStatus.PROCESSING)
            .freightCost(new BigDecimal("10.00"))
            .subtotal(new BigDecimal("90.00"))
            .total(new BigDecimal("100.00"))
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(orderRepository.save(any(OrderJpaEntity.class))).thenReturn(savedEntity);

        final Order result = orderGatewayJpa.save(order);

        assertNotNull(result);
        assertEquals(orderId, result.id());
        assertEquals(OrderStatus.PROCESSING, result.status());
        verify(orderRepository, times(1)).save(any(OrderJpaEntity.class));
    }

    @Test
    void shouldFindById() {
        final UUID orderId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final Instant now = Instant.now();

        final CustomerJpaEntity customerEntity = createCustomerJpaEntity(customerId, now);
        final AddressJpaEntity addressEntity = createAddressJpaEntity(addressId, now);

        final OrderJpaEntity entity = OrderJpaEntity.builder()
            .id(orderId)
            .customer(customerEntity)
            .deliveryAddress(addressEntity)
            .status(OrderStatus.APPROVED)
            .freightCost(new BigDecimal("10.00"))
            .subtotal(new BigDecimal("90.00"))
            .total(new BigDecimal("100.00"))
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(entity));

        final Optional<Order> result = orderGatewayJpa.findById(orderId);

        assertTrue(result.isPresent());
        assertEquals(orderId, result.get().id());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void shouldReturnEmptyWhenOrderNotFoundById() {
        final UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertTrue(orderGatewayJpa.findById(orderId).isEmpty());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void shouldFindAll() {
        final UUID orderId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);

        final CustomerJpaEntity customerEntity = createCustomerJpaEntity(customerId, now);
        final AddressJpaEntity addressEntity = createAddressJpaEntity(addressId, now);

        final OrderJpaEntity entity = OrderJpaEntity.builder()
            .id(orderId)
            .customer(customerEntity)
            .deliveryAddress(addressEntity)
            .status(OrderStatus.APPROVED)
            .freightCost(new BigDecimal("10.00"))
            .subtotal(new BigDecimal("90.00"))
            .total(new BigDecimal("100.00"))
            .createdAt(now)
            .updatedAt(now)
            .build();

        final Page<OrderJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(orderRepository.findAll(pageable)).thenReturn(page);

        final Page<Order> result = orderGatewayJpa.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldFindByCustomerId() {
        final UUID orderId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);

        final CustomerJpaEntity customerEntity = createCustomerJpaEntity(customerId, now);
        final AddressJpaEntity addressEntity = createAddressJpaEntity(addressId, now);

        final OrderJpaEntity entity = OrderJpaEntity.builder()
            .id(orderId)
            .customer(customerEntity)
            .deliveryAddress(addressEntity)
            .status(OrderStatus.APPROVED)
            .freightCost(new BigDecimal("10.00"))
            .subtotal(new BigDecimal("90.00"))
            .total(new BigDecimal("100.00"))
            .createdAt(now)
            .updatedAt(now)
            .build();

        final Page<OrderJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(orderRepository.findByCustomerId(customerId, pageable)).thenReturn(page);

        final Page<Order> result = orderGatewayJpa.findByCustomerId(customerId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderRepository, times(1)).findByCustomerId(customerId, pageable);
    }

    @Test
    void shouldFindByStatus() {
        final UUID orderId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);
        final OrderStatus status = OrderStatus.IN_TRANSIT;

        final CustomerJpaEntity customerEntity = createCustomerJpaEntity(customerId, now);
        final AddressJpaEntity addressEntity = createAddressJpaEntity(addressId, now);

        final OrderJpaEntity entity = OrderJpaEntity.builder()
            .id(orderId)
            .customer(customerEntity)
            .deliveryAddress(addressEntity)
            .status(status)
            .freightCost(new BigDecimal("10.00"))
            .subtotal(new BigDecimal("90.00"))
            .total(new BigDecimal("100.00"))
            .createdAt(now)
            .updatedAt(now)
            .build();

        final Page<OrderJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(orderRepository.findByStatus(status, pageable)).thenReturn(page);

        final Page<Order> result = orderGatewayJpa.findByStatus(status, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(status, result.getContent().getFirst().status());
        verify(orderRepository, times(1)).findByStatus(status, pageable);
    }

    @Test
    void shouldFindSalesInPeriod() {
        final UUID orderId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Instant startDate = now.minusSeconds(86400);
        final Pageable pageable = PageRequest.of(0, 10);

        final CustomerJpaEntity customerEntity = createCustomerJpaEntity(customerId, now);
        final AddressJpaEntity addressEntity = createAddressJpaEntity(addressId, now);

        final OrderJpaEntity entity = OrderJpaEntity.builder()
            .id(orderId)
            .customer(customerEntity)
            .deliveryAddress(addressEntity)
            .status(OrderStatus.DELIVERED)
            .freightCost(new BigDecimal("10.00"))
            .subtotal(new BigDecimal("90.00"))
            .total(new BigDecimal("100.00"))
            .purchasedAt(now.minusSeconds(3600))
            .createdAt(now)
            .updatedAt(now)
            .build();

        final Page<OrderJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(orderRepository.findSalesInPeriod(startDate, now, pageable)).thenReturn(page);

        final Page<Order> result = orderGatewayJpa.findSalesInPeriod(startDate, now, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderRepository, times(1)).findSalesInPeriod(startDate, now, pageable);
    }

    @Test
    void shouldFindByCustomerIdAndPurchasedAtBetween() {
        final UUID orderId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Instant startDate = now.minusSeconds(86400);
        final Pageable pageable = PageRequest.of(0, 10);

        final CustomerJpaEntity customerEntity = createCustomerJpaEntity(customerId, now);
        final AddressJpaEntity addressEntity = createAddressJpaEntity(addressId, now);

        final OrderJpaEntity entity = OrderJpaEntity.builder()
            .id(orderId)
            .customer(customerEntity)
            .deliveryAddress(addressEntity)
            .status(OrderStatus.DELIVERED)
            .freightCost(new BigDecimal("10.00"))
            .subtotal(new BigDecimal("90.00"))
            .total(new BigDecimal("100.00"))
            .purchasedAt(now.minusSeconds(3600))
            .createdAt(now)
            .updatedAt(now)
            .build();

        final Page<OrderJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(orderRepository.findByCustomerIdAndPurchasedAtBetween(customerId, startDate, now, pageable))
            .thenReturn(page);

        final Page<Order> result = orderGatewayJpa.findByCustomerIdAndPurchasedAtBetween(
            customerId, startDate, now, pageable
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(orderRepository, times(1)).findByCustomerIdAndPurchasedAtBetween(
            customerId, startDate, now, pageable
        );
    }
}

