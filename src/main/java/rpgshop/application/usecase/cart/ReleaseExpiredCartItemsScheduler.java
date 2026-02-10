package rpgshop.application.usecase.cart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rpgshop.infraestructure.config.CartBlockingProperties;

@Component
public class ReleaseExpiredCartItemsScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReleaseExpiredCartItemsScheduler.class);

    private final ReleaseExpiredCartItemsUseCase releaseExpiredCartItemsUseCase;
    private final CartBlockingProperties cartBlockingProperties;

    public ReleaseExpiredCartItemsScheduler(
        final ReleaseExpiredCartItemsUseCase releaseExpiredCartItemsUseCase,
        final CartBlockingProperties cartBlockingProperties
    ) {
        this.releaseExpiredCartItemsUseCase = releaseExpiredCartItemsUseCase;
        this.cartBlockingProperties = cartBlockingProperties;
    }

    @Scheduled(cron = "${rpgshop.cart.blocking.release-cron:0 * * * * *}")
    public void run() {
        if (!cartBlockingProperties.isAutoReleaseEnabled()) {
            return;
        }

        try {
            final int released = releaseExpiredCartItemsUseCase.execute();
            if (released > 0) {
                LOGGER.info("Desbloqueio automatico do carrinho executado. Itens removidos: {}", released);
            }
        } catch (Exception e) {
            LOGGER.error("Falha na liberacao automatica de itens expirados do carrinho", e);
        }
    }
}
