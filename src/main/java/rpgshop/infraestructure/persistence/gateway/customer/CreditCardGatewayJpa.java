package rpgshop.infraestructure.persistence.gateway.customer;

import org.springframework.stereotype.Component;
import rpgshop.application.gateway.customer.CreditCardGateway;
import rpgshop.domain.entity.customer.CreditCard;
import rpgshop.infraestructure.persistence.mapper.customer.CreditCardMapper;
import rpgshop.infraestructure.persistence.entity.customer.CreditCardJpaEntity;
import rpgshop.infraestructure.persistence.repository.customer.CreditCardRepository;
import rpgshop.infraestructure.persistence.repository.customer.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CreditCardGatewayJpa implements CreditCardGateway {
    private final CreditCardRepository creditCardRepository;
    private final CustomerRepository customerRepository;

    public CreditCardGatewayJpa(final CreditCardRepository creditCardRepository, final CustomerRepository customerRepository) {
        this.creditCardRepository = creditCardRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public CreditCard save(final CreditCard creditCard, final UUID customerId) {
        final CreditCardJpaEntity entity = CreditCardMapper.toEntity(creditCard);
        customerRepository.findById(customerId).ifPresent(entity::setCustomer);
        final CreditCardJpaEntity saved = creditCardRepository.save(entity);
        return CreditCardMapper.toDomain(saved);
    }

    @Override
    public Optional<CreditCard> findById(final UUID id) {
        return creditCardRepository.findById(id).map(CreditCardMapper::toDomain);
    }

    @Override
    public List<CreditCard> findActiveByCustomerId(final UUID customerId) {
        return creditCardRepository.findByCustomerIdAndIsActiveTrue(customerId)
            .stream().map(CreditCardMapper::toDomain).toList();
    }

    @Override
    public Optional<CreditCard> findPreferredByCustomerId(final UUID customerId) {
        return creditCardRepository.findByCustomerIdAndIsPreferredTrue(customerId)
            .map(CreditCardMapper::toDomain);
    }

    @Override
    public void clearPreferredByCustomerId(final UUID customerId) {
        creditCardRepository.clearPreferredByCustomerId(customerId);
    }

    @Override
    public boolean existsByCustomerIdAndCardNumber(final UUID customerId, final String cardNumber) {
        return creditCardRepository.existsByCustomerIdAndCardNumber(customerId, cardNumber);
    }

    @Override
    public boolean existsByIdAndCustomerId(final UUID creditCardId, final UUID customerId) {
        return creditCardRepository.existsByIdAndCustomerIdAndIsActiveTrue(creditCardId, customerId);
    }
}
