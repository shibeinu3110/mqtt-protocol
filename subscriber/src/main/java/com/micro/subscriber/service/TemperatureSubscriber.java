package com.micro.subscriber.service;

import com.micro.subscriber.config.MqttSubscriberConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "MQTT-TEMPERATURE-SUBSCRIBER")
public class TemperatureSubscriber {
    private final MqttClient mqttClient;

    @PostConstruct
    public void subscribe() {

        try {
            mqttClient.subscribe("sensor/temperature", 2, (topic, message) -> {
                String payload = new String(message.getPayload());
                log.info("Received message on topic {}: {}", topic, payload);
            });
            log.info("Subscribed to topic: sensor/temperature");
        } catch (Exception e) {
            log.error("Failed to subscribe to topic", e);
        }
    }
}
