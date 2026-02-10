package rpgshop.infraestructure.persistence.gateway.customer;

import org.springframework.stereotype.Component;
import rpgshop.application.gateway.customer.CardBrandGateway;
import rpgshop.domain.entity.customer.CardBrand;
import rpgshop.infraestructure.persistence.mapper.customer.CardBrandMapper;
import rpgshop.infraestructure.persistence.repository.customer.CardBrandRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CardBrandGatewayJpa implements CardBrandGateway {
    private final CardBrandRepository cardBrandRepository;

    public CardBrandGatewayJpa(final CardBrandRepository cardBrandRepository) {
        this.cardBrandRepository = cardBrandRepository;
    }

    @Override
    public Optional<CardBrand> findById(final UUID id) {
        return cardBrandRepository.findById(id).map(CardBrandMapper::toDomain);
    }

    @Override
    public List<CardBrand> findActive() {
        return cardBrandRepository.findByIsActiveTrueOrderByNameAsc()
            .stream()
            .map(CardBrandMapper::toDomain)
            .toList();
    }

    @Override
    public boolean existsByName(final String name) {
        return cardBrandRepository.existsByNameIgnoreCase(name);
    }
}
