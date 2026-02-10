package rpgshop.application.usecase.product;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.product.DeactivateProductCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.product.Product;

@Service
public class DeactivateProductUseCase {
    @Nonnull
    private final ProductGateway productGateway;

    public DeactivateProductUseCase(@Nonnull final ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Nonnull
    @Transactional
    public Product execute(@Nonnull final DeactivateProductCommand command) {
        if (command.reason() == null || command.reason().isBlank()) {
            throw new BusinessRuleException("Inactivation reason is required for manual deactivation");
        }
        if (command.category() == null) {
            throw new BusinessRuleException("Inactivation category is required for manual deactivation");
        }

        final Product product = productGateway
            .findById(command.productId())
            .orElseThrow(() -> new EntityNotFoundException("Product", command.productId()));

        if (!product.isActive()) {
            throw new BusinessRuleException("Product is already inactive");
        }

        Product deactivated = product.deactivate(command.reason(), command.category());

        return productGateway.save(deactivated);
    }
}
