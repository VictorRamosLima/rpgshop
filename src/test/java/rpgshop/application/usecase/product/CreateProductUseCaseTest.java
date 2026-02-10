package rpgshop.application.usecase.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.product.CreateProductCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.exception.EntityNotFoundException;
import rpgshop.application.gateway.product.CategoryGateway;
import rpgshop.application.gateway.product.PricingGroupGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.application.gateway.product.ProductTypeGateway;
import rpgshop.domain.entity.product.Category;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.ProductType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {
    @Mock
    private ProductGateway productGateway;

    @Mock
    private ProductTypeGateway productTypeGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private PricingGroupGateway pricingGroupGateway;

    @InjectMocks
    private CreateProductUseCase useCase;

    @Test
    void shouldCreateProductWithCalculatedSalePrice() {
        final UUID typeId = UUID.randomUUID();
        final UUID categoryId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();

        final CreateProductCommand command = new CreateProductCommand(
            "Livro",
            typeId,
            List.of(categoryId),
            new BigDecimal("10.00"),
            new BigDecimal("5.00"),
            new BigDecimal("2.00"),
            new BigDecimal("0.50"),
            pricingGroupId,
            "123456",
            "SKU-1",
            new BigDecimal("100.00")
        );

        when(productGateway.existsBySku("SKU-1")).thenReturn(false);
        when(productGateway.existsByBarcode("123456")).thenReturn(false);
        when(productTypeGateway.findById(typeId)).thenReturn(Optional.of(ProductType.builder().id(typeId).build()));
        when(categoryGateway.findAllByIds(List.of(categoryId))).thenReturn(List.of(Category.builder().id(categoryId).build()));
        when(pricingGroupGateway.findById(pricingGroupId)).thenReturn(
            Optional.of(PricingGroup.builder().id(pricingGroupId).marginPercentage(new BigDecimal("20.00")).build())
        );
        when(productGateway.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Product saved = useCase.execute(command);

        assertEquals(new BigDecimal("120.00"), saved.salePrice());
        assertEquals(0, saved.stockQuantity());
        assertEquals("Livro", saved.name());
    }

    @Test
    void shouldThrowWhenAnyCategoryIsMissing() {
        final UUID typeId = UUID.randomUUID();
        final UUID categoryId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final CreateProductCommand command = new CreateProductCommand(
            "Livro",
            typeId,
            List.of(categoryId),
            new BigDecimal("10.00"),
            new BigDecimal("5.00"),
            new BigDecimal("2.00"),
            new BigDecimal("0.50"),
            pricingGroupId,
            "123456",
            "SKU-1",
            new BigDecimal("100.00")
        );

        when(productGateway.existsBySku("SKU-1")).thenReturn(false);
        when(productGateway.existsByBarcode("123456")).thenReturn(false);
        when(productTypeGateway.findById(typeId)).thenReturn(Optional.of(ProductType.builder().id(typeId).build()));
        when(categoryGateway.findAllByIds(List.of(categoryId))).thenReturn(List.of());

        assertThrows(EntityNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldThrowWhenSkuAlreadyExists() {
        final CreateProductCommand command = new CreateProductCommand(
            "Livro",
            UUID.randomUUID(),
            List.of(UUID.randomUUID()),
            new BigDecimal("10.00"),
            new BigDecimal("5.00"),
            new BigDecimal("2.00"),
            new BigDecimal("0.50"),
            UUID.randomUUID(),
            "123456",
            "SKU-1",
            new BigDecimal("100.00")
        );

        when(productGateway.existsBySku("SKU-1")).thenReturn(true);

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }
}
