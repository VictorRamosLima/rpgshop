package rpgshop.application.usecase.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static java.math.BigDecimal.ZERO;

@Service
public class AutoDeactivateProductsUseCase {
    private static final String AUTO_DEACTIVATION_REASON = "Inativacao automatica por falta de estoque e baixa venda";

    private final ProductGateway productGateway;

    public AutoDeactivateProductsUseCase(final ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Transactional
    public int execute(final BigDecimal threshold) {
        if (threshold == null || threshold.compareTo(ZERO) <= 0) {
            throw new BusinessRuleException("O limite deve ser maior que zero");
        }

        final List<UUID> ids = productGateway.findEligibleForAutoDeactivation(threshold);

        if (ids.isEmpty()) {
            return 0;
        }

        int deactivatedCount = 0;

        for (final UUID id : ids) {
            final var product = productGateway.findById(id).orElse(null);

            if (product == null || !product.isActive()) {
                continue;
            }

            final var deactivated = product.deactivate(
                AUTO_DEACTIVATION_REASON,
                StatusChangeCategory.OUT_OF_MARKET
            );

            productGateway.save(deactivated);
            deactivatedCount++;
        }

        return deactivatedCount;
    }
}
