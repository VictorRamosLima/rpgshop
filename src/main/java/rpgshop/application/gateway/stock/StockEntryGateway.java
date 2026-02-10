package rpgshop.application.gateway.stock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rpgshop.domain.entity.stock.StockEntry;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface StockEntryGateway {
    StockEntry save(final StockEntry stockEntry);
    Optional<StockEntry> findById(final UUID id);
    Page<StockEntry> findByProductId(final UUID productId, final Pageable pageable);
    Optional<BigDecimal> findMaxCostValueByProductId(final UUID productId);
    int sumQuantityByProductId(final UUID productId);
}
