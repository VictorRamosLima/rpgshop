package rpgshop.application.gateway.customer;

import rpgshop.domain.entity.customer.CreditCard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditCardGateway {
    CreditCard save(final CreditCard creditCard, final UUID customerId);
    Optional<CreditCard> findById(final UUID id);
    List<CreditCard> findActiveByCustomerId(final UUID customerId);
    Optional<CreditCard> findPreferredByCustomerId(final UUID customerId);
    int clearPreferredByCustomerId(final UUID customerId);
    boolean existsByCustomerIdAndCardNumber(final UUID customerId, final String cardNumber);
}
