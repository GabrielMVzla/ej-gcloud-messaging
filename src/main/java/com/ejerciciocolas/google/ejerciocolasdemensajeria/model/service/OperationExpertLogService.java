package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.config.connector.BigQueryConnector;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util.ConstantsUtil;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.ExpertDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.ExpertPointDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.OperationExpertLogDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertInfoBigQueryDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertOperationDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.Expert;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.ExpertPoint;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.OperationExpertLog;
import com.google.api.gax.rpc.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class OperationExpertLogService {

    private final ExpertDAO expertDAO;
    private final ExpertPointDAO expertPointDAO;
    private final OperationExpertLogDAO operationExpertLogDAO;
    private final DataExtractorService dataExtractorService;
    private final static String PERFORMED_OPERATION = "ABONO";
    private final BigQueryConnector bigQueryConnector;

    public ExpertInfoBigQueryDTO saveExpertInfoOperationDBAndBQ(ExpertOperationDTO expertOperationDTO) throws IOException {

        Expert expert = expertDAO.findById( expertOperationDTO.getIdExpert() ).orElse(null);

        if(expert == null)
            return null;

        expertOperationDTO.setOperationType( expertOperationDTO.getOperationType().toUpperCase() );

        long expertId = expertOperationDTO.getIdExpert();
        String operation = expertOperationDTO.getOperationType(); //.replace("\\u00D3", "Ó");
        double amountEntered = expertOperationDTO.getAmountEntered();
        long initialCalcPoints = (long) amountEntered / 100;

        ExpertPoint expertPoint = ExpertPoint.builder()
                .id( expertId ) //id igual al del experto
                .lastOperation( "INICIAL" )
                .lastPointsEntered( initialCalcPoints )
                .lastAmountEntered( amountEntered )
                .totalPoints( initialCalcPoints )
                .expert(expert)
                .build();

        int multiplicadorBono = PERFORMED_OPERATION.equals(operation) ? 2 : 1;
        long finalCalcGeneratedPoints = initialCalcPoints;
        long finalTotalPointsExpert = initialCalcPoints;
        //Datos actuales de los puntos de nuestr@ expert@
        if (expert.getExpertPoints() != null) {
            long lastAmountEntered = (long) expert.getExpertPoints().getLastAmountEntered();

            long lastResidualAmount = lastAmountEntered % 100;
            String lastOperation = expert.getExpertPoints().getLastOperation().toUpperCase();

            //si operación anterior no es ABONO y la actual SÍ, entonces se divide entre 2 solo la anterior, ya que más adelante hay una operación de multiplicar x 2 x el bono actual con el residual, entonces,
            // los puntos anteriores generados quedan con el valor inicial + los nuevos con su bonificación, ya que no se debe realizar de nuevo el bono pq ya se ha realizado anteriormente la operación de bono
            if(!PERFORMED_OPERATION.equals( lastOperation ) && PERFORMED_OPERATION.equals( operation ) && lastResidualAmount != 0){
                lastResidualAmount = (long) Math.ceil(( (double) lastResidualAmount / 2));
            }
            long calcPointsEnteredAndResidual =  (long) (amountEntered + lastResidualAmount) / 100;
            finalCalcGeneratedPoints = calcPointsEnteredAndResidual * multiplicadorBono;

            long totalPointsExpert = expert.getExpertPoints().getTotalPoints();
            finalTotalPointsExpert = totalPointsExpert + finalCalcGeneratedPoints;

            expertPoint = ExpertPoint.builder()
                    .id( expertId ) //se agrega también el id para que sepa que es actualización de datos
                    .lastOperation( operation )
                    .lastPointsEntered( finalCalcGeneratedPoints )
                    .lastAmountEntered( amountEntered )
                    .totalPoints( finalTotalPointsExpert )
                    .expert( expert )
                    .build();
        }

        OperationExpertLog operationExpertLog = OperationExpertLog.builder()
                .operationType( operation )
                .amountEntered( amountEntered )
                .pointsGenerated( finalCalcGeneratedPoints )
                .expert( expert )
                .build();

        ExpertPoint savedExpertPoint = expertPointDAO.save(expertPoint);
        OperationExpertLog savedOperationExpertLog = operationExpertLogDAO.save(operationExpertLog);

        log.info("savedExpertPoint: {}", savedExpertPoint);
        log.info("savedOperationExpertLog: {}", savedOperationExpertLog);
        log.info("operationExpertLog: {}, savedOperationExpertLog: {}", operationExpertLog, savedOperationExpertLog);

        ExpertInfoBigQueryDTO expertInfo =
                ExpertInfoBigQueryDTO.builder()
                        .id( expertId )
                        .operationType( operation )
                        .amountEntered( amountEntered )
                        .pointsOperation( finalCalcGeneratedPoints )
                        .totalPoints( finalTotalPointsExpert )
                        .operationDate( savedOperationExpertLog.getOperationDate() )
                        .build();

        log.info("expertInfo: {}", expertInfo);
        dataExtractorService.queryToBigQuery(ConstantsUtil.SCHEMA_TBL_DEMO);

        return expertInfo;
    }
}
