package rpgshop.application.usecase.cart;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.gateway.cart.CartItemGateway;
import rpgshop.domain.entity.cart.CartItem;

import java.time.Instant;
import java.util.List;

@Service
public class ReleaseExpiredCartItemsUseCase {
    private final CartItemGateway cartItemGateway;

    public ReleaseExpiredCartItemsUseCase(final CartItemGateway cartItemGateway) {
        this.cartItemGateway = cartItemGateway;
    }

    @Transactional
    public int execute() {
        final List<CartItem> expiredItems = cartItemGateway.findExpiredBlockedItems(Instant.now());

        for (final CartItem item : expiredItems) {
            cartItemGateway.deleteByCartIdAndProductId(
                item.id(),
                item.product().id()
            );
        }

        return expiredItems.size();
    }
}
