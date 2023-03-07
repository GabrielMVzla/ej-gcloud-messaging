package com.ejerciciocolas.google.ejerciocolasdemensajeria.config.gcloud_pubsub.inbound;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.config.PubSubConfiguration;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Configuration
@AllArgsConstructor
@Slf4j
public class InboundConfiguration {

    //private final PubSubConfiguration pubSubConfiguration;

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
            //log.info("headers {}", message.getHeaders());

            BasicAcknowledgeablePubsubMessage originalMessage =
                    message.getHeaders().get(GcpPubSubHeaders.ORIGINAL_MESSAGE,
                            BasicAcknowledgeablePubsubMessage.class);
                originalMessage.ack();


        };
    }
}
