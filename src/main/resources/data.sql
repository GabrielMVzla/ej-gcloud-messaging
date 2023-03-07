INSERT INTO experts (first_name, last_name) values
('Carl', 'Igoe'),
('Valentin', 'Skaife d'),
('Amber', 'Grim'),
('Antonio', 'Luffman'),
('Erma', 'Antwis');

INSERT INTO experts_points (last_points_entered, total_points, id_experts) values
(50, 500, 1),
(60, 560, 3),
(10, 200, 2);

INSERT INTO operations_experts_log (operation_type, amount_entered, id_experts) values
('COLOCACI\u00D3N', 45000.00, 1),
('ABONO', 5000.00, 1),
('COLOCACI\u00D3N', 50000.00, 3),
('ABONO', 6000.00, 3),
('COLOCACI\u00D3N', 19000.00, 2),
('ABONO', 1000.00, 2);
