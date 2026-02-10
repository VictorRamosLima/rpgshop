package rpgshop.infraestructure.persistence.gateway;

import org.springframework.stereotype.Component;
import rpgshop.application.gateway.customer.PhoneGateway;
import rpgshop.domain.entity.customer.Phone;
import rpgshop.infraestructure.mapper.customer.PhoneMapper;
import rpgshop.infraestructure.persistence.entity.customer.PhoneJpaEntity;
import rpgshop.infraestructure.persistence.repository.customer.CustomerRepository;
import rpgshop.infraestructure.persistence.repository.customer.PhoneRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PhoneGatewayJpa implements PhoneGateway {
    private final PhoneRepository phoneRepository;
    private final CustomerRepository customerRepository;

    public PhoneGatewayJpa(final PhoneRepository phoneRepository, final CustomerRepository customerRepository) {
        this.phoneRepository = phoneRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Phone save(final Phone phone, final UUID customerId) {
        final PhoneJpaEntity entity = PhoneMapper.toEntity(phone);
        customerRepository.findById(customerId).ifPresent(entity::setCustomer);
        final PhoneJpaEntity saved = phoneRepository.save(entity);
        return PhoneMapper.toDomain(saved);
    }

    @Override
    public Optional<Phone> findById(final UUID id) {
        return phoneRepository.findById(id).map(PhoneMapper::toDomain);
    }

    @Override
    public List<Phone> findActiveByCustomerId(final UUID customerId) {
        return phoneRepository.findByCustomerIdAndIsActiveTrue(customerId)
            .stream()
            .map(PhoneMapper::toDomain)
            .toList();
    }

    @Override
    public boolean existsByCustomerIdAndAreaCodeAndNumber(final UUID customerId, final String areaCode, final String number) {
        return phoneRepository.existsByCustomerIdAndAreaCodeAndNumber(customerId, areaCode, number);
    }
}
