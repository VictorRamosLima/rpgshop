package rpgshop.infraestructure.persistence.gateway.stock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rpgshop.application.gateway.stock.StockEntryGateway;
import rpgshop.domain.entity.stock.StockEntry;
import rpgshop.infraestructure.persistence.mapper.stock.StockEntryMapper;
import rpgshop.infraestructure.persistence.repository.stock.StockEntryRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class StockEntryGatewayJpa implements StockEntryGateway {
    private final StockEntryRepository stockEntryRepository;

    public StockEntryGatewayJpa(final StockEntryRepository stockEntryRepository) {
        this.stockEntryRepository = stockEntryRepository;
    }

    @Override
    public StockEntry save(final StockEntry entry) {
        final var entity = StockEntryMapper.toEntity(entry);
        final var saved = stockEntryRepository.save(entity);
        return StockEntryMapper.toDomain(saved);
    }

    @Override
    public Optional<StockEntry> findById(final UUID id) {
        return stockEntryRepository.findById(id).map(StockEntryMapper::toDomain);
    }

    @Override
    public Page<StockEntry> findByProductId(final UUID productId, final Pageable pageable) {
        return stockEntryRepository.findByProductId(productId, pageable)
            .map(StockEntryMapper::toDomain);
    }

    @Override
    public Optional<BigDecimal> findMaxCostValueByProductId(final UUID productId) {
        return stockEntryRepository.findMaxCostValueByProductId(productId);
    }

    @Override
    public int sumQuantityByProductId(final UUID productId) {
        return stockEntryRepository.sumQuantityByProductId(productId);
    }
}
