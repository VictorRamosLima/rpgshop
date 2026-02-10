package rpgshop.application.usecase.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.infraestructure.config.ProductAutoDeactivationProperties;

import java.math.BigDecimal;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutoDeactivateProductsSchedulerTest {
    @Mock
    private AutoDeactivateProductsUseCase autoDeactivateProductsUseCase;

    @Mock
    private ProductAutoDeactivationProperties properties;

    @InjectMocks
    private AutoDeactivateProductsScheduler scheduler;

    @Test
    void shouldRunAutoDeactivationWhenEnabled() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getThreshold()).thenReturn(new BigDecimal("40.00"));
        when(autoDeactivateProductsUseCase.execute(new BigDecimal("40.00"))).thenReturn(2);

        scheduler.run();

        verify(autoDeactivateProductsUseCase).execute(new BigDecimal("40.00"));
    }

    @Test
    void shouldSkipExecutionWhenFeatureIsDisabled() {
        when(properties.isEnabled()).thenReturn(false);

        scheduler.run();

        verify(autoDeactivateProductsUseCase, never()).execute(new BigDecimal("40.00"));
    }

    @Test
    void shouldNotPropagateBusinessRuleExceptionFromUseCase() {
        when(properties.isEnabled()).thenReturn(true);
        when(properties.getThreshold()).thenReturn(new BigDecimal("40.00"));
        when(autoDeactivateProductsUseCase.execute(new BigDecimal("40.00")))
            .thenThrow(new BusinessRuleException("erro"));

        scheduler.run();

        verify(autoDeactivateProductsUseCase).execute(new BigDecimal("40.00"));
    }
}
