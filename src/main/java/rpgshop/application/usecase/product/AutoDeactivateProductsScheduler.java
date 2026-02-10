package rpgshop.application.usecase.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rpgshop.infraestructure.config.ProductAutoDeactivationProperties;
import rpgshop.application.exception.BusinessRuleException;

@Component
public class AutoDeactivateProductsScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoDeactivateProductsScheduler.class);

    private final AutoDeactivateProductsUseCase autoDeactivateProductsUseCase;
    private final ProductAutoDeactivationProperties properties;

    public AutoDeactivateProductsScheduler(
        final AutoDeactivateProductsUseCase autoDeactivateProductsUseCase,
        final ProductAutoDeactivationProperties properties
    ) {
        this.autoDeactivateProductsUseCase = autoDeactivateProductsUseCase;
        this.properties = properties;
    }

    @Scheduled(cron = "${rpgshop.product.auto-deactivation.cron:0 0 3 * * *}")
    public void run() {
        if (!properties.isEnabled()) {
            return;
        }

        try {
            final int deactivated = autoDeactivateProductsUseCase.execute(properties.getThreshold());
            LOGGER.info("Auto inativacao executada com threshold {}. Produtos inativados: {}", properties.getThreshold(), deactivated);
        } catch (BusinessRuleException e) {
            LOGGER.warn("Falha na auto inativacao: {}", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Erro inesperado na auto inativacao", e);
        }
    }
}
