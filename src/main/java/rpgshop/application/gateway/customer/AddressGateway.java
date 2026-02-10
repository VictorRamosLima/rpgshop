package rpgshop.application.gateway.customer;

import rpgshop.domain.entity.customer.Address;
import rpgshop.domain.entity.customer.constant.AddressPurpose;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressGateway {
    Address save(final Address address, final UUID customerId);
    Optional<Address> findById(final UUID id);
    List<Address> findActiveByCustomerId(final UUID customerId);
    List<Address> findByCustomerIdAndPurpose(final UUID customerId, final AddressPurpose purpose);
    boolean existsByCustomerIdAndPurpose(final UUID customerId, final AddressPurpose purpose);
}
