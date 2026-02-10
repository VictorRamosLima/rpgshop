package rpgshop.presentation.controller.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import rpgshop.application.command.product.CreateProductCommand;
import rpgshop.application.command.product.ProductFilter;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.product.CategoryGateway;
import rpgshop.application.gateway.product.PricingGroupGateway;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.application.gateway.product.ProductTypeGateway;
import rpgshop.application.usecase.product.ActivateProductUseCase;
import rpgshop.application.usecase.product.AutoDeactivateProductsUseCase;
import rpgshop.application.usecase.product.CreateProductUseCase;
import rpgshop.application.usecase.product.DeactivateProductUseCase;
import rpgshop.application.usecase.product.UpdateProductUseCase;
import rpgshop.domain.entity.product.Category;
import rpgshop.domain.entity.product.PricingGroup;
import rpgshop.domain.entity.product.Product;
import rpgshop.domain.entity.product.ProductType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Mock
    private CreateProductUseCase createProductUseCase;
    @Mock
    private UpdateProductUseCase updateProductUseCase;
    @Mock
    private DeactivateProductUseCase deactivateProductUseCase;
    @Mock
    private ActivateProductUseCase activateProductUseCase;
    @Mock
    private AutoDeactivateProductsUseCase autoDeactivateProductsUseCase;
    @Mock
    private ProductGateway productGateway;
    @Mock
    private ProductTypeGateway productTypeGateway;
    @Mock
    private CategoryGateway categoryGateway;
    @Mock
    private PricingGroupGateway pricingGroupGateway;
    @Mock
    private Model model;

    @InjectMocks
    private ProductController controller;

    @Test
    void shouldListProductsWithNormalizedFilterAndReferenceData() {
        final Page<Product> products = new PageImpl<>(List.of(Product.builder().id(UUID.randomUUID()).build()));
        when(productGateway.findByFilters(any(), eq(PageRequest.of(1, 10)))).thenReturn(products);
        when(productTypeGateway.findActive()).thenReturn(List.of(ProductType.builder().id(UUID.randomUUID()).build()));
        when(categoryGateway.findActive()).thenReturn(List.of(Category.builder().id(UUID.randomUUID()).build()));
        when(pricingGroupGateway.findActive()).thenReturn(List.of(PricingGroup.builder().id(UUID.randomUUID()).build()));

        final String view = controller.list(
            "  Livro  ",
            null,
            null,
            null,
            "  SKU1 ",
            "  BAR123 ",
            true,
            new BigDecimal("10.00"),
            new BigDecimal("100.00"),
            2,
            1,
            model
        );

        assertEquals("product/list", view);
        final ArgumentCaptor<ProductFilter> captor = ArgumentCaptor.forClass(ProductFilter.class);
        verify(productGateway).findByFilters(captor.capture(), eq(PageRequest.of(1, 10)));
        assertEquals("Livro", captor.getValue().name());
        assertEquals("SKU1", captor.getValue().sku());
        assertEquals("BAR123", captor.getValue().barcode());
        verify(model).addAttribute("products", products);
        verify(model).addAttribute("success", "Inativacao automatica executada. Produtos inativados: 2");
    }

    @Test
    void shouldCreateProductAndRedirectToDetail() {
        final UUID productTypeId = UUID.randomUUID();
        final UUID pricingGroupId = UUID.randomUUID();
        final UUID createdId = UUID.randomUUID();
        when(createProductUseCase.execute(any())).thenReturn(Product.builder().id(createdId).build());

        final String view = controller.create(
            "Livro",
            productTypeId,
            List.of(),
            null,
            null,
            null,
            new BigDecimal("1.00"),
            pricingGroupId,
            " 123 ",
            " abc ",
            new BigDecimal("20.00"),
            model
        );

        assertEquals("redirect:/products/" + createdId, view);
        verify(createProductUseCase).execute(any(CreateProductCommand.class));
    }

    @Test
    void shouldRenderCreateFormWithErrorWhenCreateFails() {
        when(createProductUseCase.execute(any())).thenThrow(new BusinessRuleException("produto invalido"));
        when(productTypeGateway.findActive()).thenReturn(List.of());
        when(categoryGateway.findActive()).thenReturn(List.of());
        when(pricingGroupGateway.findActive()).thenReturn(List.of());

        final String view = controller.create(
            "Livro",
            UUID.randomUUID(),
            List.of(),
            null,
            null,
            null,
            new BigDecimal("1.00"),
            UUID.randomUUID(),
            null,
            null,
            new BigDecimal("20.00"),
            model
        );

        assertEquals("product/create", view);
        verify(model).addAttribute("error", "produto invalido");
        verify(productTypeGateway).findActive();
        verify(categoryGateway).findActive();
        verify(pricingGroupGateway).findActive();
    }
}
