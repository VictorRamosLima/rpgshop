package rpgshop.application.usecase.cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.infraestructure.config.CartBlockingProperties;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReleaseExpiredCartItemsSchedulerTest {
    @Mock
    private ReleaseExpiredCartItemsUseCase useCase;

    @Mock
    private CartBlockingProperties properties;

    @InjectMocks
    private ReleaseExpiredCartItemsScheduler scheduler;

    @Test
    void shouldExecuteAutoReleaseWhenEnabled() {
        when(properties.isAutoReleaseEnabled()).thenReturn(true);
        when(useCase.execute()).thenReturn(2);

        scheduler.run();

        verify(useCase).execute();
    }

    @Test
    void shouldSkipAutoReleaseWhenDisabled() {
        when(properties.isAutoReleaseEnabled()).thenReturn(false);

        scheduler.run();

        verify(useCase, never()).execute();
    }
}
