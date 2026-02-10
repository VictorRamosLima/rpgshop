package rpgshop.infraestructure.persistence.gateway.exchange;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import rpgshop.application.gateway.exchange.ExchangeRequestGateway;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.infraestructure.persistence.mapper.exchange.ExchangeRequestMapper;
import rpgshop.infraestructure.persistence.repository.exchange.ExchangeRequestRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class ExchangeRequestGatewayJpa implements ExchangeRequestGateway {
    private final ExchangeRequestRepository exchangeRequestRepository;

    public ExchangeRequestGatewayJpa(final ExchangeRequestRepository exchangeRequestRepository) {
        this.exchangeRequestRepository = exchangeRequestRepository;
    }

    @Override
    public ExchangeRequest save(final ExchangeRequest exchangeRequest) {
        final var entity = ExchangeRequestMapper.toEntity(exchangeRequest);
        final var saved = exchangeRequestRepository.save(entity);
        return ExchangeRequestMapper.toDomain(saved);
    }

    @Override
    public Optional<ExchangeRequest> findById(final UUID id) {
        return exchangeRequestRepository.findById(id).map(ExchangeRequestMapper::toDomain);
    }

    @Override
    public Page<ExchangeRequest> findAll(final Pageable pageable) {
        return exchangeRequestRepository.findAll(pageable)
            .map(ExchangeRequestMapper::toDomain);
    }

    @Override
    public Page<ExchangeRequest> findByStatus(final ExchangeStatus status, final Pageable pageable) {
        return exchangeRequestRepository.findByStatus(status, pageable)
            .map(ExchangeRequestMapper::toDomain);
    }

    @Override
    public Page<ExchangeRequest> findByOrderId(final UUID orderId, final Pageable pageable) {
        return exchangeRequestRepository.findByOrderId(orderId, pageable)
            .map(ExchangeRequestMapper::toDomain);
    }

    @Override
    public boolean existsByOrderItemIdAndStatusNot(final UUID orderItemId, final ExchangeStatus status) {
        return exchangeRequestRepository.existsByOrderItemIdAndStatusNot(orderItemId, status);
    }

    @Override
    public Page<ExchangeRequest> findByCustomerId(final UUID customerId, final Pageable pageable) {
        return exchangeRequestRepository.findByOrderCustomerId(customerId, pageable)
            .map(ExchangeRequestMapper::toDomain);
    }
}
