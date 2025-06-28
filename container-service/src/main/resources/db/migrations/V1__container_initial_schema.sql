-- Table: containers
CREATE TABLE IF NOT EXISTS containers (
    container_id VARCHAR(50) PRIMARY KEY,
    client_id VARCHAR(50) NOT NULL,        -- Logical FK to Customer Service
    booking_id VARCHAR(50),                -- Logical FK to Booking Service, NULLABLE
    associated_order_ids_json JSON       -- JSON array of associated order IDs (or TEXT if H2 < 1.4.200)
);