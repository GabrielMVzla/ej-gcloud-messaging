package com.ejerciciocolas.google.ejerciocolasdemensajeria.config.gcloud_pubsub.inbound;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.config.util.AttributesToStringToArrayUtil;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.dto.ExpertOperationDTO;
import com.ejerciciocolas.google.ejerciocolasdemensajeria.model.service.OperationExpertLogService;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.io.IOException;

@Configuration
@AllArgsConstructor
@Slf4j
public class InboundConfiguration {

    //private final PubSubConfiguration pubSubConfiguration;
    @Autowired
    private OperationExpertLogService operationExpertLogService;

    @Bean
    public PubSubInboundChannelAdapter messageChannelAdapter(@Qualifier("pubsubInputChannel") MessageChannel inputChannel,
                                                             PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate, "demo-subs"); //pubSubConfiguration.getSubscription()
        adapter.setOutputChannel(inputChannel);
        adapter.setAckMode(AckMode.MANUAL);
        return adapter;
    }

    @Bean
    public MessageChannel pubsubInputChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "pubsubInputChannel")
    public MessageHandler messageReceiver() {
        return message -> {
            String payload = new String((byte[]) message.getPayload());

            log.info("¤¤¤ Message arrived! Payload: " + payload);
            try {
                if(payload != null ){
                    if(payload.isEmpty()){
                        operationExpertLogService.saveListExpertOperationsDBAndBQ();
                    } else {
                        String[] arrayExpOperations = AttributesToStringToArrayUtil.AttributesToStringToArray(payload);
                        int count = 0;
                        ExpertOperationDTO expertOperationDTO = ExpertOperationDTO.builder()
                                .idExpert( Long.parseLong( arrayExpOperations[count++]) )
                                .operationType(arrayExpOperations[count++])
                                .amountEntered( Double.parseDouble( arrayExpOperations[count] ))
                                .build();
                        operationExpertLogService.saveExpertOperationDBAndBQ(expertOperationDTO);
                    }
                }
            } catch (IOException e) {
                log.info("Message: {}, Cause: {}", e.getMessage(), e.getCause());
            }
            //log.info("headers {}", message.getHeaders());

            BasicAcknowledgeablePubsubMessage originalMessage =
                    message.getHeaders().get(GcpPubSubHeaders.ORIGINAL_MESSAGE,
                            BasicAcknowledgeablePubsubMessage.class);
                originalMessage.ack();
        };
    }
}
