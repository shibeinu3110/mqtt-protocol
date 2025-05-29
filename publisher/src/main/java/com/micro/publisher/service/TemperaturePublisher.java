package com.micro.publisher.service;

import com.micro.publisher.repository.SavedDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "MQTT-TEMPERATURE-PUBLISHER")
public class TemperaturePublisher {
    private final MqttClient mqttClient;
    private final SavedDataRepository savedDataRepository;
    private final PublisherService publisherService;
    private final String topic = "sensor/temperature";

    @Scheduled(fixedRate = 5000) // Publish every 5 seconds
    public void publishTemperature() {
        Random random = new Random();
        double temperature = 15 + random.nextInt(15);
        publisherService.publishOrSave(temperature, topic);
    }
}
