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
            throw new BusinessRuleException("O motivo da inativacao e obrigatorio para inativacao manual");
        }
        if (command.category() == null) {
            throw new BusinessRuleException("A categoria da inativacao e obrigatoria para inativacao manual");
        }

        final Product product = productGateway
            .findById(command.productId())
            .orElseThrow(() -> new EntityNotFoundException("Product", command.productId()));

        if (!product.isActive()) {
            throw new BusinessRuleException("O produto ja esta inativo");
        }

        Product deactivated = product.deactivate(command.reason(), command.category());

        return productGateway.save(deactivated);
    }
}
