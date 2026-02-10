package rpgshop.application.usecase.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import rpgshop.application.gateway.exchange.ExchangeRequestGateway;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QueryExchangesUseCaseTest {
    @Mock
    private ExchangeRequestGateway exchangeRequestGateway;

    @InjectMocks
    private QueryExchangesUseCase useCase;

    @Test
    void shouldFindExchangeById() {
        final UUID exchangeId = UUID.randomUUID();
        when(exchangeRequestGateway.findById(exchangeId))
            .thenReturn(Optional.of(ExchangeRequest.builder().id(exchangeId).build()));

        final Optional<ExchangeRequest> result = useCase.findById(exchangeId);

        assertTrue(result.isPresent());
        assertEquals(exchangeId, result.orElseThrow().id());
    }

    @Test
    void shouldFindExchangesByFilters() {
        final Pageable pageable = PageRequest.of(0, 10);
        final UUID customerId = UUID.randomUUID();
        final ExchangeRequest exchangeRequest = ExchangeRequest.builder().id(UUID.randomUUID()).build();
        final Page<ExchangeRequest> page = new PageImpl<>(List.of(exchangeRequest));

        when(exchangeRequestGateway.findAll(pageable)).thenReturn(page);
        when(exchangeRequestGateway.findByStatus(ExchangeStatus.REQUESTED, pageable)).thenReturn(page);
        when(exchangeRequestGateway.findByCustomerId(customerId, pageable)).thenReturn(page);

        assertEquals(1, useCase.findAll(pageable).getTotalElements());
        assertEquals(1, useCase.findByStatus(ExchangeStatus.REQUESTED, pageable).getTotalElements());
        assertEquals(1, useCase.findByCustomerId(customerId, pageable).getTotalElements());
    }
}
