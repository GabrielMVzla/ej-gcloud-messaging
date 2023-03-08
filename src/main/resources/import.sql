--------------------Expertas----------------
--DROP TABLE IF EXISTS experts;

--CREATE TABLE experts (
--  id BIGINT AUTO_INCREMENT PRIMARY KEY,
--  first_name VARCHAR(100) NOT NULL,
--  last_name VARCHAR(100) NOT NULL
  --created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  --updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
--);

--DROP TABLE IF EXISTS experts_points;

--CREATE TABLE experts_points (
--  id BIGINT AUTO_INCREMENT PRIMARY KEY,
--  last_points_entered BIGINT NOT NULL,
--  total_points BIGINT NOT NULL,
--
--  id_expert BIGINT REFERENCES experts(id)
--);

--DROP TABLE IF EXISTS operations_experts_log;

--CREATE TABLE operations_experts_log (
--  id BIGINT AUTO_INCREMENT PRIMARY KEY,
--  operation_type VARCHAR(20) NOT NULL,
--  amount_entered NUMERIC(10,2) NOT NULL,
--  operation_date DATE NOT NULL ,
--
--  id_expert BIGINT REFERENCES experts(id)
--);

--------------------------INSERTS------------------------------------
INSERT INTO experts (first_name, last_name) VALUES('Carl', 'Igoe');
INSERT INTO experts (first_name, last_name) VALUES('Valentin', 'Skaife d');
INSERT INTO experts (first_name, last_name) VALUES('Amber', 'Grim');
INSERT INTO experts (first_name, last_name) VALUES('Antonio', 'Luffman');
INSERT INTO experts (first_name, last_name) VALUES('Erma', 'Antwis');

INSERT INTO experts_points (last_points_entered, last_amount_entered, total_points, id_expert) VALUES(50, 5000.00, 500, 1);
INSERT INTO experts_points (last_points_entered, last_amount_entered, total_points, id_expert) VALUES(60, 1000.00, 560, 3);
INSERT INTO experts_points (last_points_entered, last_amount_entered, total_points, id_expert) VALUES(10, 6000.00, 200, 2);

INSERT INTO operations_experts_log (operation_type, amount_entered, operation_date, id_expert) VALUES('COLOCACI\u00D3N', 45000.00, '2022-08-23 12:00:00', 1);
INSERT INTO operations_experts_log (operation_type, amount_entered, operation_date, id_expert) VALUES('ABONO', 5000.00, '2022-08-23 12:00:00', 1);
INSERT INTO operations_experts_log (operation_type, amount_entered, operation_date, id_expert) VALUES('COLOCACI\u00D3N', 50000.00, '2022-08-23 12:00:00', 3);
INSERT INTO operations_experts_log (operation_type, amount_entered, operation_date, id_expert) VALUES('ABONO', 6000.00, '2022-08-23 12:00:00', 3);
INSERT INTO operations_experts_log (operation_type, amount_entered, operation_date, id_expert) VALUES('COLOCACI\u00D3N', 19000.00, '2022-08-23 12:00:00', 2);
INSERT INTO operations_experts_log (operation_type, amount_entered, operation_date, id_expert) VALUES('ABONO', 1000.00, '2022-08-23 12:00:00', 2);