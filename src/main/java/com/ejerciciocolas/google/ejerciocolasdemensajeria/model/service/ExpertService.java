package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.ExpertDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.ExpertPointDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertInfoBigQueryDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.GeneralInfoExpertDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.Expert;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.ExpertPoint;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.OperationExpertLog;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class ExpertService {

    private final ExpertDAO expertDAO;

    public List<ExpertInfoBigQueryDTO> getExpertInfoToSendBigQuery(){

        List<GeneralInfoExpertDTO> generalInfoExpertDTOs = this.getGeneralInfoExpert();
        List<ExpertInfoBigQueryDTO> expertInfoBigQueryDTOS = new ArrayList<>();

        generalInfoExpertDTOs.forEach( gieDTO -> {
            ExpertInfoBigQueryDTO expertInfo =
                    ExpertInfoBigQueryDTO.builder()
                            .id(gieDTO.getIdExpert())
                            .operationType(gieDTO.getOperationType())
                            .amountEntered(gieDTO.getAmountEntered())
                            .pointsOperation(gieDTO.getPointsOperation() )
                            .totalPoints(gieDTO.getTotalPoints())
                            .operationDate(gieDTO.getOperationDate())
                            .build();

            expertInfoBigQueryDTOS.add(expertInfo);
        });

        return expertInfoBigQueryDTOS;
    }

    public List<GeneralInfoExpertDTO> getGeneralInfoExpert() {
        List<Expert> experts = expertDAO.findAll();

        if(experts.isEmpty())
            return Collections.emptyList();

        List<GeneralInfoExpertDTO> generalInfoExpertDTOs = new ArrayList<>();
        Map<Long, List<OperationExpertLog>> mapOperationExpertLogs = new HashMap<>();

        for (Expert expert : experts) {
            long idExpert = expert.getId(); //id del expert

            if (expert.getOperationExpertLogs().isEmpty()) { //si no cuenta con operación, pasa a sig. iteración
                continue;
            }
            mapOperationExpertLogs.put(expert.getId(), expert.getOperationExpertLogs());

            List<OperationExpertLog> tempListOpExpertsLog = mapOperationExpertLogs.get(idExpert);

            //for de map - operaciones de expert@, llenamos info general de expert@ en lista "generalInfoExpertDTOs"
            for (OperationExpertLog tempOpExpertsLog : tempListOpExpertsLog) {
                GeneralInfoExpertDTO generalInfoExpertDTO =
                        GeneralInfoExpertDTO.builder()
                                .idExpert( idExpert )
                                .firstName( expert.getFirstName() )
                                .lastName( expert.getLastName() )
                                .pointsOperation( tempOpExpertsLog.getPointsGenerated() )
                                .totalPoints( expert.getExpertPoints().getTotalPoints() )
                                .operationType( tempOpExpertsLog.getOperationType() )
                                .amountEntered( tempOpExpertsLog.getAmountEntered() )
                                .operationDate( tempOpExpertsLog.getOperationDate() )
                                .build();

                generalInfoExpertDTOs.add(generalInfoExpertDTO);
            }
        }
        return generalInfoExpertDTOs;
    }

    public Expert findExperById(long id){
        return expertDAO.findById(id).orElse(null);
    }
}
