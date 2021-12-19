TRUNCATE TABLE jobs;
TRUNCATE TABLE orders;
TRUNCATE TABLE pallets;
TRUNCATE TABLE locations;
TRUNCATE TABLE accounts;

INSERT INTO z15.locations (location_id, type, path)
VALUES (1, 'SHELF', 'A/1/2');
INSERT INTO z15.locations (location_id, type, path)
VALUES (2, 'SHELF', 'A/1/3');
INSERT INTO z15.locations (location_id, type, path)
VALUES (3, 'IN_RAMP', 'In ramp 1');
INSERT INTO z15.locations (location_id, type, path)
VALUES (4, 'OUT_RAMP', 'Out ramp 1');

INSERT INTO z15.accounts (account_username, password, type, name, surname)
VALUES ('c1', 'pass', 'CLIENT', 'Karol', 'Kurka');
INSERT INTO z15.accounts (account_username, password, type, name, surname)
VALUES ('c2', 'pass', 'CLIENT', 'Karolina', NULL);
INSERT INTO z15.accounts (account_username, password, type, name, surname)
VALUES ('m1', 'pass', 'MANAGER', 'Michał', 'Malinowski');
INSERT INTO z15.accounts (account_username, password, type, name, surname)
VALUES ('m2', 'pass', 'MANAGER', 'Marcin', 'Modrzejewski');
INSERT INTO z15.accounts (account_username, password, type, name, surname)
VALUES ('w1', 'pass', 'WORKER', 'Wiesław', 'Wrona');
INSERT INTO z15.accounts (account_username, password, type, name, surname)
VALUES ('w2', 'pass', 'WORKER', 'Wojciech', 'Wysocki');

INSERT INTO z15.pallets (pallet_id, description, owner_username, location_id)
VALUES (2, 'Krzesła', 'c1', 2);
INSERT INTO z15.pallets (pallet_id, description, owner_username, location_id)
VALUES (3, 'Czekolada', 'c2', 4);
INSERT INTO z15.pallets (pallet_id, description, owner_username, location_id)
VALUES (4, 'Grzejnik', 'c1', 4);

INSERT INTO z15.orders (order_id, client_username, type)
VALUES (1, 'c1', 'IN');
INSERT INTO z15.orders (order_id, client_username, type)
VALUES (2, 'c2', 'IN');

INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (1, 1, 4, 1, 'PENDING', NULL);
INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (2, 2, 2, 1, 'PENDING', NULL);
INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (3, 3, 3, 2, 'PENDING', NULL);
