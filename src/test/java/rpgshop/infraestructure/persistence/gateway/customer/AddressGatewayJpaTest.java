package rpgshop.infraestructure.persistence.gateway.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.domain.entity.customer.constant.ResidenceType;
import rpgshop.domain.entity.customer.constant.StreetType;
import rpgshop.infraestructure.persistence.entity.customer.AddressJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;
import rpgshop.infraestructure.persistence.repository.customer.AddressRepository;
import rpgshop.infraestructure.persistence.repository.customer.CustomerRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressGatewayJpaTest {
    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AddressGatewayJpa addressGatewayJpa;

    @Test
    void shouldSaveAddress() {
        final UUID addressId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final Instant now = Instant.now();

        final Address address = Address.builder()
            .id(addressId)
            .purpose(AddressPurpose.DELIVERY)
            .label("Home")
            .residenceType(ResidenceType.HOUSE)
            .streetType(StreetType.STREET)
            .street("Main Street")
            .number("123")
            .neighborhood("Downtown")
            .zipCode("12345678")
            .city("City")
            .state("State")
            .country("Brazil")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final CustomerJpaEntity customerEntity = CustomerJpaEntity.builder().id(customerId).build();

        final AddressJpaEntity savedEntity = AddressJpaEntity.builder()
            .id(addressId)
            .purpose(AddressPurpose.DELIVERY)
            .label("Home")
            .residenceType(ResidenceType.HOUSE)
            .streetType(StreetType.STREET)
            .street("Main Street")
            .number("123")
            .neighborhood("Downtown")
            .zipCode("12345678")
            .city("City")
            .state("State")
            .country("Brazil")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
        when(addressRepository.save(any(AddressJpaEntity.class))).thenReturn(savedEntity);

        final Address result = addressGatewayJpa.save(address, customerId);

        assertNotNull(result);
        assertEquals(addressId, result.id());
        verify(customerRepository, times(1)).findById(customerId);
        verify(addressRepository, times(1)).save(any(AddressJpaEntity.class));
    }

    @Test
    void shouldSaveDetachedAddress() {
        final UUID addressId = UUID.randomUUID();
        final Instant now = Instant.now();

        final Address address = Address.builder()
            .id(addressId)
            .purpose(AddressPurpose.DELIVERY)
            .label("Home")
            .residenceType(ResidenceType.HOUSE)
            .streetType(StreetType.STREET)
            .street("Main Street")
            .number("123")
            .neighborhood("Downtown")
            .zipCode("12345678")
            .city("City")
            .state("State")
            .country("Brazil")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final AddressJpaEntity savedEntity = AddressJpaEntity.builder()
            .id(addressId)
            .purpose(AddressPurpose.DELIVERY)
            .label("Home")
            .residenceType(ResidenceType.HOUSE)
            .streetType(StreetType.STREET)
            .street("Main Street")
            .number("123")
            .neighborhood("Downtown")
            .zipCode("12345678")
            .city("City")
            .state("State")
            .country("Brazil")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(addressRepository.save(argThat(entity -> entity.getCustomer() == null))).thenReturn(savedEntity);

        final Address result = addressGatewayJpa.saveDetached(address);

        assertNotNull(result);
        assertEquals(addressId, result.id());
        verify(addressRepository, times(1)).save(argThat(entity -> entity.getCustomer() == null));
    }

    @Test
    void shouldFindById() {
        final UUID addressId = UUID.randomUUID();
        final Instant now = Instant.now();

        final AddressJpaEntity entity = AddressJpaEntity.builder()
            .id(addressId)
            .purpose(AddressPurpose.DELIVERY)
            .label("Home")
            .residenceType(ResidenceType.HOUSE)
            .streetType(StreetType.STREET)
            .street("Main Street")
            .number("123")
            .neighborhood("Downtown")
            .zipCode("12345678")
            .city("City")
            .state("State")
            .country("Brazil")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(entity));

        final Optional<Address> result = addressGatewayJpa.findById(addressId);

        assertTrue(result.isPresent());
        assertEquals(addressId, result.get().id());
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void shouldReturnEmptyWhenAddressNotFoundById() {
        final UUID addressId = UUID.randomUUID();

        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        assertTrue(addressGatewayJpa.findById(addressId).isEmpty());
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void shouldFindActiveByCustomerId() {
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final Instant now = Instant.now();

        final AddressJpaEntity entity = AddressJpaEntity.builder()
            .id(addressId)
            .purpose(AddressPurpose.DELIVERY)
            .label("Home")
            .residenceType(ResidenceType.HOUSE)
            .streetType(StreetType.STREET)
            .street("Main Street")
            .number("123")
            .neighborhood("Downtown")
            .zipCode("12345678")
            .city("City")
            .state("State")
            .country("Brazil")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(addressRepository.findByCustomerIdAndIsActiveTrue(customerId)).thenReturn(List.of(entity));

        final List<Address> result = addressGatewayJpa.findActiveByCustomerId(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(addressRepository, times(1)).findByCustomerIdAndIsActiveTrue(customerId);
    }

    @Test
    void shouldFindByCustomerIdAndPurpose() {
        final UUID customerId = UUID.randomUUID();
        final UUID addressId = UUID.randomUUID();
        final AddressPurpose purpose = AddressPurpose.DELIVERY;
        final Instant now = Instant.now();

        final AddressJpaEntity entity = AddressJpaEntity.builder()
            .id(addressId)
            .purpose(purpose)
            .label("Home")
            .residenceType(ResidenceType.HOUSE)
            .streetType(StreetType.STREET)
            .street("Main Street")
            .number("123")
            .neighborhood("Downtown")
            .zipCode("12345678")
            .city("City")
            .state("State")
            .country("Brazil")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(addressRepository.findByCustomerIdAndPurposeAndIsActiveTrue(customerId, purpose))
            .thenReturn(List.of(entity));

        final List<Address> result = addressGatewayJpa.findByCustomerIdAndPurpose(customerId, purpose);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(purpose, result.getFirst().purpose());
        verify(addressRepository, times(1)).findByCustomerIdAndPurposeAndIsActiveTrue(customerId, purpose);
    }

    @Test
    void shouldReturnTrueWhenExistsByCustomerIdAndPurpose() {
        final UUID customerId = UUID.randomUUID();
        final AddressPurpose purpose = AddressPurpose.DELIVERY;

        when(addressRepository.existsByCustomerIdAndPurposeAndIsActiveTrue(customerId, purpose)).thenReturn(true);

        assertTrue(addressGatewayJpa.existsByCustomerIdAndPurpose(customerId, purpose));
        verify(addressRepository, times(1)).existsByCustomerIdAndPurposeAndIsActiveTrue(customerId, purpose);
    }

    @Test
    void shouldReturnFalseWhenNotExistsByCustomerIdAndPurpose() {
        final UUID customerId = UUID.randomUUID();
        final AddressPurpose purpose = AddressPurpose.DELIVERY;

        when(addressRepository.existsByCustomerIdAndPurposeAndIsActiveTrue(customerId, purpose)).thenReturn(false);

        assertFalse(addressGatewayJpa.existsByCustomerIdAndPurpose(customerId, purpose));
        verify(addressRepository, times(1)).existsByCustomerIdAndPurposeAndIsActiveTrue(customerId, purpose);
    }

    @Test
    void shouldReturnTrueWhenExistsByIdAndCustomerId() {
        final UUID addressId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();

        when(addressRepository.existsByIdAndCustomerId(addressId, customerId)).thenReturn(true);

        assertTrue(addressGatewayJpa.existsByIdAndCustomerId(addressId, customerId));
        verify(addressRepository, times(1)).existsByIdAndCustomerId(addressId, customerId);
    }

    @Test
    void shouldReturnTrueWhenExistsByIdAndCustomerIdOrWithoutCustomer() {
        final UUID addressId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();

        when(addressRepository.existsByIdAndCustomerIdOrWithoutCustomer(addressId, customerId)).thenReturn(true);

        assertTrue(addressGatewayJpa.existsByIdAndCustomerIdOrWithoutCustomer(addressId, customerId));
        verify(addressRepository, times(1)).existsByIdAndCustomerIdOrWithoutCustomer(addressId, customerId);
    }
}
