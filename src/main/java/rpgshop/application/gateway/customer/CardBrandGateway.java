package rpgshop.application.gateway.customer;

import rpgshop.domain.entity.customer.CardBrand;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardBrandGateway {
    Optional<CardBrand> findById(final UUID id);
    List<CardBrand> findActive();
    boolean existsByName(final String name);
}
