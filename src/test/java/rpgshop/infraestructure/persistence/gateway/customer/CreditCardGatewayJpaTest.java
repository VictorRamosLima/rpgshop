package rpgshop.infraestructure.persistence.gateway.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.domain.entity.customer.CardBrand;
import rpgshop.domain.entity.customer.CreditCard;
import rpgshop.infraestructure.persistence.entity.customer.CardBrandJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CreditCardJpaEntity;
import rpgshop.infraestructure.persistence.entity.customer.CustomerJpaEntity;
import rpgshop.infraestructure.persistence.repository.customer.CreditCardRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditCardGatewayJpaTest {
    @Mock
    private CreditCardRepository creditCardRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CreditCardGatewayJpa creditCardGatewayJpa;

    @Test
    void shouldSaveCreditCard() {
        final UUID cardId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();
        final UUID brandId = UUID.randomUUID();
        final Instant now = Instant.now();

        final CardBrand cardBrand = CardBrand.builder().id(brandId).name("Visa").isActive(true).createdAt(now).updatedAt(now).build();
        final CreditCard creditCard = CreditCard.builder()
            .id(cardId)
            .cardNumber("1234567890123456")
            .printedName("John Doe")
            .cardBrand(cardBrand)
            .securityCode("123")
            .isPreferred(true)
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        final CustomerJpaEntity customerEntity = CustomerJpaEntity.builder().id(customerId).build();
        final CardBrandJpaEntity brandEntity = CardBrandJpaEntity.builder().id(brandId).name("Visa").isActive(true).createdAt(now).updatedAt(now).build();
        final CreditCardJpaEntity savedEntity = CreditCardJpaEntity.builder()
            .id(cardId)
            .cardNumber("1234567890123456")
            .printedName("John Doe")
            .cardBrand(brandEntity)
            .securityCode("123")
            .isPreferred(true)
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerEntity));
        when(creditCardRepository.save(any(CreditCardJpaEntity.class))).thenReturn(savedEntity);

        final CreditCard result = creditCardGatewayJpa.save(creditCard, customerId);

        assertNotNull(result);
        assertEquals(cardId, result.id());
        verify(customerRepository, times(1)).findById(customerId);
        verify(creditCardRepository, times(1)).save(any(CreditCardJpaEntity.class));
    }

    @Test
    void shouldFindById() {
        final UUID cardId = UUID.randomUUID();
        final UUID brandId = UUID.randomUUID();
        final Instant now = Instant.now();

        final CardBrandJpaEntity brandEntity = CardBrandJpaEntity.builder().id(brandId).name("Visa").isActive(true).createdAt(now).updatedAt(now).build();
        final CreditCardJpaEntity entity = CreditCardJpaEntity.builder()
            .id(cardId)
            .cardNumber("1234567890123456")
            .printedName("John Doe")
            .cardBrand(brandEntity)
            .securityCode("123")
            .isPreferred(true)
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(creditCardRepository.findById(cardId)).thenReturn(Optional.of(entity));

        final Optional<CreditCard> result = creditCardGatewayJpa.findById(cardId);

        assertTrue(result.isPresent());
        assertEquals(cardId, result.get().id());
        verify(creditCardRepository, times(1)).findById(cardId);
    }

    @Test
    void shouldReturnEmptyWhenCreditCardNotFoundById() {
        final UUID cardId = UUID.randomUUID();

        when(creditCardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertTrue(creditCardGatewayJpa.findById(cardId).isEmpty());
        verify(creditCardRepository, times(1)).findById(cardId);
    }

    @Test
    void shouldFindActiveByCustomerId() {
        final UUID customerId = UUID.randomUUID();
        final UUID cardId = UUID.randomUUID();
        final UUID brandId = UUID.randomUUID();
        final Instant now = Instant.now();

        final CardBrandJpaEntity brandEntity = CardBrandJpaEntity.builder().id(brandId).name("Visa").isActive(true).createdAt(now).updatedAt(now).build();
        final CreditCardJpaEntity entity = CreditCardJpaEntity.builder()
            .id(cardId)
            .cardNumber("1234567890123456")
            .printedName("John Doe")
            .cardBrand(brandEntity)
            .securityCode("123")
            .isPreferred(true)
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(creditCardRepository.findByCustomerIdAndIsActiveTrue(customerId)).thenReturn(List.of(entity));

        final List<CreditCard> result = creditCardGatewayJpa.findActiveByCustomerId(customerId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(creditCardRepository, times(1)).findByCustomerIdAndIsActiveTrue(customerId);
    }

    @Test
    void shouldFindPreferredByCustomerId() {
        final UUID customerId = UUID.randomUUID();
        final UUID cardId = UUID.randomUUID();
        final UUID brandId = UUID.randomUUID();
        final Instant now = Instant.now();

        final CardBrandJpaEntity brandEntity = CardBrandJpaEntity.builder().id(brandId).name("Visa").isActive(true).createdAt(now).updatedAt(now).build();
        final CreditCardJpaEntity entity = CreditCardJpaEntity.builder()
            .id(cardId)
            .cardNumber("1234567890123456")
            .printedName("John Doe")
            .cardBrand(brandEntity)
            .securityCode("123")
            .isPreferred(true)
            .isActive(true)
            .createdAt(now)
            .updatedAt(now)
            .build();

        when(creditCardRepository.findByCustomerIdAndIsPreferredTrue(customerId)).thenReturn(Optional.of(entity));

        final Optional<CreditCard> result = creditCardGatewayJpa.findPreferredByCustomerId(customerId);

        assertTrue(result.isPresent());
        assertTrue(result.get().isPreferred());
        verify(creditCardRepository, times(1)).findByCustomerIdAndIsPreferredTrue(customerId);
    }

    @Test
    void shouldClearPreferredByCustomerId() {
        final UUID customerId = UUID.randomUUID();

        when(creditCardRepository.clearPreferredByCustomerId(customerId)).thenReturn(1);

        creditCardGatewayJpa.clearPreferredByCustomerId(customerId);

        verify(creditCardRepository, times(1)).clearPreferredByCustomerId(customerId);
    }

    @Test
    void shouldReturnTrueWhenExistsByCustomerIdAndCardNumber() {
        final UUID customerId = UUID.randomUUID();
        final String cardNumber = "1234567890123456";

        when(creditCardRepository.existsByCustomerIdAndCardNumber(customerId, cardNumber)).thenReturn(true);

        assertTrue(creditCardGatewayJpa.existsByCustomerIdAndCardNumber(customerId, cardNumber));
        verify(creditCardRepository, times(1)).existsByCustomerIdAndCardNumber(customerId, cardNumber);
    }

    @Test
    void shouldReturnFalseWhenNotExistsByCustomerIdAndCardNumber() {
        final UUID customerId = UUID.randomUUID();
        final String cardNumber = "1234567890123456";

        when(creditCardRepository.existsByCustomerIdAndCardNumber(customerId, cardNumber)).thenReturn(false);

        assertFalse(creditCardGatewayJpa.existsByCustomerIdAndCardNumber(customerId, cardNumber));
        verify(creditCardRepository, times(1)).existsByCustomerIdAndCardNumber(customerId, cardNumber);
    }

    @Test
    void shouldReturnTrueWhenExistsByIdAndCustomerId() {
        final UUID cardId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();

        when(creditCardRepository.existsByIdAndCustomerIdAndIsActiveTrue(cardId, customerId)).thenReturn(true);

        assertTrue(creditCardGatewayJpa.existsByIdAndCustomerId(cardId, customerId));
        verify(creditCardRepository, times(1)).existsByIdAndCustomerIdAndIsActiveTrue(cardId, customerId);
    }

    @Test
    void shouldReturnFalseWhenNotExistsByIdAndCustomerId() {
        final UUID cardId = UUID.randomUUID();
        final UUID customerId = UUID.randomUUID();

        when(creditCardRepository.existsByIdAndCustomerIdAndIsActiveTrue(cardId, customerId)).thenReturn(false);

        assertFalse(creditCardGatewayJpa.existsByIdAndCustomerId(cardId, customerId));
        verify(creditCardRepository, times(1)).existsByIdAndCustomerIdAndIsActiveTrue(cardId, customerId);
    }
}
