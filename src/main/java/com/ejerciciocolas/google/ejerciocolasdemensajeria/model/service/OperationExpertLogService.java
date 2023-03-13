package com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.ExpertDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.ExpertPointDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dao.OperationExpertLogDAO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertInfoBigQueryDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertOperationDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.Expert;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.ExpertPoint;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.entity.OperationExpertLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class OperationExpertLogService {

    private final ExpertDAO expertDAO;
    private final ExpertPointDAO expertPointDAO;
    private final OperationExpertLogDAO operationExpertLogDAO;
    private final DataExtractorService dataExtractorService;
    private static final String OPERATION_PERFORMED = "ABONO";
    private static final int POINTS_FLAG = 100;

    /**
     * Retorna informaci&#243;n escencial para guardar en gcloug BigQuery, adem&#225;s guarda informaci&#243;n de operaci&#243;n realizada en tabla <u>operations_experts_log</u>
     * y retorna informaci&#243;n espec&#237;fica para guardar en BigQuery adem&#225;s se calculan los puntos generados por las/los expert@s
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
        int multiplicadorBono = OPERATION_PERFORMED.equals(operation) ? 2 : 1;
        long initialCalcPoints = (long) (amountEntered * multiplicadorBono) / POINTS_FLAG;
        long finalAcumulatedResidualPoints = initialCalcPoints % POINTS_FLAG;

        //por si nunca ha realizado una operación, se va a realizar esta acción, en lugar de consultar puntos anteriores para realizar operación en base a ello
        ExpertPoint expertPoint = ExpertPoint.builder()
                .id( expertId ) //id igual al del/(de la) expert@
                .lastOperation( "COLOCACI\\u00D3N" )
                .lastPointsEntered( initialCalcPoints )
                .lastAmountEntered( amountEntered )
                .acumulatedResidual( finalAcumulatedResidualPoints )
                .totalPoints( initialCalcPoints )
                .expert(expert)
                .build();

        long finalResultGeneratedPoints = initialCalcPoints;
        long finalTotalPointsExpert = initialCalcPoints;
        //Datos actuales de los puntos de nuestr@ expert@
        if (expert.getExpertPoints() != null) {

            long lastAcumulatedResidualPoints = expert.getExpertPoints().getAcumulatedResidual();

            log.info("cada 100 pesos ingresados es un punto, si no se llega al punto, el residual se acumula para la pr\u00D3xima!");

            String lastOperation = expert.getExpertPoints().getLastOperation().toUpperCase();

            //si operación anterior no es ABONO y la actual SÍ, entonces se divide entre 2 solo la anterior, ya que más adelante hay una operación de multiplicar x 2 x el bono actual con el residual, entonces,
            // los puntos anteriores generados quedan con el valor inicial + los nuevos con su bonificación, ya que no se debe realizar de nuevo el bono pq ya se ha realizado anteriormente la operación de bono
            if(!OPERATION_PERFORMED.equals( lastOperation ) && OPERATION_PERFORMED.equals( operation ) && lastAcumulatedResidualPoints != 0){
                lastAcumulatedResidualPoints = (long) Math.ceil(( (double) lastAcumulatedResidualPoints / 2));
            }
            long calcPointsEnteredAndResidual =  (long) ((amountEntered + lastAcumulatedResidualPoints)   * multiplicadorBono);


            long pointsFromAmountEntered = (long) (amountEntered * multiplicadorBono) / POINTS_FLAG;
            long pointsFromAmountEnteredAndResidual = (calcPointsEnteredAndResidual) / POINTS_FLAG;
            if(pointsFromAmountEnteredAndResidual > pointsFromAmountEntered){
            //solo se podría generar 1 punto debido a las reglas del negocio con los cálculos establecidos
                log.info("Un punto generado x acumulaci\u00D3n anterior!!!");
            }

            finalAcumulatedResidualPoints = calcPointsEnteredAndResidual % 100;
            finalResultGeneratedPoints = pointsFromAmountEnteredAndResidual;

            long totalPointsExpert = expert.getExpertPoints().getTotalPoints();
            finalTotalPointsExpert = totalPointsExpert + finalResultGeneratedPoints;

            expertPoint = ExpertPoint.builder()
                    .id( expertId ) //se agrega también el id para que sepa que es actualización de datos
                    .lastOperation( operation )
                    .lastPointsEntered( finalResultGeneratedPoints )
                    .lastAmountEntered( amountEntered )
                    .acumulatedResidual( finalAcumulatedResidualPoints )
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
                        .acumulatedResidual( finalAcumulatedResidualPoints )
                        .totalPoints( finalTotalPointsExpert )
                        .operationDate( savedOperationExpertLog.getOperationDate() )
                        .build();
        log.info("expertInfo: {}", expertInfo);

        return expertInfo;
    }

    /**
     * Toma la información que lleg&#243; desde la request para realizar guardado informaci&#243;n en H2 y c&#225;lculos, adem&#225;s
     * de luego mandarlo a gcloud BigQuery
     *
     * @param expertOperationDTO ExpertOperationDTO
     * @throws IOException
     */
    public void saveExpertOperationDBAndBQ(ExpertOperationDTO expertOperationDTO) throws IOException {
        ExpertInfoBigQueryDTO expertInfoBigQueryDTO = this.getSpecificInfoToSendBigQuery(expertOperationDTO);
        dataExtractorService.queryToCSVAndBigQuery(expertInfoBigQueryDTO);
    }

    /**
     * Toma la informaci&#243n de las/los expert@s y la env&#237;a a gcloud BigQuery
     *
     * @throws IOException
     */
    public void saveListExpertOperationsDBAndBQ() throws IOException {
        dataExtractorService.queryListToCSVAndBigQuery();
    }

}
