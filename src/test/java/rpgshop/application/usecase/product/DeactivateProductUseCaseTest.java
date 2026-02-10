package rpgshop.application.usecase.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.product.DeactivateProductCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.StatusChange;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static rpgshop.domain.entity.product.constant.StatusChangeCategory.OUT_OF_MARKET;

@ExtendWith(MockitoExtension.class)
class DeactivateProductUseCaseTest {
    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private DeactivateProductUseCase useCase;

    @Test
    void shouldDeactivateActiveProduct() {
        final UUID productId = UUID.randomUUID();
        final DeactivateProductCommand command = new DeactivateProductCommand(
            productId,
            "Sem giro",
            OUT_OF_MARKET
        );
        final Product active = Product.builder().id(productId).statusChanges(List.of()).build();

        when(productGateway.findById(productId)).thenReturn(Optional.of(active));
        when(productGateway.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Product deactivated = useCase.execute(command);

        assertEquals(1, deactivated.statusChanges().size());
        assertEquals(OUT_OF_MARKET, deactivated.statusChanges().getLast().category());
    }

    @Test
    void shouldThrowWhenProductIsAlreadyInactive() {
        final UUID productId = UUID.randomUUID();
        final DeactivateProductCommand command = new DeactivateProductCommand(
            productId,
            "Sem giro",
            OUT_OF_MARKET
        );
        final Product inactive = Product.builder()
            .id(productId)
            .statusChanges(List.of(StatusChange.deactivate("motivo", OUT_OF_MARKET)))
            .build();

        when(productGateway.findById(productId)).thenReturn(Optional.of(inactive));

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }
}
