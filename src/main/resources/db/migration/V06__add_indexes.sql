-- Performance indexes for FIDEX

-- Indexes for Client table
CREATE INDEX IF NOT EXISTS idx_client_status ON client(status);
CREATE INDEX IF NOT EXISTS idx_client_name ON client(name);
CREATE INDEX IF NOT EXISTS idx_client_points ON client(points);

-- Indexes for Purchase table
CREATE INDEX IF NOT EXISTS idx_purchase_status ON purchase(status);
CREATE INDEX IF NOT EXISTS idx_purchase_date ON purchase(date);
CREATE INDEX IF NOT EXISTS idx_purchase_client_id ON purchase(client_id);

-- Indexes for Product table
CREATE INDEX IF NOT EXISTS idx_product_status ON product(status);

-- Indexes for Prize table
CREATE INDEX IF NOT EXISTS idx_prize_status ON prize(status);

-- Index for Usuario table (login queries)
CREATE INDEX IF NOT EXISTS idx_usuario_nome_usuario ON usuario(nome_usuario);
