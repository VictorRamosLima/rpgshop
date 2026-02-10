package rpgshop.application.usecase.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rpgshop.application.command.product.ProductFilter;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.product.Product;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryProductsUseCaseTest {
    @Mock
    private ProductGateway productGateway;

    @InjectMocks
    private QueryProductsUseCase useCase;

    @Test
    void shouldDelegateFilterSearchToGateway() {
        final ProductFilter filter = new ProductFilter(
            "Livro",
            null,
            null,
            null,
            null,
            null,
            true,
            null,
            null
        );
        final Pageable pageable = PageRequest.of(0, 10);
        final Page<Product> expected = new PageImpl<>(List.of(Product.builder().id(UUID.randomUUID()).name("Livro").build()));

        when(productGateway.findByFilters(filter, pageable)).thenReturn(expected);

        final Page<Product> result = useCase.execute(filter, pageable);

        assertEquals(1, result.getTotalElements());
    }
}
