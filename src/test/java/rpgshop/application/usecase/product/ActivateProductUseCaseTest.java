package rpgshop.application.usecase.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.product.ActivateProductCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.StatusChange;
import rpgshop.domain.entity.product.constant.StatusChangeCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static rpgshop.domain.entity.product.constant.StatusChangeCategory.BACK_IN_DEMAND;
import static rpgshop.domain.entity.product.constant.StatusChangeCategory.OUT_OF_MARKET;

@ExtendWith(MockitoExtension.class)
class ActivateProductUseCaseTest {
    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private ActivateProductUseCase useCase;

    @Test
    void shouldActivateInactiveProduct() {
        final UUID productId = UUID.randomUUID();
        final ActivateProductCommand command = new ActivateProductCommand(
            productId,
            "Voltou para o catalogo",
            BACK_IN_DEMAND
        );

        final Product inactive = Product.builder()
            .id(productId)
            .name("Livro")
            .statusChanges(List.of(StatusChange.deactivate("Saiu do mercado", OUT_OF_MARKET)))
            .build();

        when(productGateway.findById(productId)).thenReturn(Optional.of(inactive));
        when(productGateway.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Product activated = useCase.execute(command);

        assertEquals(2, activated.statusChanges().size());
        assertEquals(BACK_IN_DEMAND, activated.statusChanges().getLast().category());
        assertEquals("Voltou para o catalogo", activated.statusChanges().getLast().reason());
    }

    @Test
    void shouldThrowWhenProductIsAlreadyActive() {
        final UUID productId = UUID.randomUUID();
        final ActivateProductCommand command = new ActivateProductCommand(
            productId,
            "Motivo",
            BACK_IN_DEMAND
        );
        final Product active = Product.builder().id(productId).statusChanges(List.of()).build();

        when(productGateway.findById(productId)).thenReturn(Optional.of(active));

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }
}
