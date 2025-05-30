package com.micro.publisher.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j(topic = "MQTT-PUBLISHER-CONFIG")
public class MqttConfig {

    private final MqttProperties mqttProperties;
    private String broker;
    private String clientId;

    @PostConstruct
    void init() {
        this.broker = mqttProperties.getBroker();
        this.clientId = mqttProperties.getClientId();
        log.info("MQTT broker URL: {}", broker);
        log.info("MQTT client ID: {}", clientId);
    }

    @Bean
    public MqttClient mqttClient() throws MqttException {
        // Create and configure the MQTT client
        // the data (session and message) is stored in memory RAM, anther option is to use a file persistence
        MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        // clear all session data on disconnect
        options.setCleanSession(false);
        options.setAutomaticReconnect(true);


        client.connect(options);
        return client;
    }
}
