package rpgshop.infraestructure.persistence.gateway;


import org.springframework.stereotype.Component;
import rpgshop.application.gateway.product.CategoryGateway;
import rpgshop.domain.entity.product.Category;
import rpgshop.infraestructure.mapper.product.CategoryMapper;
import rpgshop.infraestructure.persistence.repository.product.CategoryRepository;

import java.util.List;
import java.util.UUID;

@Component
public class CategoryGatewayJpa implements CategoryGateway {
    private final CategoryRepository categoryRepository;

    public CategoryGatewayJpa(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAllByIds(final List<UUID> ids) {
        return categoryRepository.findByIdIn(ids)
            .stream()
            .map(CategoryMapper::toDomain)
            .toList();
    }
}
