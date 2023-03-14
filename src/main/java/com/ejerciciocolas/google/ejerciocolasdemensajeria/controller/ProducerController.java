package com.ejerciciocolas.google.ejerciocolasdemensajeria.controller;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.config.gcloud_pubsub.outbound.OutboundConfiguration;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertOperationDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.MyAppGCPMessageDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service.OperationExpertLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
     * Publica en la cola (Pub/Sub) un mensaje el cual interpreta que la informaci&#243;n almacenada de las/los experto@s se almacenar&#225; en gcloud BigQuery
     *
     * @return String
     */
    @PostMapping("/publish-all-experts-operations")
    public String publishAllExpertsOperations(){
        gateway.sendToPubsub( "" );
        return "Se enviaron los movimientos de las/los expert@s a la cola";
    }

    /**
     * Publica en la cola de mensajer&#237;a un mensaje con informaci&#243;n de una operaci&#243;n realizada por el/la expert@, el cual sus detalles ser&#225;n almacenados
     * en H2 y los detalles m&#225;s espec&#237;ficos en gcloud BigQuery
     *
     * @param expertOperationDTO ExpertOperationDTO
     * @return String
     */
    @PostMapping("/publish-expert-operation")
    public ResponseEntity<String> publishExpertOperation(@Valid @RequestBody ExpertOperationDTO expertOperationDTO) {
        String msg = operationExpertLogService.publishExpertOperation(expertOperationDTO);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
