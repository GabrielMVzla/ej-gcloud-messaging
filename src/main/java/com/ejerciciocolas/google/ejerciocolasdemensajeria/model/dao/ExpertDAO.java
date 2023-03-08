package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpertDAO extends JpaRepository<Expert, Long> {


    @Query("SELECT SUM( ep.lastAmountEntered ) FROM Expert e " +
            "INNER JOIN e.expertPoints ep " +
            "ON e.id = ep.expert.id " +
            "AND e.id = ?1"
    )
    long getResidualAmount(long idExpert);

}