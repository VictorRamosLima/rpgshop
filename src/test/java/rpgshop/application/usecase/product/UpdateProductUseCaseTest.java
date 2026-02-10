package rpgshop.application.usecase.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rpgshop.application.command.product.UpdateProductCommand;
import rpgshop.application.exception.BusinessRuleException;
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
class UpdateProductUseCaseTest {
    @Mock
    private ProductGateway productGateway;

    @Mock
    private ProductTypeGateway productTypeGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private PricingGroupGateway pricingGroupGateway;

    @InjectMocks
    private UpdateProductUseCase useCase;

    @Test
    void shouldUpdateProductUsingCalculatedMinimumSalePriceWhenSalePriceIsNull() {
        final UUID productId = UUID.randomUUID();
        final UUID typeId = UUID.randomUUID();
        final UUID categoryId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();

        final Product existing = Product.builder()
            .id(productId)
            .name("Livro antigo")
            .sku("SKU-1")
            .barcode("123")
            .build();
        final UpdateProductCommand command = new UpdateProductCommand(
            productId,
            "Livro novo",
            typeId,
            List.of(categoryId),
            new BigDecimal("10.00"),
            new BigDecimal("5.00"),
            new BigDecimal("2.00"),
            new BigDecimal("1.00"),
            pricingGroupId,
            "123",
            "SKU-1",
            new BigDecimal("100.00"),
            null,
            false
        );

        when(productGateway.findById(productId)).thenReturn(Optional.of(existing));
        when(productTypeGateway.findById(typeId)).thenReturn(Optional.of(ProductType.builder().id(typeId).build()));
        when(categoryGateway.findAllByIds(List.of(categoryId))).thenReturn(List.of(Category.builder().id(categoryId).build()));
        when(pricingGroupGateway.findById(pricingGroupId)).thenReturn(
            Optional.of(PricingGroup.builder().id(pricingGroupId).marginPercentage(new BigDecimal("20.00")).build())
        );
        when(productGateway.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Product updated = useCase.execute(command);

        assertEquals("Livro novo", updated.name());
        assertEquals(new BigDecimal("120.00"), updated.salePrice());
    }

    @Test
    void shouldThrowWhenSalePriceIsBelowMinimumAndNoManagerAuthorization() {
        final UUID productId = UUID.randomUUID();
        final UUID typeId = UUID.randomUUID();
        final UUID categoryId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final Product existing = Product.builder().id(productId).sku("SKU-1").barcode("123").build();

        final UpdateProductCommand command = new UpdateProductCommand(
            productId,
            "Livro novo",
            typeId,
            List.of(categoryId),
            new BigDecimal("10.00"),
            new BigDecimal("5.00"),
            new BigDecimal("2.00"),
            new BigDecimal("1.00"),
            pricingGroupId,
            "123",
            "SKU-1",
            new BigDecimal("100.00"),
            new BigDecimal("90.00"),
            false
        );

        when(productGateway.findById(productId)).thenReturn(Optional.of(existing));
        when(productTypeGateway.findById(typeId)).thenReturn(Optional.of(ProductType.builder().id(typeId).build()));
        when(categoryGateway.findAllByIds(List.of(categoryId))).thenReturn(List.of(Category.builder().id(categoryId).build()));
        when(pricingGroupGateway.findById(pricingGroupId)).thenReturn(
            Optional.of(PricingGroup.builder().id(pricingGroupId).marginPercentage(new BigDecimal("20.00")).build())
        );

        assertThrows(BusinessRuleException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldAllowSalePriceBelowMinimumWhenManagerAuthorizes() {
        final UUID productId = UUID.randomUUID();
        final UUID typeId = UUID.randomUUID();
        final UUID categoryId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final Product existing = Product.builder().id(productId).sku("SKU-1").barcode("123").build();

        final UpdateProductCommand command = new UpdateProductCommand(
            productId,
            "Livro novo",
            typeId,
            List.of(categoryId),
            new BigDecimal("10.00"),
            new BigDecimal("5.00"),
            new BigDecimal("2.00"),
            new BigDecimal("1.00"),
            pricingGroupId,
            "123",
            "SKU-1",
            new BigDecimal("100.00"),
            new BigDecimal("90.00"),
            true
        );

        when(productGateway.findById(productId)).thenReturn(Optional.of(existing));
        when(productTypeGateway.findById(typeId)).thenReturn(Optional.of(ProductType.builder().id(typeId).build()));
        when(categoryGateway.findAllByIds(List.of(categoryId))).thenReturn(List.of(Category.builder().id(categoryId).build()));
        when(pricingGroupGateway.findById(pricingGroupId)).thenReturn(
            Optional.of(PricingGroup.builder().id(pricingGroupId).marginPercentage(new BigDecimal("20.00")).build())
        );
        when(productGateway.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Product updated = useCase.execute(command);

        assertEquals(new BigDecimal("90.00"), updated.salePrice());
    }
}
