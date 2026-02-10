package rpgshop.application.usecase.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.StatusChange;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static rpgshop.domain.entity.product.constant.StatusChangeCategory.OUT_OF_MARKET;

@ExtendWith(MockitoExtension.class)
class AutoDeactivateProductsUseCaseTest {
    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private AutoDeactivateProductsUseCase useCase;

    @Test
    void shouldDeactivateOnlyActiveProductsFromEligibleIds() {
        final UUID activeId = UUID.randomUUID();
        final UUID inactiveId = UUID.randomUUID();
        final UUID missingId = UUID.randomUUID();

        final Product active = Product.builder().id(activeId).statusChanges(List.of()).build();
        final Product inactive = Product.builder()
            .id(inactiveId)
            .statusChanges(List.of(StatusChange.deactivate("Motivo", OUT_OF_MARKET)))
            .build();

        when(productGateway.findById(activeId)).thenReturn(Optional.of(active));
        when(productGateway.findById(inactiveId)).thenReturn(Optional.of(inactive));
        when(productGateway.findById(missingId)).thenReturn(Optional.empty());
        when(productGateway.findEligibleForAutoDeactivation(new BigDecimal("50.00")))
            .thenReturn(List.of(activeId, inactiveId, missingId));

        final int deactivated = useCase.execute(new BigDecimal("50.00"));

        assertEquals(1, deactivated);
        verify(productGateway).save(any(Product.class));
    }

    @Test
    void shouldReturnZeroWhenNoEligibleProductsExist() {
        when(productGateway.findEligibleForAutoDeactivation(new BigDecimal("50.00"))).thenReturn(List.of());

        final int deactivated = useCase.execute(new BigDecimal("50.00"));

        assertEquals(0, deactivated);
        verify(productGateway, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowWhenThresholdIsInvalid() {
        assertThrows(BusinessRuleException.class, () -> useCase.execute(BigDecimal.ZERO));
    }
}
