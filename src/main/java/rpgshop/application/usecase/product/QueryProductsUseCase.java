package rpgshop.application.usecase.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.command.product.ProductFilter;
import rpgshop.application.gateway.product.ProductGateway;
import rpgshop.domain.entity.product.Product;

@Service
public class QueryProductsUseCase {
    private final ProductGateway productGateway;

    public QueryProductsUseCase(final ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    @Transactional(readOnly = true)
    public Page<Product> execute(final ProductFilter filter, final Pageable pageable) {
        return productGateway.findByFilters(filter, pageable);
    }
}
