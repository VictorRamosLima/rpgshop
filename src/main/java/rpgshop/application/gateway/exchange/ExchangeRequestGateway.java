package rpgshop.application.gateway.exchange;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;

import java.util.Optional;
import java.util.UUID;

public interface ExchangeRequestGateway {
    ExchangeRequest save(final ExchangeRequest exchangeRequest);
    Optional<ExchangeRequest> findById(final UUID id);
    Page<ExchangeRequest> findByStatus(final ExchangeStatus status, final Pageable pageable);
    Page<ExchangeRequest> findByOrderId(final UUID orderId, final Pageable pageable);
    boolean existsByOrderItemIdAndStatusNot(final UUID orderItemId, final ExchangeStatus status);
    Page<ExchangeRequest> findByCustomerId(final UUID customerId, final Pageable pageable);
}
