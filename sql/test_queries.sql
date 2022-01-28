
-- WYZWALACZE

-- trigger order_history_add
SAVEPOINT before_test;

SELECT *
FROM order_history;

BEGIN
    FOR job IN (SELECT * FROM jobs WHERE jobs.order_id = 2)
        LOOP
            complete_job(job.job_id);
        END LOOP;
END;

SELECT *
FROM order_history;

ROLLBACK TO before_test;

-- trigger check_shelf_has_one_pallet
SAVEPOINT before_test;
DECLARE
    expected EXCEPTION;
    PRAGMA EXCEPTION_INIT (expected, -20000);
BEGIN
    -- should not throw
    UPDATE jobs SET jobs.assigned_worker_username = 'w1' WHERE job_id = 2;
    -- should throw
    UPDATE jobs SET jobs.assigned_worker_username = 'c1' WHERE job_id = 2;
EXCEPTION
    WHEN expected THEN dbms_output.put_line('test 2 passed');
    WHEN OTHERS THEN RAISE ;
END;
ROLLBACK TO before_test;

-- trigger check_client_assigned_to_order
SAVEPOINT before_test;
DECLARE
    expected EXCEPTION;
    PRAGMA EXCEPTION_INIT (expected, -20001);
BEGIN
    -- should not throw
    UPDATE orders SET orders.client_username = 'c1' WHERE order_id = 2;
    -- should throw
    UPDATE orders SET orders.client_username = 'w1' WHERE order_id = 2;
EXCEPTION
    WHEN expected THEN dbms_output.put_line('test 3 passed');
    WHEN OTHERS THEN RAISE ;
END;
ROLLBACK TO before_test;

SAVEPOINT before_test;

-- FUNKCJE

-- Dla każdego zamówienia wypisz imię i nazwisko zamawiajacego,
-- oraz caość do zaplacenia za dane zamówienie
SELECT o.order_id              AS order_id,
       a.name                  AS client_name,
       a.surname               AS client_surname,
       order_price(o.order_id) AS order_price
FROM orders o
         LEFT JOIN accounts a ON (o.client_username = a.account_username);

-- Dla każdego klienta wypisz sumę jaka ma do zaplacenia
SELECT a.account_username AS login,
       a.name             AS client_name,
       a.surname          AS client_surname,
       SUM(p.amount)      AS total_amount
FROM accounts a
         JOIN orders o ON (a.account_username = o.client_username)
         JOIN payments p USING (order_id)
GROUP BY a.account_username, a.name, a.surname;

-- Dla każdego managera wypisz bonus jaki on otrzymuje
SELECT a.name                       AS man_name,
       a.surname                    AS man_surname,
       manager_bonus(e.employee_id) AS bonus
FROM accounts a
         LEFT JOIN employees e USING (account_username)
WHERE a.type LIKE 'MANAGER';


-- PROCEDURY

-- create_job, create_job_out
SELECT *
FROM jobs;
SELECT *
FROM orders;
SELECT *
FROM pallets;

EXEC create_job('c1', 'IN', 'Pomidory', 'PLANNED');
EXEC create_job_out('c1', 'OUT', 1, 'PLANNED');

SELECT *
FROM jobs;
SELECT *
FROM orders;
SELECT *
FROM pallets;

-- assign_worker, unassign_worker
SELECT *
FROM jobs;
EXEC assign_worker('w1', 7);
SELECT *
FROM jobs;
EXEC unasssign_worker(7);
SELECT *
FROM jobs;

ROLLBACK TO before_test;

-- complete_job -> zmienia status w Jobie na 'COMPLETED',
-- zmienia przypisanego workera na NULL, zmienia lokacje palety na docelowa
SAVEPOINT before_test_complete_job;

SELECT j.*, p.location_id
FROM jobs j
         JOIN pallets p ON (p.pallet_id = j.pallet_id)
WHERE job_id = 1;

EXEC complete_job(1);

SELECT j.*, p.location_id
FROM jobs j
         JOIN pallets p ON (p.pallet_id = j.pallet_id)
WHERE job_id = 1;

ROLLBACK TO before_test_complete_job;

-- procedura wypisania sumy jaka  dany klient ma do zaplacenia
EXEC show_clients_payments;

-- PRZYKŁADOWE ZAPYTANIA

-- Wyświetl wszystkie palety w alejce B
SELECT p.description, l.path
FROM pallets p
         JOIN locations l USING (location_id)
WHERE l.path LIKE 'Aisle B%';

-- Wyświetl klientów i liczbę palet które posiada w magazynie
SELECT a.account_username, COUNT(p.pallet_id) AS number_of_pallets
FROM accounts a
         JOIN pallets p ON (a.account_username = p.owner_username)
WHERE a.type = 'CLIENT'
GROUP BY a.account_username;
