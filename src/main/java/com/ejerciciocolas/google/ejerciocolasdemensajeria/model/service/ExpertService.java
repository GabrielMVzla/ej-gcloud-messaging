package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.ExpertDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertInfoFromBigQueryDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.Expert;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
//@AllArgsConstructor
public class ExpertService {

    @Autowired
    private ExpertDAO expertDAO;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<ExpertInfoFromBigQueryDTO> getExpertInfoFromBigQuery(){

        /*Expert expert = Expert.builder()
                .firstName("gabriel")
                .lastName("montes")
                .build();
        expertDAO.save(expert);
        List<Expert> experts = expertDAO.findAll();
        */

        Expert expert = expertDAO.findById(1L).orElse(null);

        log.info("toString: {}", expert.toString() != null ? expert.toString() : null);
        log.info("ExpertPoints: {}", expert.getExpertPoints());
        log.info("OperationExpertLogs: {}", expert.getOperationExpertLogs());

        ExpertInfoFromBigQueryDTO expertInfoFromBigQueryDTO = ExpertInfoFromBigQueryDTO.builder()
                .id( expert.getId() )
                .operationType( expert.getOperationExpertLogs().get(0).getOperationType() )
                .amountEntered( expert.getOperationExpertLogs().get(0).getAmountEntered() )
                .totalPoints( expert.getExpertPoints().get(0).getTotalPoints() )
                .operationDate( expert.getOperationExpertLogs().get(0).getOperationDate() )
                .build();

        log.info( "expertInfoFromBigQueryDTO: {}", expertInfoFromBigQueryDTO.toString() );
         //experts.forEach(m -> System.out.println("Service " + m.toString()));

        /*
        List<ExpertInfoFromBigQueryDTO> expertsInfoFBQ = expertDAO.findExpertInfoFromBigQuery();
        * */
        return null;
    }
}
