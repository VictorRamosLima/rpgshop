package rpgshop.infraestructure.persistence.gateway.exchange;

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
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;
import rpgshop.domain.entity.order.constant.OrderStatus;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.ProductType;
import rpgshop.infraestructure.persistence.entity.customer.AddressJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;
import rpgshop.infraestructure.persistence.entity.exchange.ExchangeRequestJpaEntity;
import rpgshop.infraestructure.persistence.entity.order.OrderItemJpaEntity;
import rpgshop.infraestructure.persistence.entity.order.OrderJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.PricingGroupJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductJpaEntity;
import rpgshop.infraestructure.persistence.entity.product.ProductTypeJpaEntity;
import rpgshop.infraestructure.persistence.repository.exchange.ExchangeRequestRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRequestGatewayJpaTest {
    @Mock
    private ExchangeRequestRepository exchangeRequestRepository;

    @InjectMocks
    private ExchangeRequestGatewayJpa exchangeRequestGatewayJpa;

    // Domain helper methods
    private Customer createCustomerDomain(UUID customerId, Instant now) {
        return Customer.builder()
            .id(customerId)
            .gender(Gender.MALE)
            .name("Test Customer")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .cpf("12345678901")
            .email("test@example.com")
            .password("password123")
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

    private Order createOrderDomain(UUID orderId, Customer customer, Address address, Instant now) {
        return Order.builder()
            .id(orderId)
            .customer(customer)
            .deliveryAddress(address)
            .status(OrderStatus.PROCESSING)
            .freightCost(new BigDecimal("10.00"))
            .subtotal(new BigDecimal("90.00"))
            .total(new BigDecimal("100.00"))
            .items(Collections.emptyList())
            .payments(Collections.emptyList())
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private ProductType createProductTypeDomain(UUID typeId, Instant now) {
        return ProductType.builder()
            .id(typeId)
            .name("Test Product Type")
            .description("Test Description")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private PricingGroup createPricingGroupDomain(UUID pricingGroupId, Instant now) {
        return PricingGroup.builder()
            .id(pricingGroupId)
            .name("Test Pricing Group")
            .marginPercentage(new BigDecimal("10.00"))
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private Product createProductDomain(UUID productId, ProductType productType, PricingGroup pricingGroup, Instant now) {
        return Product.builder()
            .id(productId)
            .name("Test Product")
            .productType(productType)
            .pricingGroup(pricingGroup)
            .barcode("1234567890123")
            .sku("SKU001")
            .salePrice(new BigDecimal("50.00"))
            .costPrice(new BigDecimal("30.00"))
            .stockQuantity(100)
            .minimumSaleThreshold(new BigDecimal("10"))
            .categories(Collections.emptyList())
            .statusChanges(Collections.emptyList())
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private OrderItem createOrderItemDomain(UUID orderItemId, Product product, Instant now) {
        return OrderItem.builder()
            .id(orderItemId)
            .product(product)
            .quantity(2)
            .unitPrice(new BigDecimal("50.00"))
            .totalPrice(new BigDecimal("100.00"))
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    // JPA Entity helper methods

    private CustomerJpaEntity createCustomerJpaEntity(UUID customerId, Instant now) {
        return CustomerJpaEntity.builder()
            .id(customerId)
            .gender(Gender.MALE)
            .name("Test Customer")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .cpf("12345678901")
            .email("test@example.com")
            .password("password123")
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

    private OrderJpaEntity createOrderJpaEntity(UUID orderId, CustomerJpaEntity customer, AddressJpaEntity address, Instant now) {
        return OrderJpaEntity.builder()
            .id(orderId)
            .orderCode("ORD001")
            .customer(customer)
            .deliveryAddress(address)
            .status(OrderStatus.PROCESSING)
            .freightCost(new BigDecimal("10.00"))
            .subtotal(new BigDecimal("90.00"))
            .total(new BigDecimal("100.00"))
            .items(Collections.emptyList())
            .payments(Collections.emptyList())
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private ProductTypeJpaEntity createProductTypeJpaEntity(UUID typeId, Instant now) {
        return ProductTypeJpaEntity.builder()
            .id(typeId)
            .name("Test Product Type")
            .description("Test Description")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private PricingGroupJpaEntity createPricingGroupJpaEntity(UUID pricingGroupId, Instant now) {
        return PricingGroupJpaEntity.builder()
            .id(pricingGroupId)
            .name("Test Pricing Group")
            .marginPercentage(new BigDecimal("10.00"))
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private ProductJpaEntity createProductJpaEntity(UUID productId, ProductTypeJpaEntity productType, PricingGroupJpaEntity pricingGroup, Instant now) {
        return ProductJpaEntity.builder()
            .id(productId)
            .name("Test Product")
            .productType(productType)
            .pricingGroup(pricingGroup)
            .barcode("1234567890123")
            .sku("SKU001")
            .salePrice(new BigDecimal("50.00"))
            .costPrice(new BigDecimal("30.00"))
            .stockQuantity(100)
            .minimumSaleThreshold(new BigDecimal("10"))
            .isActive(true)
            .categories(Collections.emptyList())
            .statusChanges(Collections.emptyList())
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    private OrderItemJpaEntity createOrderItemJpaEntity(UUID orderItemId, ProductJpaEntity product, Instant now) {
        return OrderItemJpaEntity.builder()
            .id(orderItemId)
            .product(product)
            .quantity(2)
            .unitPrice(new BigDecimal("50.00"))
            .totalPrice(new BigDecimal("100.00"))
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    @Test
    void shouldSaveExchangeRequest() {
        final UUID exchangeId = UUID.randomUUID();
        final UUID orderId = UUID.randomUUID();
        final UUID orderItemId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final UUID productTypeId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final Instant now = Instant.now();

        final Customer customer = createCustomerDomain(customerId, now);
        final Address address = createAddressDomain(addressId, now);
        final Order order = createOrderDomain(orderId, customer, address, now);
        final ProductType productType = createProductTypeDomain(productTypeId, now);
        final PricingGroup pricingGroup = createPricingGroupDomain(pricingGroupId, now);
        final Product product = createProductDomain(productId, productType, pricingGroup, now);
        final OrderItem orderItem = createOrderItemDomain(orderItemId, product, now);

        final ExchangeRequest exchangeRequest = ExchangeRequest.builder()
            .id(exchangeId)
            .order(order)
            .orderItem(orderItem)
            .quantity(2)
            .status(ExchangeStatus.REQUESTED)
            .reason("Defective product")
            .returnToStock(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        // Create JPA entities for the mock return
        final CustomerJpaEntity customerEntity = createCustomerJpaEntity(customerId, now);
        final AddressJpaEntity addressEntity = createAddressJpaEntity(addressId, now);
        final OrderJpaEntity orderEntity = createOrderJpaEntity(orderId, customerEntity, addressEntity, now);
        final ProductTypeJpaEntity productTypeEntity = createProductTypeJpaEntity(productTypeId, now);
        final PricingGroupJpaEntity pricingGroupEntity = createPricingGroupJpaEntity(pricingGroupId, now);
        final ProductJpaEntity productEntity = createProductJpaEntity(productId, productTypeEntity, pricingGroupEntity, now);
        final OrderItemJpaEntity orderItemEntity = createOrderItemJpaEntity(orderItemId, productEntity, now);

        final ExchangeRequestJpaEntity savedEntity = ExchangeRequestJpaEntity.builder()
            .id(exchangeId)
            .order(orderEntity)
            .orderItem(orderItemEntity)
            .quantity(2)
            .status(ExchangeStatus.REQUESTED)
            .reason("Defective product")
            .returnToStock(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(exchangeRequestRepository.save(any(ExchangeRequestJpaEntity.class))).thenReturn(savedEntity);

        final ExchangeRequest result = exchangeRequestGatewayJpa.save(exchangeRequest);

        assertNotNull(result);
        assertEquals(exchangeId, result.id());
        assertEquals(ExchangeStatus.REQUESTED, result.status());
        verify(exchangeRequestRepository, times(1)).save(any(ExchangeRequestJpaEntity.class));
    }

    @Test
    void shouldFindById() {
        final UUID exchangeId = UUID.randomUUID();
        final UUID orderId = UUID.randomUUID();
        final UUID orderItemId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final UUID productTypeId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final Instant now = Instant.now();

        final CustomerJpaEntity customerEntity = createCustomerJpaEntity(customerId, now);
        final AddressJpaEntity addressEntity = createAddressJpaEntity(addressId, now);
        final OrderJpaEntity orderEntity = createOrderJpaEntity(orderId, customerEntity, addressEntity, now);
        final ProductTypeJpaEntity productTypeEntity = createProductTypeJpaEntity(productTypeId, now);
        final PricingGroupJpaEntity pricingGroupEntity = createPricingGroupJpaEntity(pricingGroupId, now);
        final ProductJpaEntity productEntity = createProductJpaEntity(productId, productTypeEntity, pricingGroupEntity, now);
        final OrderItemJpaEntity orderItemEntity = createOrderItemJpaEntity(orderItemId, productEntity, now);

        final ExchangeRequestJpaEntity entity = ExchangeRequestJpaEntity.builder()
            .id(exchangeId)
            .order(orderEntity)
            .orderItem(orderItemEntity)
            .quantity(2)
            .status(ExchangeStatus.REQUESTED)
            .reason("Defective product")
            .returnToStock(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(exchangeRequestRepository.findById(exchangeId)).thenReturn(Optional.of(entity));

        final Optional<ExchangeRequest> result = exchangeRequestGatewayJpa.findById(exchangeId);

        assertTrue(result.isPresent());
        assertEquals(exchangeId, result.get().id());
        verify(exchangeRequestRepository, times(1)).findById(exchangeId);
    }

    @Test
    void shouldReturnEmptyWhenExchangeRequestNotFoundById() {
        final UUID exchangeId = UUID.randomUUID();

        when(exchangeRequestRepository.findById(exchangeId)).thenReturn(Optional.empty());

        assertTrue(exchangeRequestGatewayJpa.findById(exchangeId).isEmpty());
        verify(exchangeRequestRepository, times(1)).findById(exchangeId);
    }

    @Test
    void shouldFindAll() {
        final UUID exchangeId = UUID.randomUUID();
        final UUID orderId = UUID.randomUUID();
        final UUID orderItemId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final UUID productTypeId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);

        final CustomerJpaEntity customerEntity = createCustomerJpaEntity(customerId, now);
        final AddressJpaEntity addressEntity = createAddressJpaEntity(addressId, now);
        final OrderJpaEntity orderEntity = createOrderJpaEntity(orderId, customerEntity, addressEntity, now);
        final ProductTypeJpaEntity productTypeEntity = createProductTypeJpaEntity(productTypeId, now);
        final PricingGroupJpaEntity pricingGroupEntity = createPricingGroupJpaEntity(pricingGroupId, now);
        final ProductJpaEntity productEntity = createProductJpaEntity(productId, productTypeEntity, pricingGroupEntity, now);
        final OrderItemJpaEntity orderItemEntity = createOrderItemJpaEntity(orderItemId, productEntity, now);

        final ExchangeRequestJpaEntity entity = ExchangeRequestJpaEntity.builder()
            .id(exchangeId)
            .order(orderEntity)
            .orderItem(orderItemEntity)
            .quantity(2)
            .status(ExchangeStatus.REQUESTED)
            .reason("Defective product")
            .returnToStock(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final Page<ExchangeRequestJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(exchangeRequestRepository.findAll(pageable)).thenReturn(page);

        final Page<ExchangeRequest> result = exchangeRequestGatewayJpa.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(exchangeRequestRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldFindByStatus() {
        final UUID exchangeId = UUID.randomUUID();
        final UUID orderId = UUID.randomUUID();
        final UUID orderItemId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final UUID productTypeId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);
        final ExchangeStatus status = ExchangeStatus.AUTHORIZED;

        final CustomerJpaEntity customerEntity = createCustomerJpaEntity(customerId, now);
        final AddressJpaEntity addressEntity = createAddressJpaEntity(addressId, now);
        final OrderJpaEntity orderEntity = createOrderJpaEntity(orderId, customerEntity, addressEntity, now);
        final ProductTypeJpaEntity productTypeEntity = createProductTypeJpaEntity(productTypeId, now);
        final PricingGroupJpaEntity pricingGroupEntity = createPricingGroupJpaEntity(pricingGroupId, now);
        final ProductJpaEntity productEntity = createProductJpaEntity(productId, productTypeEntity, pricingGroupEntity, now);
        final OrderItemJpaEntity orderItemEntity = createOrderItemJpaEntity(orderItemId, productEntity, now);

        final ExchangeRequestJpaEntity entity = ExchangeRequestJpaEntity.builder()
            .id(exchangeId)
            .order(orderEntity)
            .orderItem(orderItemEntity)
            .quantity(2)
            .status(status)
            .reason("Defective product")
            .returnToStock(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final Page<ExchangeRequestJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(exchangeRequestRepository.findByStatus(status, pageable)).thenReturn(page);

        final Page<ExchangeRequest> result = exchangeRequestGatewayJpa.findByStatus(status, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(status, result.getContent().getFirst().status());
        verify(exchangeRequestRepository, times(1)).findByStatus(status, pageable);
    }

    @Test
    void shouldFindByOrderId() {
        final UUID exchangeId = UUID.randomUUID();
        final UUID orderId = UUID.randomUUID();
        final UUID orderItemId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final UUID productTypeId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);

        final CustomerJpaEntity customerEntity = createCustomerJpaEntity(customerId, now);
        final AddressJpaEntity addressEntity = createAddressJpaEntity(addressId, now);
        final OrderJpaEntity orderEntity = createOrderJpaEntity(orderId, customerEntity, addressEntity, now);
        final ProductTypeJpaEntity productTypeEntity = createProductTypeJpaEntity(productTypeId, now);
        final PricingGroupJpaEntity pricingGroupEntity = createPricingGroupJpaEntity(pricingGroupId, now);
        final ProductJpaEntity productEntity = createProductJpaEntity(productId, productTypeEntity, pricingGroupEntity, now);
        final OrderItemJpaEntity orderItemEntity = createOrderItemJpaEntity(orderItemId, productEntity, now);

        final ExchangeRequestJpaEntity entity = ExchangeRequestJpaEntity.builder()
            .id(exchangeId)
            .order(orderEntity)
            .orderItem(orderItemEntity)
            .quantity(2)
            .status(ExchangeStatus.REQUESTED)
            .reason("Defective product")
            .returnToStock(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final Page<ExchangeRequestJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(exchangeRequestRepository.findByOrderId(orderId, pageable)).thenReturn(page);

        final Page<ExchangeRequest> result = exchangeRequestGatewayJpa.findByOrderId(orderId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(exchangeRequestRepository, times(1)).findByOrderId(orderId, pageable);
    }

    @Test
    void shouldReturnTrueWhenExistsByOrderItemIdAndStatusNot() {
        final UUID orderItemId = UUID.randomUUID();
        final ExchangeStatus status = ExchangeStatus.DENIED;

        when(exchangeRequestRepository.existsByOrderItemIdAndStatusNot(orderItemId, status)).thenReturn(true);

        assertTrue(exchangeRequestGatewayJpa.existsByOrderItemIdAndStatusNot(orderItemId, status));
        verify(exchangeRequestRepository, times(1)).existsByOrderItemIdAndStatusNot(orderItemId, status);
    }

    @Test
    void shouldReturnFalseWhenNotExistsByOrderItemIdAndStatusNot() {
        final UUID orderItemId = UUID.randomUUID();
        final ExchangeStatus status = ExchangeStatus.DENIED;

        when(exchangeRequestRepository.existsByOrderItemIdAndStatusNot(orderItemId, status)).thenReturn(false);

        assertFalse(exchangeRequestGatewayJpa.existsByOrderItemIdAndStatusNot(orderItemId, status));
        verify(exchangeRequestRepository, times(1)).existsByOrderItemIdAndStatusNot(orderItemId, status);
    }

    @Test
    void shouldFindByCustomerId() {
        final UUID exchangeId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID orderId = UUID.randomUUID();
        final UUID orderItemId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final UUID productTypeId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final Instant now = Instant.now();
        final Pageable pageable = PageRequest.of(0, 10);

        final CustomerJpaEntity customerEntity = createCustomerJpaEntity(customerId, now);
        final AddressJpaEntity addressEntity = createAddressJpaEntity(addressId, now);
        final OrderJpaEntity orderEntity = createOrderJpaEntity(orderId, customerEntity, addressEntity, now);
        final ProductTypeJpaEntity productTypeEntity = createProductTypeJpaEntity(productTypeId, now);
        final PricingGroupJpaEntity pricingGroupEntity = createPricingGroupJpaEntity(pricingGroupId, now);
        final ProductJpaEntity productEntity = createProductJpaEntity(productId, productTypeEntity, pricingGroupEntity, now);
        final OrderItemJpaEntity orderItemEntity = createOrderItemJpaEntity(orderItemId, productEntity, now);

        final ExchangeRequestJpaEntity entity = ExchangeRequestJpaEntity.builder()
            .id(exchangeId)
            .order(orderEntity)
            .orderItem(orderItemEntity)
            .quantity(2)
            .status(ExchangeStatus.REQUESTED)
            .reason("Defective product")
            .returnToStock(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final Page<ExchangeRequestJpaEntity> page = new PageImpl<>(List.of(entity), pageable, 1);

        when(exchangeRequestRepository.findByOrderCustomerId(customerId, pageable)).thenReturn(page);

        final Page<ExchangeRequest> result = exchangeRequestGatewayJpa.findByCustomerId(customerId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(exchangeRequestRepository, times(1)).findByOrderCustomerId(customerId, pageable);
    }
}
