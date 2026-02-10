package rpgshop.application.gateway.customer;

import rpgshop.domain.entity.customer.Phone;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhoneGateway {
    Phone save(final Phone phone, final UUID customerId);
    Optional<Phone> findById(final UUID id);
    List<Phone> findActiveByCustomerId(final UUID customerId);
    boolean existsByCustomerIdAndAreaCodeAndNumber(final UUID customerId, final String areaCode, final String number);
}
