-- =====================================================
-- INDEXES FOR RPG SHOP DATABASE
-- =====================================================

-- =============================================================================
-- TRANSACTION LOGS
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_transaction_logs_timestamp_brin
    ON transaction_logs USING brin (timestamp);

CREATE INDEX IF NOT EXISTS idx_transaction_logs_entity_name_entity_id
    ON transaction_logs (entity_name, entity_id);

CREATE INDEX IF NOT EXISTS idx_transaction_logs_operation
    ON transaction_logs (operation);

CREATE INDEX IF NOT EXISTS idx_transaction_logs_responsible_user
    ON transaction_logs (responsible_user);

-- =============================================================================
-- CARTS
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_carts_customer_id
    ON carts (customer_id);

-- =============================================================================
-- CART ITEMS
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_cart_items_cart_id
    ON cart_items (cart_id);

CREATE INDEX IF NOT EXISTS idx_cart_items_cart_id_product_id
    ON cart_items (cart_id, product_id);

CREATE INDEX IF NOT EXISTS idx_cart_items_blocked_expires
    ON cart_items (is_blocked, expires_at)
    WHERE is_blocked = true AND expires_at IS NOT NULL;

CREATE INDEX IF NOT EXISTS idx_cart_items_product_id_is_blocked
    ON cart_items (product_id, is_blocked)
    WHERE is_blocked = true;

-- =============================================================================
-- COUPONS
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_coupons_customer_id_is_used_expires_at
    ON coupons (customer_id, is_used, expires_at);

CREATE INDEX IF NOT EXISTS idx_coupons_customer_id_type_is_used
    ON coupons (customer_id, type, is_used);

-- =============================================================================
-- CUSTOMERS
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_customers_is_active
    ON customers (is_active);

CREATE INDEX IF NOT EXISTS idx_customers_gender
    ON customers (gender);

CREATE INDEX IF NOT EXISTS idx_customers_name_lower
    ON customers (LOWER(name));

-- =============================================================================
-- ADDRESSES
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_addresses_customer_id_is_active
    ON addresses (customer_id, is_active);

CREATE INDEX IF NOT EXISTS idx_addresses_customer_id_purpose_is_active
    ON addresses (customer_id, purpose, is_active);

-- =============================================================================
-- CREDIT CARDS
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_credit_cards_customer_id_is_active
    ON credit_cards (customer_id, is_active);

CREATE INDEX IF NOT EXISTS idx_credit_cards_customer_id_is_preferred
    ON credit_cards (customer_id, is_preferred);

CREATE INDEX IF NOT EXISTS idx_credit_cards_customer_id_card_number
    ON credit_cards (customer_id, card_number);

-- =============================================================================
-- PHONES
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_phones_customer_id_is_active
    ON phones (customer_id, is_active);

CREATE INDEX IF NOT EXISTS idx_phones_customer_id_area_code_number
    ON phones (customer_id, area_code, number);

-- =============================================================================
-- EXCHANGE REQUESTS
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_exchange_requests_status
    ON exchange_requests (status);

CREATE INDEX IF NOT EXISTS idx_exchange_requests_order_id
    ON exchange_requests (order_id);

CREATE INDEX IF NOT EXISTS idx_exchange_requests_order_item_id_status
    ON exchange_requests (order_item_id, status);

-- =============================================================================
-- ORDERS
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_orders_customer_id
    ON orders (customer_id);

CREATE INDEX IF NOT EXISTS idx_orders_status
    ON orders (status);

CREATE INDEX IF NOT EXISTS idx_orders_status_purchased_at
    ON orders (status, purchased_at)
    WHERE status != 'REJECTED';

CREATE INDEX IF NOT EXISTS idx_orders_customer_id_purchased_at
    ON orders (customer_id, purchased_at);

-- =============================================================================
-- ORDER ITEMS
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_order_items_order_id
    ON order_items (order_id);

CREATE INDEX IF NOT EXISTS idx_order_items_product_id
    ON order_items (product_id);

-- =============================================================================
-- ORDER PAYMENTS
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_order_payments_order_id
    ON order_payments (order_id);

CREATE INDEX IF NOT EXISTS idx_order_payments_coupon_id
    ON order_payments (coupon_id)
    WHERE coupon_id IS NOT NULL;

-- =============================================================================
-- PRODUCTS
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_products_is_active
    ON products (is_active);

CREATE INDEX IF NOT EXISTS idx_products_type_id
    ON products (type_id);

CREATE INDEX IF NOT EXISTS idx_products_pricing_group_id
    ON products (pricing_group_id);

CREATE INDEX IF NOT EXISTS idx_products_sale_price
    ON products (sale_price);

CREATE INDEX IF NOT EXISTS idx_products_name_lower
    ON products (LOWER(name));

CREATE INDEX IF NOT EXISTS idx_products_auto_inactivation
    ON products (is_active, stock_quantity, sale_price)
    WHERE is_active = true AND stock_quantity = 0;

-- =============================================================================
-- PRODUCT CATEGORIES (Join Table)
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_product_categories_category_id
    ON product_categories (category_id);

CREATE INDEX IF NOT EXISTS idx_product_categories_product_id
    ON product_categories (product_id);

-- =============================================================================
-- STATUS CHANGES
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_status_changes_product_id
    ON status_changes (product_id);

-- =============================================================================
-- STOCK ENTRIES
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_stock_entries_product_id
    ON stock_entries (product_id);

-- =============================================================================
-- SUPPLIERS
-- =============================================================================
CREATE INDEX IF NOT EXISTS idx_suppliers_is_active
    ON suppliers (is_active);
