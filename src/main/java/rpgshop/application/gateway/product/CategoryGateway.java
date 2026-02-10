package rpgshop.application.gateway.product;

import rpgshop.domain.entity.product.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryGateway {
    List<Category> findAllByIds(final List<UUID> ids);
}
