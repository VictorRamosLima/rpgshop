package rpgshop.application.gateway.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import rpgshop.application.command.product.ProductFilter;
import rpgshop.domain.entity.product.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductGateway {
    Product save(final Product product);
    Optional<Product> findById(final UUID id);
    Page<Product> findByFilters(final ProductFilter filter, final Pageable pageable);
    List<UUID> findEligibleForAutoDeactivation(final BigDecimal threshold);
    boolean existsBySku(final String sku);
    boolean existsByBarcode(final String barcode);
    int deactivateOutOfMarket(final List<UUID> ids, final Instant now);
}
