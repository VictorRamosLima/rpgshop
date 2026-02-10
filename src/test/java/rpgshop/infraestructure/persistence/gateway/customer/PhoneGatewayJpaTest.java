package rpgshop.infraestructure.persistence.gateway.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.domain.entity.customer.Phone;
import rpgshop.domain.entity.customer.constant.PhoneType;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.PhoneJpaEntity;
import rpgshop.infraestructure.persistence.repository.customer.CustomerRepository;
import rpgshop.infraestructure.persistence.repository.customer.PhoneRepository;

import java.time.Instant;
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
class PhoneGatewayJpaTest {
    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private PhoneGatewayJpa phoneGatewayJpa;

    @Test
    void shouldSavePhone() {
        final UUID phoneId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final Instant now = Instant.now();

        final Phone phone = Phone.builder()
            .id(phoneId)
            .type(PhoneType.MOBILE)
            .areaCode("11")
            .number("999999999")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final CustomerJpaEntity customerEntity = CustomerJpaEntity.builder().id(customerId).build();

        final PhoneJpaEntity savedEntity = PhoneJpaEntity.builder()
            .id(phoneId)
            .type(PhoneType.MOBILE)
            .areaCode("11")
            .number("999999999")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
        when(phoneRepository.save(any(PhoneJpaEntity.class))).thenReturn(savedEntity);

        final Phone result = phoneGatewayJpa.save(phone, customerId);

        assertNotNull(result);
        assertEquals(phoneId, result.id());
        assertEquals("11", result.areaCode());
        verify(customerRepository, times(1)).findById(customerId);
        verify(phoneRepository, times(1)).save(any(PhoneJpaEntity.class));
    }

    @Test
    void shouldSavePhoneEvenWhenCustomerNotFound() {
        final UUID phoneId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final Instant now = Instant.now();

        final Phone phone = Phone.builder()
            .id(phoneId)
            .type(PhoneType.MOBILE)
            .areaCode("11")
            .number("999999999")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final PhoneJpaEntity savedEntity = PhoneJpaEntity.builder()
            .id(phoneId)
            .type(PhoneType.MOBILE)
            .areaCode("11")
            .number("999999999")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        when(phoneRepository.save(any(PhoneJpaEntity.class))).thenReturn(savedEntity);

        final Phone result = phoneGatewayJpa.save(phone, customerId);

        assertNotNull(result);
        verify(customerRepository, times(1)).findById(customerId);
        verify(phoneRepository, times(1)).save(any(PhoneJpaEntity.class));
    }

    @Test
    void shouldFindById() {
        final UUID phoneId = UUID.randomUUID();
        final Instant now = Instant.now();

        final PhoneJpaEntity entity = PhoneJpaEntity.builder()
            .id(phoneId)
            .type(PhoneType.MOBILE)
            .areaCode("11")
            .number("999999999")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(phoneRepository.findById(phoneId)).thenReturn(Optional.of(entity));

        final Optional<Phone> result = phoneGatewayJpa.findById(phoneId);

        assertTrue(result.isPresent());
        assertEquals(phoneId, result.get().id());
        verify(phoneRepository, times(1)).findById(phoneId);
    }

    @Test
    void shouldReturnEmptyWhenPhoneNotFoundById() {
        final UUID phoneId = UUID.randomUUID();

        when(phoneRepository.findById(phoneId)).thenReturn(Optional.empty());

        assertTrue(phoneGatewayJpa.findById(phoneId).isEmpty());
        verify(phoneRepository, times(1)).findById(phoneId);
    }

    @Test
    void shouldFindActiveByCustomerId() {
        final UUID customerId = UUID.randomUUID();
        final UUID phoneId = UUID.randomUUID();
        final Instant now = Instant.now();

        final PhoneJpaEntity entity = PhoneJpaEntity.builder()
            .id(phoneId)
            .type(PhoneType.MOBILE)
            .areaCode("11")
            .number("999999999")
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(phoneRepository.findByCustomerIdAndIsActiveTrue(customerId)).thenReturn(List.of(entity));

        final List<Phone> result = phoneGatewayJpa.findActiveByCustomerId(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(phoneId, result.getFirst().id());
        verify(phoneRepository, times(1)).findByCustomerIdAndIsActiveTrue(customerId);
    }

    @Test
    void shouldReturnEmptyListWhenNoActivePhonesForCustomer() {
        final UUID customerId = UUID.randomUUID();

        when(phoneRepository.findByCustomerIdAndIsActiveTrue(customerId)).thenReturn(List.of());

        final List<Phone> result = phoneGatewayJpa.findActiveByCustomerId(customerId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(phoneRepository, times(1)).findByCustomerIdAndIsActiveTrue(customerId);
    }

    @Test
    void shouldReturnTrueWhenExistsByCustomerIdAndAreaCodeAndNumber() {
        final UUID customerId = UUID.randomUUID();
        final String areaCode = "11";
        final String number = "999999999";

        when(phoneRepository.existsByCustomerIdAndAreaCodeAndNumber(customerId, areaCode, number)).thenReturn(true);

        assertTrue(phoneGatewayJpa.existsByCustomerIdAndAreaCodeAndNumber(customerId, areaCode, number));
        verify(phoneRepository, times(1)).existsByCustomerIdAndAreaCodeAndNumber(customerId, areaCode, number);
    }

    @Test
    void shouldReturnFalseWhenNotExistsByCustomerIdAndAreaCodeAndNumber() {
        final UUID customerId = UUID.randomUUID();
        final String areaCode = "11";
        final String number = "999999999";

        when(phoneRepository.existsByCustomerIdAndAreaCodeAndNumber(customerId, areaCode, number)).thenReturn(false);

        assertFalse(phoneGatewayJpa.existsByCustomerIdAndAreaCodeAndNumber(customerId, areaCode, number));
        verify(phoneRepository, times(1)).existsByCustomerIdAndAreaCodeAndNumber(customerId, areaCode, number);
    }
}
