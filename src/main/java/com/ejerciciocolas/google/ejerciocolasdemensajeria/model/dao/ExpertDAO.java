package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertInfoFromBigQueryDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.Expert;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.ExpertPoint;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.OperationExpertLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExpertDAO extends JpaRepository<Expert, Long> {
}