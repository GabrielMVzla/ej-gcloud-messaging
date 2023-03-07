package com.ejerciciocolas.google.ejerciocolasdemensajeria.controller;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.config.gcloud_pubsub.outbound.OutboundConfiguration;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.MyAppGCPMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/pubsub")
public class ProducerController {

    @Autowired
    private OutboundConfiguration.PubsubOutboundGateway gateway;
    //private PubSubConfigurationCustom.PubsubOutboudGateway gateway;
    @PostMapping("/publish")
    public String publishMessage(@RequestBody MyAppGCPMessageDTO message){
        log.info("Mensaje saliente {}", message.toString());
        gateway.sendToPubsub( message.toString() );

        //id_expert, operation_type, amount_entered, total_points, operation_date

        return "Message sent to Google Pub/Sub Successfully";
    }
}
