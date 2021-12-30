TRUNCATE TABLE jobs;
TRUNCATE TABLE orders;
TRUNCATE TABLE pallets;
TRUNCATE TABLE locations;
TRUNCATE TABLE accounts;

INSERT INTO z15.locations (location_id, type, path)
VALUES (1, 'SHELF', 'Aisle A/Rack 1/Shelf 1');
INSERT INTO z15.locations (location_id, type, path)
VALUES (2, 'SHELF', 'Aisle A/Rack 1/Shelf 2');
INSERT INTO z15.locations (location_id, type, path)
VALUES (3, 'SHELF', 'Aisle A/Rack 2/Shelf 1');
INSERT INTO z15.locations (location_id, type, path)
VALUES (4, 'SHELF', 'Aisle A/Rack 2/Shelf 2');
INSERT INTO z15.locations (location_id, type, path)
VALUES (5, 'SHELF', 'Aisle B/Rack 1/Shelf 1');
INSERT INTO z15.locations (location_id, type, path)
VALUES (6, 'SHELF', 'Aisle B/Rack 1/Shelf 2');
INSERT INTO z15.locations (location_id, type, path)
VALUES (7, 'SHELF', 'Aisle B/Rack 2/Shelf 1');
INSERT INTO z15.locations (location_id, type, path)
VALUES (8, 'SHELF', 'Aisle B/Rack 2/Shelf 2');
INSERT INTO z15.locations (location_id, type, path)
VALUES (9, 'IN_RAMP', 'Unloading ramps/Unloading ramp 1');
INSERT INTO z15.locations (location_id, type, path)
VALUES (10, 'OUT_RAMP', 'Loading ramps/Loading ramp 1');

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
VALUES (1, 'Krzesła', 'c1', 2);
INSERT INTO z15.pallets (pallet_id, description, owner_username, location_id)
VALUES (2, 'Czekolada', 'c2', 4);
INSERT INTO z15.pallets (pallet_id, description, owner_username, location_id)
VALUES (3, 'Grzejnik', 'c1', 7);
INSERT INTO z15.pallets (pallet_id, description, owner_username, location_id)
VALUES (4, 'Ryż', 'c2', 10);
INSERT INTO z15.pallets (pallet_id, description, owner_username, location_id)
VALUES (5, 'Sprzęt elektroniczny', 'c2', 10);

INSERT INTO z15.orders (order_id, client_username, type)
VALUES (1, 'c1', 'OUT');
INSERT INTO z15.orders (order_id, client_username, type)
VALUES (2, 'c2', 'OUT');
INSERT INTO z15.orders (order_id, client_username, type)
VALUES (3, 'c2', 'IN');

INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (1, 10, 1, 1, 'PENDING', NULL);
INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (2, 10, 2, 2, 'PENDING', NULL);
INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (3, 6, 5, 3, 'PENDING', NULL);
