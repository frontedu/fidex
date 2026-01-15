-- Migration to add cashback to usuario and points to purchase
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS cashback DOUBLE PRECISION;
ALTER TABLE purchase ADD COLUMN IF NOT EXISTS points INTEGER;
