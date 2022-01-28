TRUNCATE TABLE job_history/
TRUNCATE TABLE jobs/
TRUNCATE TABLE pallets/
TRUNCATE TABLE locations/
TRUNCATE TABLE shelf_type/
TRUNCATE TABLE payments/
TRUNCATE TABLE position_history/
TRUNCATE TABLE employees/
TRUNCATE TABLE positions/
TRUNCATE TABLE order_history/
TRUNCATE TABLE orders/
TRUNCATE TABLE accounts/


INSERT INTO z15.shelf_type (shelf_type_id, max_weight, max_size, shelf_description)
VALUES (1, 200, 200, 'Small shelf');
INSERT INTO z15.shelf_type (shelf_type_id, max_weight, max_size, shelf_description)
VALUES (2, 500, 300, 'Medium shelf');
INSERT INTO z15.shelf_type (shelf_type_id, max_weight, max_size, shelf_description)
VALUES (3, 1000, 400, 'Large shelf');
INSERT INTO z15.shelf_type (shelf_type_id, max_weight, max_size, shelf_description)
VALUES (4, 10000, 1000, 'Ramp');

INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (1, 'SHELF', 'Aisle A/Rack 1/Shelf 1', 1);
INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (2, 'SHELF', 'Aisle A/Rack 1/Shelf 2', 1);
INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (3, 'SHELF', 'Aisle A/Rack 2/Shelf 1', 1);
INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (4, 'SHELF', 'Aisle A/Rack 2/Shelf 2', 1);
INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (5, 'SHELF', 'Aisle B/Rack 1/Shelf 1', 2);
INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (6, 'SHELF', 'Aisle B/Rack 1/Shelf 2', 2);
INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (7, 'SHELF', 'Aisle B/Rack 2/Shelf 1', 2);
INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (8, 'SHELF', 'Aisle B/Rack 2/Shelf 2', 2);
INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (9, 'IN_RAMP', 'Unloading ramps/Unloading ramp 1', 4);
INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (10, 'OUT_RAMP', 'Loading ramps/Loading ramp 1', 4);
INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (11, 'SHELF', 'Aisle C/Rack 1/Shelf 1', 3);
INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (12, 'SHELF', 'Aisle C/Rack 1/Shelf 2', 3);
INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (13, 'SHELF', 'Aisle C/Rack 2/Shelf 1', 3);
INSERT INTO z15.locations (location_id, type, path, shelf_type_id)
VALUES (14, 'SHELF', 'Aisle C/Rack 2/Shelf 2', 3);

INSERT INTO z15.accounts (account_username, password, type, name, surname)
VALUES ('m1', '$2a$10$cQzgg5n.svokEeE6Cwy4uuLDJY3LAidpSLD7CmGWZkt0yZ//lCkjy', 'MANAGER', 'Manager', 'Pierwszy');
INSERT INTO z15.accounts (account_username, password, type, name, surname)
VALUES ('m2', '$2a$10$Lgb/TO8BL5asgGI.aUbH6eJ6UtPaIYS83NvcQBJ5Vj/74gQd7d2Ay', 'MANAGER', 'Manager', 'Drugi');
INSERT INTO z15.accounts (account_username, password, type, name, surname)
VALUES ('c1', '$2a$10$xcUjeymQj0sFdoSZkJhpOOVam5NYyknQLreYv4LrocGiWfwXMr7GO', 'CLIENT', 'Klient', 'Pierwszy');
INSERT INTO z15.accounts (account_username, password, type, name, surname)
VALUES ('c2', '$2a$10$6p9yKMjvVYMhlmbBuDVaeOhMHWsqiIpFsl1sjYAWXyJSfP67gr0Cm', 'CLIENT', 'Klient', 'Drugi');
INSERT INTO z15.accounts (account_username, password, type, name, surname)
VALUES ('w1', '$2a$10$QNB3ksLJ5jwiXIlnc8/5M.VP5R4yfkrWskhWoW3wzPyJdyTfJIGUy', 'WORKER', 'Pracownik', 'Pierwszy');
INSERT INTO z15.accounts (account_username, password, type, name, surname)
VALUES ('w2', '$2a$10$Jvqy17S/HNJm/KzVePQp8OKJIgng.hGn5qOrEJuDQ795dLgPyVJBG', 'WORKER', 'Pracownik', 'Drugi');

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
INSERT INTO z15.pallets (pallet_id, description, owner_username, location_id)
VALUES (6, 'Lustro', 'c1', 2);
INSERT INTO z15.pallets (pallet_id, description, owner_username, location_id)
VALUES (7, 'Kaloryfer', 'c1', 3);
INSERT INTO z15.pallets (pallet_id, description, owner_username, location_id)
VALUES (8, 'Zmywarka', 'c1', 6);
INSERT INTO z15.pallets (pallet_id, description, owner_username, location_id)
VALUES (9, 'Komoda', 'c1', 12);
INSERT INTO z15.pallets (pallet_id, description, owner_username, location_id)
VALUES (10, 'Arbuzy', 'c1', 13);

INSERT INTO z15.orders (order_id, client_username, type)
VALUES (1, 'c1', 'OUT');
INSERT INTO z15.orders (order_id, client_username, type)
VALUES (2, 'c2', 'OUT');
INSERT INTO z15.orders (order_id, client_username, type)
VALUES (3, 'c2', 'IN');
INSERT INTO z15.orders (order_id, client_username, type)
VALUES (4, 'c1', 'IN');
INSERT INTO z15.orders (order_id, client_username, type)
VALUES (5, 'c1', 'OUT');
INSERT INTO z15.orders (order_id, client_username, type)
VALUES (6, 'c1', 'IN');

INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (1, 10, 1, 1, 'PENDING', NULL);
INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (2, 10, 2, 2, 'PENDING', NULL);
INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (3, 6, 5, 3, 'PENDING', NULL);
INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (4, 9, 6, 3, 'IN_PROGRESS', NULL);
INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (5, 9, 7, 4, 'PENDING', NULL);
INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (6, 10, 8, 5, 'IN_PROGRESS', NULL);
INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (7, 11, 9, 6, 'IN_PROGRESS', NULL);
INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (8, 14, 9, 6, 'PENDING', NULL);
INSERT INTO z15.jobs (job_id, destination_id, pallet_id, order_id, status, assigned_worker_username)
VALUES (9, 10, 3, 5, 'IN_PROGRESS', NULL);

INSERT INTO z15.job_history (job_hist_id, job_id, new_status, date_changed)
VALUES (1, 4, 'PENDING', '03-JAN-22');
INSERT INTO z15.job_history (job_hist_id, job_id, new_status, date_changed)
VALUES (2, 7, 'PENDING', '04-JAN-22');
INSERT INTO z15.job_history (job_hist_id, job_id, new_status, date_changed)
VALUES (3, 9, 'PENDING', '05-JAN-22');
INSERT INTO z15.job_history (job_hist_id, job_id, new_status, date_changed)
VALUES (4, 7, 'IN_PROGRESS', '05-JAN-22');
INSERT INTO z15.job_history (job_hist_id, job_id, new_status, date_changed)
VALUES (5, 9, 'IN_PROGRESS', '06-JAN-22');

INSERT INTO z15.order_history (order_history_id, order_id, date_completed, client_id)
VALUES (1, 1, '03-JAN-22', 'c1');
INSERT INTO z15.order_history (order_history_id, order_id, date_completed, client_id)
VALUES (2, 2, '04-JAN-22', 'c2');
INSERT INTO z15.order_history (order_history_id, order_id, date_completed, client_id)
VALUES (3, 3, '05-JAN-22', 'c2');
INSERT INTO z15.order_history (order_history_id, order_id, date_completed, client_id)
VALUES (4, 4, '05-JAN-22', 'c1');
INSERT INTO z15.order_history (order_history_id, order_id, date_completed, client_id)
VALUES (5, 5, '06-JAN-22', 'c1');

INSERT INTO z15.payments (payment_id, order_id, deadline, amount)
VALUES (1, 1, '10-JAN-22', 250);
INSERT INTO z15.payments (payment_id, order_id, deadline, amount)
VALUES (2, 2, '11-JAN-22', 100);
INSERT INTO z15.payments (payment_id, order_id, deadline, amount)
VALUES (3, 3, '12-JAN-22', 500);
INSERT INTO z15.payments (payment_id, order_id, deadline, amount)
VALUES (4, 4, '12-JAN-22', 200);
INSERT INTO z15.payments (payment_id, order_id, deadline, amount)
VALUES (5, 5, '13-JAN-22', 500);

INSERT INTO z15.positions (position_id, position_name, min_salary, max_salary)
VALUES (1, 'Main Manager', 5000, 10000);
INSERT INTO z15.positions (position_id, position_name, min_salary, max_salary)
VALUES (2, 'Manager', 3500, 6000);
INSERT INTO z15.positions (position_id, position_name, min_salary, max_salary)
VALUES (3, 'Worker', 2500, 3500);
INSERT INTO z15.positions (position_id, position_name, min_salary, max_salary)
VALUES (4, 'Internship', 0, 1500);

INSERT INTO z15.employees (employee_id, account_username, salary, date_employed, position_id, manager_id)
VALUES (1, 'm1', 5400, '01-JAN-22', 1, NULL);
INSERT INTO z15.employees (employee_id, account_username, salary, date_employed, position_id, manager_id)
VALUES (2, 'm2', 4600, '01-JAN-22', 2, NULL);
INSERT INTO z15.employees (employee_id, account_username, salary, date_employed, position_id, manager_id)
VALUES (3, 'w1', 3100, '03-JAN-22', 3, 1);
INSERT INTO z15.employees (employee_id, account_username, salary, date_employed, position_id, manager_id)
VALUES (4, 'w2', 3000, '05-JAN-22', 3, 2);

INSERT INTO z15.position_history (ph_id, employee_id, position_id, date_start, date_end)
VALUES (1, 2, 3, '01-JAN-22', '03-JAN-22');
INSERT INTO z15.position_history (ph_id, employee_id, position_id, date_start, date_end)
VALUES (2, 4, 4, '05-JAN-22', '10-JAN-22');