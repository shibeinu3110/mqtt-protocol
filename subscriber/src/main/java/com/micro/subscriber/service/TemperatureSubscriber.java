package com.micro.subscriber.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "MQTT-TEMPERATURE-SUBSCRIBER")
public class TemperatureSubscriber {

    private final MqttClient mqttClient;

    @PostConstruct
    public void init() {
        setupCallback();
        if (mqttClient.isConnected()) {
            subscribeToTopic();
        } else {
            log.warn("MQTT client is not connected. Will subscribe when connection is established.");
        }
    }


    // set up callback for mqtt to handle when one of these events happen
    private void setupCallback() {
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if(reconnect) {
                    log.info("Reconnected to MQTT broker: {}", serverURI);
                } else {
                    log.info("Connected to MQTT broker for the first time: {}", serverURI);
                }
                subscribeToTopic();
            }

            @Override
            public void connectionLost(Throwable cause) {
                log.warn("MQTT connection lost: {}", cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String payload = new String(message.getPayload());
                log.info("Received message on topic {}: {}", topic, payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }

        });
    }

    private void subscribeToTopic() {
        try {
            mqttClient.subscribe("sensor/temperature", 2);
            log.info("Subscribed to topic: sensor/temperature");
        } catch (MqttException e) {
            log.error("Failed to subscribe to topic", e);
        }
    }
}
