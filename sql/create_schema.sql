DROP TABLE job;
DROP TABLE clientorder;
DROP TABLE pallet;
DROP TABLE location;
DROP TABLE account;

CREATE TABLE account
(
    username       VARCHAR(20) PRIMARY KEY,
    password       VARCHAR(20) NOT NULL,
    account_type   VARCHAR(20) NOT NULL,
    name           VARCHAR(20),
    surname        VARCHAR(20),
    current_job_id INTEGER,
    CONSTRAINT acc_type_valid CHECK ( account_type IN ('MANAGER', 'CLIENT', 'WORKER') )
    -- todo: check only worker can have job
);

CREATE TABLE location
(
    location_id   INTEGER PRIMARY KEY,
    location_type VARCHAR(20) NOT NULL,
    path          VARCHAR(20) NOT NULL,
    CONSTRAINT loc_type_valid CHECK ( location_type IN ('SHELF', 'IN_RAMP', 'OUT_RAMP') )
    -- todo: check if shelf has max 1 item
);

CREATE TABLE pallet
(
    pallet_id   INTEGER PRIMARY KEY,
    description VARCHAR(20),
    owner       VARCHAR(20) NOT NULL,
    location_id INTEGER     NOT NULL,
    CONSTRAINT owner_fk FOREIGN KEY (owner) REFERENCES account (username),
    CONSTRAINT location_fk FOREIGN KEY (location_id) REFERENCES location (location_id)
);

-- todo: create table PalletType

CREATE TABLE clientorder
(
    order_id   INTEGER PRIMARY KEY,
    username   VARCHAR(20) NOT NULL,
    order_type VARCHAR(20) NOT NULL,
    CONSTRAINT client_fk FOREIGN KEY (username) REFERENCES account (username),
    CONSTRAINT client_type_valid CHECK ( order_type IN ('IN', 'OUT') )
    -- todo check if account is client type
);

CREATE TABLE job
(
    job_id         INTEGER PRIMARY KEY,
    destination_id INTEGER     NOT NULL,
    pallet_id      INTEGER     NOT NULL,
    order_id       INTEGER     NOT NULL,
    status         VARCHAR(20) NOT NULL,
    CONSTRAINT dest_fk FOREIGN KEY (destination_id) REFERENCES location (location_id),
    CONSTRAINT pallet_fk FOREIGN KEY (pallet_id) REFERENCES pallet (pallet_id),
    CONSTRAINT order_fk FOREIGN KEY (order_id) REFERENCES clientorder (order_id),
    CONSTRAINT status_valid CHECK ( status IN ('PLANNED', 'PENDING', 'IN_PROGRESS', 'COMPLETED') )
    -- todo check if job has max 1 worker
);
