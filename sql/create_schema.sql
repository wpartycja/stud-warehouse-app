DROP TABLE jobs CASCADE CONSTRAINTS;
DROP TABLE orders CASCADE CONSTRAINTS;
DROP TABLE pallets CASCADE CONSTRAINTS;
DROP TABLE accounts CASCADE CONSTRAINTS;
DROP TABLE locations CASCADE CONSTRAINTS;


CREATE TABLE locations
(
    location_id INTEGER PRIMARY KEY,
    type        VARCHAR(255) NOT NULL
        CONSTRAINT location_type_enum CHECK ( type IN ('SHELF', 'IN_RAMP', 'OUT_RAMP') ),
    path        VARCHAR(255) NOT NULL
    -- todo: check if shelf has max 1 item
);

CREATE TABLE accounts
(
    account_username VARCHAR(255) PRIMARY KEY,
    password         VARCHAR(255) NOT NULL,
    type             VARCHAR(255) NOT NULL
        CONSTRAINT account_type_enum CHECK ( type IN ('MANAGER', 'CLIENT', 'WORKER') ),
    name             VARCHAR(255),
    surname          VARCHAR(255)
);

CREATE TABLE pallets
(
    pallet_id      INTEGER PRIMARY KEY,
    description    VARCHAR(255),
    owner_username VARCHAR(255) NOT NULL
        CONSTRAINT pal_own_fk REFERENCES accounts (account_username),
    location_id    INTEGER     NOT NULL
        CONSTRAINT location_fk REFERENCES locations (location_id)
);

-- todo: create table PalletType

CREATE TABLE orders
(
    order_id        INTEGER PRIMARY KEY,
    client_username VARCHAR(255) NOT NULL
        CONSTRAINT order_client_fk REFERENCES accounts (account_username),
    type            VARCHAR(255) NOT NULL
        CONSTRAINT client_type_enum CHECK ( type IN ('IN', 'OUT') )
    -- todo check if account is client type
);

CREATE TABLE jobs
(
    job_id                   INTEGER PRIMARY KEY,
    destination_id           INTEGER     NOT NULL
        CONSTRAINT job_destination_fk REFERENCES locations (location_id),
    pallet_id                INTEGER     NOT NULL
        CONSTRAINT job_pallet_fk REFERENCES pallets (pallet_id),
    order_id                 INTEGER     NOT NULL
        CONSTRAINT job_order_fk REFERENCES orders (order_id),
    status                   VARCHAR(255) NOT NULL
        CONSTRAINT job_status_enum CHECK ( status IN ('PLANNED', 'PENDING', 'IN_PROGRESS', 'COMPLETED') ),
    assigned_worker_username VARCHAR(255)
        CONSTRAINT job_assigned_worker_fk REFERENCES accounts (account_username)
    -- todo: check only worker can have job
);
