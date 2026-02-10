package rpgshop.application.usecase.exchange;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rpgshop.application.gateway.exchange.ExchangeRequestGateway;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;

import java.util.Optional;
import java.util.UUID;

@Service
public class QueryExchangesUseCase {
    private final ExchangeRequestGateway exchangeRequestGateway;

    public QueryExchangesUseCase(final ExchangeRequestGateway exchangeRequestGateway) {
        this.exchangeRequestGateway = exchangeRequestGateway;
    }

    @Transactional(readOnly = true)
    public Optional<ExchangeRequest> findById(final UUID id) {
        return exchangeRequestGateway.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<ExchangeRequest> findAll(final Pageable pageable) {
        return exchangeRequestGateway.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ExchangeRequest> findByStatus(final ExchangeStatus status, final Pageable pageable) {
        return exchangeRequestGateway.findByStatus(status, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ExchangeRequest> findByCustomerId(final UUID customerId, final Pageable pageable) {
        return exchangeRequestGateway.findByCustomerId(customerId, pageable);
    }
}
