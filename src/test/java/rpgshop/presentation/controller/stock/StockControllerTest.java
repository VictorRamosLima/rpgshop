package rpgshop.presentation.controller.stock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import rpgshop.application.command.stock.CreateStockEntryCommand;
import rpgshop.application.exception.BusinessRuleException;
import rpgshop.application.gateway.stock.StockEntryGateway;
import rpgshop.application.usecase.stock.CreateStockEntryUseCase;
import rpgshop.domain.entity.stock.StockEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockControllerTest {
    @Mock
    private CreateStockEntryUseCase createStockEntryUseCase;
    @Mock
    private StockEntryGateway stockEntryGateway;
    @Mock
    private Model model;

    @InjectMocks
    private StockController controller;

    @Test
    void shouldListStockEntriesByProduct() {
        final UUID productId = UUID.randomUUID();
        final Page<StockEntry> entries = new PageImpl<>(List.of(StockEntry.builder().id(UUID.randomUUID()).build()));
        when(stockEntryGateway.findByProductId(productId, PageRequest.of(2, 10))).thenReturn(entries);

        final String view = controller.listByProduct(productId, 2, model);

        assertEquals("stock/list", view);
        verify(stockEntryGateway).findByProductId(productId, PageRequest.of(2, 10));
        verify(model).addAttribute("entries", entries);
        verify(model).addAttribute("productId", productId);
    }

    @Test
    void shouldCreateStockEntryAndRedirect() {
        final UUID productId = UUID.randomUUID();
        final UUID supplierId = UUID.randomUUID();

        final String view = controller.create(
            productId,
            10,
            new BigDecimal("23.50"),
            supplierId,
            "2026-01-20",
            model
        );

        assertEquals("redirect:/stock/product/" + productId, view);
        verify(createStockEntryUseCase).execute(
            new CreateStockEntryCommand(productId, 10, new BigDecimal("23.50"), supplierId, LocalDate.parse("2026-01-20"))
        );
    }

    @Test
    void shouldRenderCreateFormWithErrorWhenCreateFails() {
        final UUID productId = UUID.randomUUID();
        final UUID supplierId = UUID.randomUUID();
        when(createStockEntryUseCase.execute(any())).thenThrow(new BusinessRuleException("entrada invalida"));

        final String view = controller.create(
            productId,
            5,
            new BigDecimal("10.00"),
            supplierId,
            "2026-01-20",
            model
        );

        assertEquals("stock/create", view);
        verify(model).addAttribute("error", "entrada invalida");
        verify(model).addAttribute("productId", productId);
    }
}
