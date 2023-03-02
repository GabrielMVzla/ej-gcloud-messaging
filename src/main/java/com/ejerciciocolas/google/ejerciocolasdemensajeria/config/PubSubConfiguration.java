package com.ejerciciocolas.google.ejerciocolasdemensajeria.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gm.pubsub")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PubSubConfiguration {

    private String topic;

    private String subscription;
}
