package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.ExpertPoint;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.OperationExpertLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertPointDAO extends JpaRepository<ExpertPoint, Long> {
}
