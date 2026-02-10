package rpgshop.presentation.controller.analysis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import rpgshop.application.command.analysis.SalesAnalysisFilter;
import rpgshop.application.usecase.analysis.SalesAnalysisUseCase;
import rpgshop.domain.entity.order.Order;
import rpgshop.domain.entity.order.OrderItem;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalysisControllerTest {
    @Mock
    private SalesAnalysisUseCase salesAnalysisUseCase;

    @Mock
    private Model model;

    @InjectMocks
    private AnalysisController controller;

    @Test
    void shouldCallSalesUseCaseWhenPeriodIsProvided() {
        final Page<Order> sales = new PageImpl<>(List.of(Order.builder().id(UUID.randomUUID()).build()));
        when(salesAnalysisUseCase.findSalesInPeriod(any(), any(), any())).thenReturn(sales);

        final String view = controller.salesInPeriod("2026-01-01", "2026-01-31", 2, model);

        assertEquals("analysis/sales", view);
        verify(salesAnalysisUseCase).findSalesInPeriod(
            eq(Instant.parse("2026-01-01T00:00:00Z")),
            eq(Instant.parse("2026-01-31T23:59:59Z")),
            eq(PageRequest.of(2, 10))
        );
        verify(model).addAttribute("sales", sales);
    }

    @Test
    void shouldNotCallSalesUseCaseWhenPeriodIsMissing() {
        final String view = controller.salesInPeriod(null, "2026-01-31", 0, model);

        assertEquals("analysis/sales", view);
        verify(salesAnalysisUseCase, never()).findSalesInPeriod(any(), any(), any());
    }

    @Test
    void shouldQueryByProductAndAddResultsToModel() {
        final UUID productId = UUID.randomUUID();
        final Page<OrderItem> items = new PageImpl<>(List.of());
        when(salesAnalysisUseCase.findByProductAndPeriod(any(), any())).thenReturn(items);
        when(salesAnalysisUseCase.sumQuantitySoldByProductInPeriod(any())).thenReturn(7L);

        final String view = controller.byProduct(productId, "2026-01-01", "2026-01-15", model);

        assertEquals("analysis/by-product", view);
        final ArgumentCaptor<SalesAnalysisFilter> captor = ArgumentCaptor.forClass(SalesAnalysisFilter.class);
        verify(salesAnalysisUseCase).findByProductAndPeriod(captor.capture(), eq(PageRequest.of(0, 100)));
        verify(salesAnalysisUseCase).sumQuantitySoldByProductInPeriod(captor.getValue());
        assertEquals(productId, captor.getValue().productId());
        assertEquals(Instant.parse("2026-01-01T00:00:00Z"), captor.getValue().startDate());
        assertEquals(Instant.parse("2026-01-15T23:59:59Z"), captor.getValue().endDate());
        verify(model).addAttribute("items", items);
        verify(model).addAttribute("totalSold", 7L);
    }

    @Test
    void shouldQueryByCategoryAndAddItemsToModel() {
        final UUID categoryId = UUID.randomUUID();
        final Page<OrderItem> items = new PageImpl<>(List.of());
        when(salesAnalysisUseCase.findByCategoryAndPeriod(any(), any())).thenReturn(items);

        final String view = controller.byCategory(categoryId, "2026-01-01", "2026-01-31", model);

        assertEquals("analysis/by-category", view);
        final ArgumentCaptor<SalesAnalysisFilter> captor = ArgumentCaptor.forClass(SalesAnalysisFilter.class);
        verify(salesAnalysisUseCase).findByCategoryAndPeriod(captor.capture(), eq(PageRequest.of(0, 100)));
        assertEquals(categoryId, captor.getValue().categoryId());
        verify(model).addAttribute("items", items);
    }
}
