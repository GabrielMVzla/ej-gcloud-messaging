package com.ejerciciocolas.google.ejerciocolasdemensajeria;

import com.ejerciciocolas.google.ejerciocolasdemensajeria.config.PubSubConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class EjercioColasDeMensajeriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EjercioColasDeMensajeriaApplication.class, args);
	}

}
