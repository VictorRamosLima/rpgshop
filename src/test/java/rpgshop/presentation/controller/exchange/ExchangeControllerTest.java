package rpgshop.presentation.controller.exchange;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.order.OrderItemGateway;
import rpgshop.application.gateway.supplier.SupplierGateway;
import rpgshop.application.usecase.exchange.AuthorizeExchangeUseCase;
import rpgshop.application.usecase.exchange.DenyExchangeUseCase;
import rpgshop.application.usecase.exchange.QueryExchangesUseCase;
import rpgshop.application.usecase.exchange.ReceiveExchangeItemsUseCase;
import rpgshop.application.usecase.exchange.RequestExchangeUseCase;
import rpgshop.application.usecase.order.QueryOrdersUseCase;
import rpgshop.domain.entity.exchange.ExchangeRequest;
import rpgshop.domain.entity.exchange.constant.ExchangeStatus;
import rpgshop.domain.entity.supplier.Supplier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeControllerTest {
    @Mock
    private RequestExchangeUseCase requestExchangeUseCase;
    @Mock
    private AuthorizeExchangeUseCase authorizeExchangeUseCase;
    @Mock
    private DenyExchangeUseCase denyExchangeUseCase;
    @Mock
    private ReceiveExchangeItemsUseCase receiveExchangeItemsUseCase;
    @Mock
    private QueryExchangesUseCase queryExchangesUseCase;
    @Mock
    private QueryOrdersUseCase queryOrdersUseCase;
    @Mock
    private OrderItemGateway orderItemGateway;
    @Mock
    private SupplierGateway supplierGateway;
    @Mock
    private Model model;

    @InjectMocks
    private ExchangeController controller;

    @Test
    void shouldListExchangesByStatus() {
        final Page<ExchangeRequest> exchanges = new PageImpl<>(List.of());
        when(queryExchangesUseCase.findByStatus(ExchangeStatus.REQUESTED, PageRequest.of(2, 10))).thenReturn(exchanges);

        final String view = controller.list(ExchangeStatus.REQUESTED, 2, model);

        assertEquals("exchange/list", view);
        verify(queryExchangesUseCase).findByStatus(ExchangeStatus.REQUESTED, PageRequest.of(2, 10));
        verify(model).addAttribute("exchanges", exchanges);
        verify(model).addAttribute(eq("statuses"), any());
    }

    @Test
    void shouldRedirectWhenExchangeDetailIsMissing() {
        final UUID exchangeId = UUID.randomUUID();
        when(queryExchangesUseCase.findById(exchangeId)).thenReturn(Optional.empty());

        final String view = controller.detail(exchangeId, model);

        assertEquals("redirect:/exchanges", view);
        verify(queryExchangesUseCase).findById(exchangeId);
    }

    @Test
    void shouldRenderExchangeDetailWithActiveSuppliers() {
        final UUID exchangeId = UUID.randomUUID();
        final ExchangeRequest exchange = ExchangeRequest.builder().id(exchangeId).build();
        final Supplier supplier = Supplier.builder().id(UUID.randomUUID()).name("Fornecedor").build();
        when(queryExchangesUseCase.findById(exchangeId)).thenReturn(Optional.of(exchange));
        when(supplierGateway.findActiveSuppliers(PageRequest.of(0, 100)))
            .thenReturn(new PageImpl<>(List.of(supplier)));

        final String view = controller.detail(exchangeId, model);

        assertEquals("exchange/detail", view);
        verify(model).addAttribute("exchange", exchange);
        verify(model).addAttribute("activeSuppliers", List.of(supplier));
    }

    @Test
    void shouldAuthorizeAndRedirect() {
        final UUID exchangeId = UUID.randomUUID();

        final String view = controller.authorize(exchangeId, model);

        assertEquals("redirect:/exchanges/" + exchangeId, view);
        verify(authorizeExchangeUseCase).execute(exchangeId);
    }

    @Test
    void shouldReturnDetailWithErrorWhenAuthorizeFails() {
        final UUID exchangeId = UUID.randomUUID();
        final ExchangeRequest exchange = ExchangeRequest.builder().id(exchangeId).build();
        when(authorizeExchangeUseCase.execute(exchangeId)).thenThrow(new BusinessRuleException("nao autorizado"));
        when(queryExchangesUseCase.findById(exchangeId)).thenReturn(Optional.of(exchange));
        when(supplierGateway.findActiveSuppliers(PageRequest.of(0, 100))).thenReturn(new PageImpl<>(List.of()));

        final String view = controller.authorize(exchangeId, model);

        assertEquals("exchange/detail", view);
        verify(model).addAttribute("error", "nao autorizado");
        verify(model).addAttribute("exchange", exchange);
    }
}
