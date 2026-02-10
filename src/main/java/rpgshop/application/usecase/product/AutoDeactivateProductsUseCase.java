package rpgshop.application.usecase.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.product.ProductGateway;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static java.math.BigDecimal.ZERO;

@Service
public class AutoDeactivateProductsUseCase {
    private final ProductGateway productGateway;

    public AutoDeactivateProductsUseCase(final ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Transactional
    public int execute(final BigDecimal threshold) {
        if (threshold == null || threshold.compareTo(ZERO) <= 0) {
            throw new BusinessRuleException("Threshold must be greater than zero");
        }

        final List<UUID> ids = productGateway.findEligibleForAutoDeactivation(threshold);

        if (ids.isEmpty()) {
            return 0;
        }

        return productGateway.deactivateOutOfMarket(threshold, Instant.now());
    }
}
