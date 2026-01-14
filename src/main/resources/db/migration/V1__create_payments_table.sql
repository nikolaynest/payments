SELECT current_database();


CREATE TABLE IF NOT EXISTS payments (
                          id UUID PRIMARY KEY,
                          external_id VARCHAR(255) NOT NULL UNIQUE,
                          amount NUMERIC(19,2) NOT NULL,
                          status VARCHAR(32) NOT NULL,
                          created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
