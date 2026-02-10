package rpgshop.application.usecase.product;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.product.ActivateProductCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.product.Product;

@Service
public class ActivateProductUseCase {
    @Nonnull private final ProductGateway productGateway;

    public ActivateProductUseCase(@Nonnull final ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Nonnull
    @Transactional
    public Product execute(@Nonnull final ActivateProductCommand command) {
        if (command.reason() == null || command.reason().isBlank()) {
            throw new BusinessRuleException("O motivo da ativacao e obrigatorio");
        }
        if (command.category() == null) {
            throw new BusinessRuleException("A categoria da ativacao e obrigatoria para reativacao");
        }

        final Product product = productGateway
            .findById(command.productId())
            .orElseThrow(() -> new EntityNotFoundException("Product", command.productId()));

        if (product.isActive()) {
            throw new BusinessRuleException("O produto ja esta ativo");
        }

        final Product activated = product.activate(
            command.reason(),
            command.category()
        );

        return productGateway.save(activated);
    }
}
