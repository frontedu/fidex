-- Indexes to speed up tenant filtering by createdBy + status
CREATE INDEX IF NOT EXISTS idx_client_createdby_status ON client(createdBy, status);
CREATE INDEX IF NOT EXISTS idx_product_createdby_status ON product(createdBy, status);
CREATE INDEX IF NOT EXISTS idx_purchase_createdby_status ON purchase(createdBy, status);
CREATE INDEX IF NOT EXISTS idx_prize_createdby_status ON prize(createdBy, status);
