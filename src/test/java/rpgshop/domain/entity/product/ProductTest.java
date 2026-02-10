package rpgshop.domain.entity.product;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rpgshop.domain.entity.product.constant.StatusChangeCategory.DISCONTINUED;
import static rpgshop.domain.entity.product.constant.StatusChangeCategory.OUT_OF_MARKET;
import static rpgshop.domain.entity.product.constant.StatusChangeCategory.RESTOCKED;

class ProductTest {
    @Test
    void shouldCreateProduct() {
        final UUID id = UUID.randomUUID();
        final String name = "D&D Player's Handbook";
        final ProductType productType = ProductType.builder().name("Book").build();
        final List<Category> categories = List.of();
        final BigDecimal height = new BigDecimal("28.0");
        final BigDecimal width = new BigDecimal("22.0");
        final BigDecimal depth = new BigDecimal("2.5");
        final BigDecimal weight = new BigDecimal("1.2");
        final PricingGroup pricingGroup = PricingGroup.builder().name("Standard").build();
        final String barcode = "1234567890123";
        final String sku = "RPG-001";
        final BigDecimal salePrice = new BigDecimal("199.90");
        final BigDecimal costPrice = new BigDecimal("100.00");
        final Integer stockQuantity = 50;
        final List<StatusChange> statusChanges = new ArrayList<>();
        final BigDecimal minimumSaleThreshold = new BigDecimal("5");
        final Instant createdAt = Instant.now();
        final Instant updatedAt = Instant.now();

        final Product product = Product.builder()
            .id(id)
            .name(name)
            .productType(productType)
            .categories(categories)
            .height(height)
            .width(width)
            .depth(depth)
            .weight(weight)
            .pricingGroup(pricingGroup)
            .barcode(barcode)
            .sku(sku)
            .salePrice(salePrice)
            .costPrice(costPrice)
            .stockQuantity(stockQuantity)
            .statusChanges(statusChanges)
            .minimumSaleThreshold(minimumSaleThreshold)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();

        assertEquals(id, product.id());
        assertEquals(name, product.name());
        assertEquals(productType, product.productType());
        assertEquals(categories, product.categories());
        assertEquals(height, product.height());
        assertEquals(width, product.width());
        assertEquals(depth, product.depth());
        assertEquals(weight, product.weight());
        assertEquals(pricingGroup, product.pricingGroup());
        assertEquals(barcode, product.barcode());
        assertEquals(sku, product.sku());
        assertEquals(salePrice, product.salePrice());
        assertEquals(costPrice, product.costPrice());
        assertEquals(stockQuantity, product.stockQuantity());
        assertEquals(statusChanges, product.statusChanges());
        assertEquals(minimumSaleThreshold, product.minimumSaleThreshold());
        assertEquals(createdAt, product.createdAt());
        assertEquals(updatedAt, product.updatedAt());
    }

    @Test
    void shouldCreateProductWithNullValues() {
        final Product product = Product.builder().build();

        assertNull(product.id());
        assertNull(product.name());
        assertNull(product.productType());
        assertNull(product.categories());
        assertNull(product.height());
        assertNull(product.width());
        assertNull(product.depth());
        assertNull(product.weight());
        assertNull(product.pricingGroup());
        assertNull(product.barcode());
        assertNull(product.sku());
        assertNull(product.salePrice());
        assertNull(product.costPrice());
        assertNull(product.stockQuantity());
        assertNull(product.statusChanges());
        assertNull(product.minimumSaleThreshold());
        assertNull(product.createdAt());
        assertNull(product.updatedAt());
    }

    @Test
    void shouldReturnTrueForIsActiveWhenStatusChangesIsNull() {
        final Product product = Product.builder().statusChanges(null).build();
        assertTrue(product.isActive());
    }

    @Test
    void shouldReturnTrueForIsActiveWhenStatusChangesIsEmpty() {
        final Product product = Product.builder().statusChanges(new ArrayList<>()).build();
        assertTrue(product.isActive());
    }

    @Test
    void shouldReturnTrueForIsActiveWhenLastStatusIsActivate() {
        final List<StatusChange> statusChanges = new ArrayList<>();
        statusChanges.add(StatusChange.deactivate("Out of stock", OUT_OF_MARKET));
        statusChanges.add(StatusChange.activate("Restocked", RESTOCKED));

        final Product product = Product.builder().statusChanges(statusChanges).build();
        assertTrue(product.isActive());
    }

    @Test
    void shouldReturnFalseForIsActiveWhenLastStatusIsDeactivate() {
        final List<StatusChange> statusChanges = new ArrayList<>();
        statusChanges.add(StatusChange.activate("Initial", RESTOCKED));
        statusChanges.add(StatusChange.deactivate("Discontinued", DISCONTINUED));

        final Product product = Product.builder().statusChanges(statusChanges).build();
        assertFalse(product.isActive());
    }

    @Test
    void shouldActivateProduct() {
        final List<StatusChange> statusChanges = new ArrayList<>();
        statusChanges.add(StatusChange.deactivate("Out of stock", OUT_OF_MARKET));

        final Product product = Product.builder().statusChanges(statusChanges).build();
        final Product activatedProduct = product.activate("Restocked", RESTOCKED);

        assertFalse(product.isActive());
        assertTrue(activatedProduct.isActive());
        assertEquals(2, activatedProduct.statusChanges().size());
    }

    @Test
    void shouldDeactivateProduct() {
        final List<StatusChange> statusChanges = new ArrayList<>();
        statusChanges.add(StatusChange.activate("Initial", RESTOCKED));

        final Product product = Product.builder().statusChanges(statusChanges).build();
        final Product deactivatedProduct = product.deactivate("Discontinued", DISCONTINUED);

        assertTrue(product.isActive());
        assertFalse(deactivatedProduct.isActive());
        assertEquals(2, deactivatedProduct.statusChanges().size());
    }

    @Test
    void shouldUseToBuilder() {
        final String originalName = "D&D Player's Handbook";
        final String newName = "D&D Dungeon Master's Guide";

        final Product original = Product.builder().name(originalName).build();
        final Product modified = original.toBuilder().name(newName).build();

        assertEquals(originalName, original.name());
        assertEquals(newName, modified.name());
    }
}

