package rpgshop.infraestructure.persistence.gateway;

import org.springframework.stereotype.Component;
import rpgshop.application.gateway.product.PricingGroupGateway;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.infraestructure.persistence.mapper.product.PricingGroupMapper;
import rpgshop.infraestructure.persistence.repository.product.PricingGroupRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PricingGroupGatewayJpa implements PricingGroupGateway {
    private final PricingGroupRepository pricingGroupRepository;

    public PricingGroupGatewayJpa(PricingGroupRepository pricingGroupRepository) {
        this.pricingGroupRepository = pricingGroupRepository;
    }

    @Override
    public Optional<PricingGroup> findById(UUID id) {
        return pricingGroupRepository.findById(id).map(PricingGroupMapper::toDomain);
    }

    @Override
    public List<PricingGroup> findActive() {
        return pricingGroupRepository.findByIsActiveTrueOrderByNameAsc()
            .stream()
            .map(PricingGroupMapper::toDomain)
            .toList();
    }
}
