-- Optimization indexes for createdBy filters
CREATE INDEX IF NOT EXISTS idx_client_created_by ON client(createdBy);
CREATE INDEX IF NOT EXISTS idx_product_created_by ON product(createdBy);
CREATE INDEX IF NOT EXISTS idx_prize_created_by ON prize(createdBy);
CREATE INDEX IF NOT EXISTS idx_purchase_created_by ON purchase(createdBy);

-- Composite indexes for common dashboard/list queries
CREATE INDEX IF NOT EXISTS idx_client_user_status ON client(createdBy, status);
CREATE INDEX IF NOT EXISTS idx_prize_user_status ON prize(createdBy, status);
CREATE INDEX IF NOT EXISTS idx_purchase_user_status ON purchase(createdBy, status);
