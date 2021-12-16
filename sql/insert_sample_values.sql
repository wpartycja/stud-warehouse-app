TRUNCATE TABLE job;
TRUNCATE TABLE clientorder;
TRUNCATE TABLE pallet;
TRUNCATE TABLE location;
TRUNCATE TABLE account;

INSERT INTO z15.location (location_id, location_type, path)
VALUES (1, 'SHELF', 'A/1/2');
INSERT INTO z15.location (location_id, location_type, path)
VALUES (2, 'SHELF', 'A/1/3');
INSERT INTO z15.location (location_id, location_type, path)
VALUES (3, 'IN_RAMP', 'In ramp 1');
INSERT INTO z15.location (location_id, location_type, path)
VALUES (4, 'OUT_RAMP', 'Out ramp 1');

INSERT INTO z15.account (username, password, account_type, name, surname, current_job_id)
VALUES ('c1', 'pass', 'CLIENT', 'Karol', 'Kurka', NULL);
INSERT INTO z15.account (username, password, account_type, name, surname, current_job_id)
VALUES ('c2', 'pass', 'CLIENT', 'Karolina', NULL, NULL);
INSERT INTO z15.account (username, password, account_type, name, surname, current_job_id)
VALUES ('m1', 'pass', 'MANAGER', 'Michał', 'Malinowski', NULL);
INSERT INTO z15.account (username, password, account_type, name, surname, current_job_id)
VALUES ('m2', 'pass', 'MANAGER', 'Marcin', 'Modrzejewski', NULL);
INSERT INTO z15.account (username, password, account_type, name, surname, current_job_id)
VALUES ('w1', 'pass', 'WORKER', 'Wiesław', 'Wrona', NULL);
INSERT INTO z15.account (username, password, account_type, name, surname, current_job_id)
VALUES ('w2', 'pass', 'WORKER', 'Wojciech', 'Wysocki', NULL);

INSERT INTO z15.pallet (pallet_id, description, owner, location_id)
VALUES (2, 'Krzesła', 'c1', 2);
INSERT INTO z15.pallet (pallet_id, description, owner, location_id)
VALUES (3, 'Czekolada', 'c2', 4);
INSERT INTO z15.pallet (pallet_id, description, owner, location_id)
VALUES (4, 'Grzejnik', 'c1', 4);

INSERT INTO z15.clientorder (order_id, username, order_type)
VALUES (1, 'c1', 'IN');
INSERT INTO z15.clientorder (order_id, username, order_type)
VALUES (2, 'c2', 'IN');

INSERT INTO z15.job (job_id, destination_id, pallet_id, order_id, status)
VALUES (1, 1, 4, 1, 'PENDING');
INSERT INTO z15.job (job_id, destination_id, pallet_id, order_id, status)
VALUES (2, 2, 2, 1, 'PENDING');
INSERT INTO z15.job (job_id, destination_id, pallet_id, order_id, status)
VALUES (3, 3, 3, 2, 'PENDING');
