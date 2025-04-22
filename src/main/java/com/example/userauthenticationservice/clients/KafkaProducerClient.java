package com.example.userauthenticationservice.clients;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Support reading materials:
 * 1. https://stackoverflow.com/questions/72930539/javax-mail-authenticationfailedexception-535-5-7-8-username-and-password-not-ac
 * 2. https://www.digitalocean.com/community/tutorials/javamail-example-send-mail-in-java-smtp
 * 3. https://mvnrepository.com/artifact/org.springframework.kafka/spring-kafka
 */
@Component
public class KafkaProducerClient {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

}
