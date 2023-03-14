package com.ejerciciocolas.google.ejerciocolasdemensajeria.controller;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.config.gcloud_pubsub.outbound.OutboundConfiguration;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.exception.exceptions.BadRequestExpertOperationException;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertOperationDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.MyAppGCPMessageDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service.OperationExpertLogService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util.ExceptionCustomCodesUtil.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/pubsub")
public class ProducerController {

    private final OutboundConfiguration.PubsubOutboundGateway gateway;
    private final OperationExpertLogService operationExpertLogService;

    /**
     * Publica un mensaje cualquiera en la cola Pub/Sub de gcloud
     *
     * @param message MyAppGCPMessageDTO
     * @return String
     */
    @PostMapping("/publish")
    public String publishMessage(@RequestBody MyAppGCPMessageDTO message){
        log.info("Mensaje saliente {}", message.toString());
        gateway.sendToPubsub( message.toString() );

        return "Message sent to Google Pub/Sub Successfully";
    }

    /**
     * Publica en la cola de mensajería un mensaje el cual se interpreta que la informaci&#243;n almacenada de las/los experto@s se almacenar&#225; en gcloud BigQuery
     *
     * @return String
     */
    @PostMapping("/publish-all-experts-operations")
    public String publishAllExpertsOperations(){
        gateway.sendToPubsub( "" );

        return "Se enviaron los movimientos de las/los expert@s a bigQuery";
    }

    /**
     * Publica en la cola de mensajer&#237;a un mensaje con informaci&#243;n de una operaci&#243;n realizada por el/la expert@, el cual sus detalles ser&#225;n almacenados
     * en H2 y los detalles m&#225;s espec&#237;ficos en gcloud BigQuery
     *
     * @param expertOperationDTO ExpertOperationDTO
     * @return String
     */
    @PostMapping("/publish-expert-operation")
    public ResponseEntity<String> publishExpertOperation(@Valid @RequestBody ExpertOperationDTO expertOperationDTO, BindingResult result) {




        if (!expertOperationDTO.getOperationType().isEmpty() && expertOperationDTO.getOperationType() != null) {

            log.info("Mensaje saliente {}", expertOperationDTO.toString());
            gateway.sendToPubsub( expertOperationDTO.toString() );

            return new ResponseEntity<>("Se envío el movimiento del/(de la) expert@ a bigQuery", HttpStatus.OK);
        } else {

            throw new BadRequestExpertOperationException(EXPERT_BAD_REQUEST_CODE, getSpecificMessageError(EXPERT_BAD_REQUEST_CODE) + " con el campo 'operation_type'.");
        }
    }
}
