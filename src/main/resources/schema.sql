
--------------------Expertas----------------
DROP TABLE IF EXISTS experts;

CREATE TABLE experts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS experts_points;

CREATE TABLE experts_points (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  last_points_entered BIGINT NOT NULL,
  total_points BIGINT NOT NULL,

  id_expert BIGINT REFERENCES experts(id)
);

DROP TABLE IF EXISTS operations_experts_log;

CREATE TABLE operations_experts_log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  operation_type VARCHAR(20) NOT NULL,
  amount_entered NUMERIC(10,2) NOT NULL,
  operation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  id_expert BIGINT REFERENCES experts(id)
);



