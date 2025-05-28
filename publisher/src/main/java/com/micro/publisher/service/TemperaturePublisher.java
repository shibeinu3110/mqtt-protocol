package com.micro.publisher.service;

import com.micro.publisher.document.SavedData;
import com.micro.publisher.repository.SavedDataRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "MQTT-TEMPERATURE-PUBLISHER")
public class TemperaturePublisher {
    private final MqttClient mqttClient;
    private final SavedDataRepository savedDataRepository;
    private double index = 0;

    @Scheduled(fixedRate = 5000) // Publish every 5 seconds
    public void publishTemperature() {
        Random random = new Random();
        double temperature = 15 + random.nextInt(15);
        if (mqttClient.isConnected()) {
            try {
                // random temp for sure
                //String payload = String.format("{\"temperature\": %.2f}", index);
                String payload = String.format("{\"temperature\": %.2f}", temperature);
                MqttMessage message = new MqttMessage(payload.getBytes());
                message.setQos(2);
//            message.setRetained(true);
                mqttClient.publish("sensor/temperature", message);
//                index++;
                log.info("Published message: {}", payload);
            } catch (Exception e) {
                log.error("Failed to publish temperature", e);
            }
        } else {
            savedDataRepository.save(new SavedData(String.valueOf(temperature), LocalDateTime.now()));
            log.warn("MQTT client is not connected, cannot publish temperature");
        }
    }
}
