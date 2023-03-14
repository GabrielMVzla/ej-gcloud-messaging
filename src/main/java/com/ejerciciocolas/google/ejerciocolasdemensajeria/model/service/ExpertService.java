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
    private final ExpertPointDAO expertPointDAO;

    /**
     * Retorna un listado con informaci&#243;n espec&#237;fica de las/los expert@s para ser guardada en la tabla de gcloud BigQuery
     *
     * @return List&#60;ExpertInfoBigQueryDTO>
     */
    public List<ExpertInfoBigQueryDTO> getListExpertInfoToSendBigQuery(){

        List<GeneralInfoExpertDTO> generalInfoExpertDTOs = this.getListGeneralInfoExpert();
        List<ExpertInfoBigQueryDTO> expertInfoBigQueryDTOS = new ArrayList<>();

        generalInfoExpertDTOs.forEach( gieDTO -> {
            ExpertInfoBigQueryDTO expertInfo =
                    ExpertInfoBigQueryDTO.builder()
                            .id( gieDTO.getIdExpert() )
                            .operationType( gieDTO.getOperationType() )
                            .amountEntered( gieDTO.getAmountEntered() )
                            .pointsOperation( gieDTO.getPointsOperation() )
                            .acumulatedResidual( gieDTO.getAcumulatedResidual() )
                            .totalPoints( gieDTO.getTotalPoints() )
                            .operationDate( gieDTO.getOperationDate() )
                            .build();

            expertInfoBigQueryDTOS.add(expertInfo);
        });

        return expertInfoBigQueryDTOS;
    }

    /**
     * Retorna un listado con la informaci&#243;n generas de de las/los expert@s en la BDD H2
     *
     * @return List<GeneralInfoExpertDTO>
     */
    public List<GeneralInfoExpertDTO> getListGeneralInfoExpert() {

        List<Expert> experts = expertDAO.findAll();

        if(experts.isEmpty())
            return Collections.emptyList();

        List<GeneralInfoExpertDTO> generalInfoExpertDTOs = new ArrayList<>();
        Map<Long, List<OperationExpertLog>> mapOperationExpertLogs = new HashMap<>();
        long expertTotalPoint = 0L;
        long acumulatedResidualPoints = 0L;

        for (Expert expert : experts) {
            long idExpert = expert.getId(); //id del/(de la) expert@

            if (expert.getOperationExpertLogs().isEmpty()) { //si no cuenta con operación, pasa a sig. iteración
                continue;
            }
            ExpertPoint expertPoint = expertPointDAO.findById(idExpert).orElse(null);
            if(expertPoint != null) {
                expertTotalPoint = expertPoint.getTotalPoints();
                acumulatedResidualPoints = expertPoint.getAcumulatedResidual();
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
                                .operationType( tempOpExpertsLog.getOperationType() )
                                .amountEntered( tempOpExpertsLog.getAmountEntered() )
                                .pointsOperation( tempOpExpertsLog.getPointsGenerated() )
                                .acumulatedResidual( acumulatedResidualPoints )
                                .totalPoints( expertTotalPoint )
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
