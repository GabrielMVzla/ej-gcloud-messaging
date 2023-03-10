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

    /**
     * Guarda información de operación realizada en tabla <u>operations_experts_log</u> y retorna información específica para guardar en BigQuery
     *
     * @param expertOperationDTO ExpertOperationDTO
     * @return ExpertInfoBigQueryDTO
     */
    public ExpertInfoBigQueryDTO getSpecificInfoToSendBigQuery(ExpertOperationDTO expertOperationDTO)  {

        Expert expert = expertDAO.findById( expertOperationDTO.getIdExpert() ).orElse(null);

        if(expert == null)
            return null;

        expertOperationDTO.setOperationType( expertOperationDTO.getOperationType().toUpperCase() );

        long expertId = expertOperationDTO.getIdExpert();
        String operation = expertOperationDTO.getOperationType(); //.replace("\\u00D3", "Ó");
        double amountEntered = expertOperationDTO.getAmountEntered();
        int multiplicadorBono = PERFORMED_OPERATION.equals(operation) ? 2 : 1;
        long initialCalcPoints = (long) (amountEntered * multiplicadorBono) / 100;

        //por si nunca ha realizado una operación, se va a realizar esta acción, en lugar de consultar puntos anteriores para realizar operación en base a ello
        ExpertPoint expertPoint = ExpertPoint.builder()
                .id( expertId ) //id igual al del experto
                .lastOperation( "COLOCACI\\u00D3N" )
                .lastPointsEntered( initialCalcPoints )
                .lastAmountEntered( amountEntered )
                .totalPoints( initialCalcPoints )
                .expert(expert)
                .build();

        long finalResultGeneratedPoints = initialCalcPoints;
        long finalTotalPointsExpert = initialCalcPoints;
        //Datos actuales de los puntos de nuestr@ expert@
        if (expert.getExpertPoints() != null) {
            //TODO crear nuevo campo que almacene los puntos residuales para que sean utilizados en el futuro
            //TODO add nuevo atributo amucumulatedResidual

            //TODO remover esto x nuevo campo amucumulatedResidual***/
                long lastAmountEntered = (long) expert.getExpertPoints().getLastAmountEntered();
                long lastResidualAmount = lastAmountEntered % 100;
            log.info("cada 100 pesos ingresados es un punto, si no se llega al punto, el residual se acumula para la próxima!");

            String lastOperation = expert.getExpertPoints().getLastOperation().toUpperCase();

            //si operación anterior no es ABONO y la actual SÍ, entonces se divide entre 2 solo la anterior, ya que más adelante hay una operación de multiplicar x 2 x el bono actual con el residual, entonces,
            // los puntos anteriores generados quedan con el valor inicial + los nuevos con su bonificación, ya que no se debe realizar de nuevo el bono pq ya se ha realizado anteriormente la operación de bono
            if(!PERFORMED_OPERATION.equals( lastOperation ) && PERFORMED_OPERATION.equals( operation ) && lastResidualAmount != 0){
                lastResidualAmount = (long) Math.ceil(( (double) lastResidualAmount / 2));
            }
            long calcPointsEnteredAndResidual =  (long) ((amountEntered + lastResidualAmount)   * multiplicadorBono);


            //TODO corregir que si ya se tomaron los puntos para completar un punto extra, no volver a utilizarlos para el siguiente
            long pointsFromAmountEntered = (long) (amountEntered * multiplicadorBono) / 100;
            long pointsFromAmountEnteredAndResidual = (calcPointsEnteredAndResidual) / 100;
            if(pointsFromAmountEnteredAndResidual > pointsFromAmountEntered){
            //solo se podría generar 1 punto debido a las reglas del negocio con los cálculos establecidos
                log.info("Un punto generado x acumulación anterior!!!");
            }

            finalResultGeneratedPoints = calcPointsEnteredAndResidual / 100;

            long totalPointsExpert = expert.getExpertPoints().getTotalPoints();
            finalTotalPointsExpert = totalPointsExpert + finalResultGeneratedPoints;

            expertPoint = ExpertPoint.builder()
                    .id( expertId ) //se agrega también el id para que sepa que es actualización de datos
                    .lastOperation( operation )
                    .lastPointsEntered( finalResultGeneratedPoints )
                    .lastAmountEntered( amountEntered )
                    .totalPoints( finalTotalPointsExpert )
                    .expert( expert )
                    .build();
        }

        OperationExpertLog operationExpertLog = OperationExpertLog.builder()
                .operationType( operation )
                .amountEntered( amountEntered )
                .pointsGenerated( finalResultGeneratedPoints )
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
                        .pointsOperation( finalResultGeneratedPoints )
                        .totalPoints( finalTotalPointsExpert )
                        .operationDate( savedOperationExpertLog.getOperationDate() )
                        .build();

        log.info("expertInfo: {}", expertInfo);

        return expertInfo;
    }

    public void saveExpertOperationDBAndBQ(ExpertOperationDTO expertOperationDTO) throws IOException {
        ExpertInfoBigQueryDTO expertInfoBigQueryDTO = this.getSpecificInfoToSendBigQuery(expertOperationDTO);
        dataExtractorService.queryToCSVAndBigQuery(expertInfoBigQueryDTO);
    }

    public void saveListExpertOperationsDBAndBQ() throws IOException {
        dataExtractorService.queryListToCSVAndBigQuery();
    }

}
