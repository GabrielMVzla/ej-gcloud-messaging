INSERT INTO experts (first_name, last_name) values
('Carl', 'Igoe'),
('Valentin', 'Skaife d'),
('Amber', 'Grim'),
('Antonio', 'Luffman'),
('Erma', 'Antwis');

INSERT INTO experts_points (last_points_entered, total_points, id_expert) values
(50, 500, 1),
(60, 560, 3),
(10, 200, 2);

INSERT INTO operations_experts_log (operation_type, amount_entered, id_expert) values
('COLOCACI\u00D3N', 45000.0, 1),
('ABONO', 5000.00, 1),
('COLOCACI\u00D3N', 50000.0, 3),
('ABONO', 6000.00, 3),
('COLOCACI\u00D3N', 19000.0, 2),
('ABONO', 1000.0, 2);
