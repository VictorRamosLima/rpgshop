CREATE INDEX IF NOT EXISTS transaction_logs_timestamp_brin
ON transaction_logs
USING brin (transaction_logs.timestamp);
