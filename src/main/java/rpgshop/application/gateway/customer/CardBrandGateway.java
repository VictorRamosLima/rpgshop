package rpgshop.application.gateway.customer;

import rpgshop.domain.entity.customer.CardBrand;

import java.util.Optional;
import java.util.UUID;

public interface CardBrandGateway {
    Optional<CardBrand> findById(final UUID id);
    boolean existsByName(final String name);
}
