package rpgshop.infraestructure.persistence.gateway;

import org.springframework.stereotype.Component;
import rpgshop.application.gateway.customer.CardBrandGateway;
import rpgshop.domain.entity.customer.CardBrand;
import rpgshop.infraestructure.mapper.customer.CardBrandMapper;
import rpgshop.infraestructure.persistence.repository.customer.CardBrandRepository;

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
    public boolean existsByName(final String name) {
        return cardBrandRepository.existsByNameIgnoreCase(name);
    }
}
