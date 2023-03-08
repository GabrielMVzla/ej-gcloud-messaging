package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.ExpertDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.ExpertPointDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.OperationExpertLogDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertInfoBigQueryDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertOperationDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.Expert;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.ExpertPoint;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.OperationExpertLog;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class OperationExpertLogService {

    private final ExpertDAO expertDAO;
    private final ExpertPointDAO expertPointDAO;
    private final OperationExpertLogDAO operationExpertLogDAO;

    public ExpertInfoBigQueryDTO saveExpertInfoOperationDBAndBQ(ExpertOperationDTO expertOperationDTO){

        Expert expert = expertDAO.findById( expertOperationDTO.getIdExpert() ).orElse(null);
        if(expert == null)
            return null;

        expertOperationDTO.setOperationType( expertOperationDTO.getOperationType().toUpperCase() );
        //Cálculo de puntos generados
        String operation = expertOperationDTO.getOperationType(); //.replace("\\u00D3", "Ó");

        //si no es abono, entonces es colocación
        int multiplicadorBono = operation.equals("ABONO") ? 2 : 1;

        //toma los puntos de la operación anterior que no lograron el punto
        long residualAmount = expertDAO.getResidualAmount(expert.getId()) % 100;

        double amountEntered = expertOperationDTO.getAmountEntered();

        //no diferencía en tipo de operación para sumar nuevos puntos
        long calcPointsEnteredAndResidual = residualAmount + ((long) amountEntered / 100);
        long finalCalcGeneratedPoints = calcPointsEnteredAndResidual * multiplicadorBono;

        long totalPointsExpert = expert.getExpertPoints().get( expert.getExpertPoints().size() - 1).getTotalPoints();

        long totalPoints = totalPointsExpert + finalCalcGeneratedPoints;

        ExpertPoint expertPoint = ExpertPoint.builder()
                .lastPointsEntered( finalCalcGeneratedPoints )
                .lastAmountEntered( expertOperationDTO.getAmountEntered() )
                .totalPoints(totalPoints)
                .expert(expert)
                .build();

        OperationExpertLog operationExpertLog = OperationExpertLog.builder()
                .operationType( expertOperationDTO.getOperationType() )
                .amountEntered( expertOperationDTO.getAmountEntered() )
                .expert(expert)
                .build();

        ExpertPoint savedExpertPoint = expertPointDAO.save(expertPoint);
        OperationExpertLog savedOperationExpertLog = operationExpertLogDAO.save(operationExpertLog);

        log.info("savedExpertPoint: {}", savedExpertPoint);
        log.info("savedOperationExpertLog: {}", savedOperationExpertLog);


        ExpertInfoBigQueryDTO expertInfo =
                ExpertInfoBigQueryDTO.builder()
                        .id(expert.getId())
                        .operationType( operationExpertLog.getOperationType() )
                        .amountEntered( operationExpertLog.getAmountEntered() )
                        .totalPoints( expertPoint.getTotalPoints() )
                        .operationDate( operationExpertLog.getOperationDate() )
                        .build();

        log.info("expertInfo: {}", expertInfo);

        return expertInfo;
    }


}
