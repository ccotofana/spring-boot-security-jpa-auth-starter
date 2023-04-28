-- Execute in "myschema" schema
SET search_path TO myschema;

CREATE EXTENSION IF NOT EXISTS citext;

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
	user_id BIGSERIAL UNIQUE,
	username CITEXT NOT NULL PRIMARY KEY,
	password VARCHAR(255) NOT NULL,
	account_expired BOOLEAN NOT NULL,
	account_locked BOOLEAN NOT NULL,
	enabled BOOLEAN NOT NULL,
	credentials_expired BOOLEAN NOT NULL,
	created_at TIMESTAMP WITH TIME ZONE NOT NULL,
	updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

DROP TABLE IF EXISTS user_authorities CASCADE;
CREATE TABLE IF NOT EXISTS user_authorities (
    user_id BIGINT NOT NULL,
    authority_id INTEGER NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (user_id)
        REFERENCES users (user_id)
);
