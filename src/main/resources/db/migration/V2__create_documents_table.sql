CREATE TABLE documents
(
    id                  UUID PRIMARY KEY,

    original_file_name  VARCHAR(500) NOT NULL,

    stored_file_name    VARCHAR(500) NOT NULL,

    mime_type           VARCHAR(255) NOT NULL,

    file_size           BIGINT NOT NULL,

    storage_path        VARCHAR(1000) NOT NULL,

    status              VARCHAR(50) NOT NULL,

    uploaded_at         TIMESTAMP NOT NULL,

    processed_at        TIMESTAMP
);

CREATE INDEX idx_documents_status
    ON documents(status);

CREATE INDEX idx_documents_uploaded_at
    ON documents(uploaded_at);