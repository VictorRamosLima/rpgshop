package rpgshop.infraestructure.persistence.gateway.customer;

import org.springframework.stereotype.Component;
import rpgshop.application.gateway.customer.AddressGateway;
import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.constant.AddressPurpose;
import rpgshop.infraestructure.persistence.mapper.customer.AddressMapper;
import rpgshop.infraestructure.persistence.entity.customer.AddressJpaEntity;
import rpgshop.infraestructure.persistence.repository.customer.AddressRepository;
import rpgshop.infraestructure.persistence.repository.customer.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AddressGatewayJpa implements AddressGateway {
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public AddressGatewayJpa(final AddressRepository addressRepository, final CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Address save(final Address address, final UUID customerId) {
        final AddressJpaEntity entity = AddressMapper.toEntity(address);
        customerRepository.findById(customerId).ifPresent(entity::setCustomer);
        final AddressJpaEntity saved = addressRepository.save(entity);
        return AddressMapper.toDomain(saved);
    }

    @Override
    public Address saveDetached(final Address address) {
        final AddressJpaEntity entity = AddressMapper.toEntity(address);
        entity.setCustomer(null);
        final AddressJpaEntity saved = addressRepository.save(entity);
        return AddressMapper.toDomain(saved);
    }

    @Override
    public Optional<Address> findById(final UUID id) {
        return addressRepository.findById(id).map(AddressMapper::toDomain);
    }

    @Override
    public List<Address> findActiveByCustomerId(final UUID customerId) {
        return addressRepository.findByCustomerIdAndIsActiveTrue(customerId)
            .stream()
            .map(AddressMapper::toDomain)
            .toList();
    }

    @Override
    public List<Address> findByCustomerIdAndPurpose(final UUID customerId, final AddressPurpose purpose) {
        return addressRepository.findByCustomerIdAndPurposeAndIsActiveTrue(customerId, purpose)
            .stream()
            .map(AddressMapper::toDomain)
            .toList();
    }

    @Override
    public boolean existsByCustomerIdAndPurpose(final UUID customerId, final AddressPurpose purpose) {
        return addressRepository.existsByCustomerIdAndPurposeAndIsActiveTrue(customerId, purpose);
    }

    @Override
    public boolean existsByIdAndCustomerId(final UUID addressId, final UUID customerId) {
        return addressRepository.existsByIdAndCustomerId(addressId, customerId);
    }

    @Override
    public boolean existsByIdAndCustomerIdOrWithoutCustomer(final UUID addressId, final UUID customerId) {
        return addressRepository.existsByIdAndCustomerIdOrWithoutCustomer(addressId, customerId);
    }
}
