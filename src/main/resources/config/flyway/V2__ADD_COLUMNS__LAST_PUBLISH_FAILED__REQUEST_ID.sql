ALTER TABLE b2c_transaction
ADD COLUMN last_publish_failed BOOLEAN DEFAULT false;

ALTER TABLE b2c_transaction
ADD COLUMN request_id UUID NOT NULL;

ALTER TABLE b2c_transaction
ADD COLUMN remarks VARCHAR(200);