package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.ExpertDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertInfoFromBigQueryDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.Expert;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.ExpertPoint;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.OperationExpertLog;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class ExpertService {

    private final ExpertDAO expertDAO;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<ExpertInfoFromBigQueryDTO> getExpertInfoToSendBigQuery(){

        List<Expert> experts = expertDAO.findAll();
        if(experts.isEmpty())
            return Collections.emptyList();

        List<ExpertInfoFromBigQueryDTO> expertsInfo = new ArrayList<>();
        Map<Long, List<OperationExpertLog>> mapOperationExpertLogs = new HashMap<>();

        for (Expert expert : experts) {
            long id = expert.getId(); //id del expert

            if (expert.getOperationExpertLogs().isEmpty()) { //si no cuenta con operación, pasa a sig. iteración
                continue;
            }

            mapOperationExpertLogs.put(expert.getId(), expert.getOperationExpertLogs());

            if (!expert.getExpertPoints().isEmpty()) { //si es vacío quiere decir que aún no tiene puntos/movimientos
                int sizeExp = expert.getExpertPoints().size() - 1;
                ExpertPoint expertPoint = expert.getExpertPoints().get(sizeExp); //toma el último, es decir, para tomar cantidad actual

                long totalPointsmapExpertPoints = expertPoint.getTotalPoints();

                List<OperationExpertLog> tempListOpExpertsLog = mapOperationExpertLogs.get(id);
                for (OperationExpertLog tempOpExpertsLog : tempListOpExpertsLog) { //for de map
                    ExpertInfoFromBigQueryDTO expertInfo =
                            ExpertInfoFromBigQueryDTO.builder()
                                    .id(id)
                                    .operationType(tempOpExpertsLog.getOperationType())
                                    .amountEntered(tempOpExpertsLog.getAmountEntered())
                                    .totalPoints(totalPointsmapExpertPoints)
                                    .operationDate(tempOpExpertsLog.getOperationDate())
                                    .build();

                    expertsInfo.add(expertInfo);
                }
            }
        }
        expertsInfo.forEach(System.out::println);

        return expertsInfo;
    }
}
