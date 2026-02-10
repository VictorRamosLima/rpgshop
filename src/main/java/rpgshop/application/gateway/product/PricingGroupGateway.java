package rpgshop.application.gateway.product;

import rpgshop.domain.entity.product.PricingGroup;

import java.util.Optional;
import java.util.UUID;

public interface PricingGroupGateway {
    Optional<PricingGroup> findById(final UUID id);
}
