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
    private final RetryService retryService;
    private final String topic = "sensor/temperature";
    Random random = new Random();

    @Scheduled(fixedRate = 5000) // Publish every 5 seconds
    public void publishTemperature() {

        double temperature = 15 + random.nextInt(15);
        publisherService.publishOrSave(temperature, topic);
    }

    @Scheduled(fixedRate = 10000) // Retry every 10 seconds
    public void retryPendingMessages() {
        retryService.sendPendingMessages();
    }
}
