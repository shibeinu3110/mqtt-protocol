package com.micro.publisher.service.impl;

import com.micro.publisher.document.SavedData;
import com.micro.publisher.enumarate.MessageStatus;
import com.micro.publisher.repository.SavedDataRepository;
import com.micro.publisher.service.PublisherService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PUBLISHER-SERVICE")
public class PublisherServiceImpl implements PublisherService {
    private final MqttClient mqttClient;
    private final SavedDataRepository savedDataRepository;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void publishOrSave(double temperature, String topic) {
        if (mqttClient.isConnected()) {
            try {
                // random temp for sure
                //String payload = String.format("{\"temperature\": %.2f}", index);
                String payload = String.format("{\"temperature\": %.2f}", temperature);
                MqttMessage message = new MqttMessage(payload.getBytes());
                message.setQos(2);
                message.setRetained(false);
                mqttClient.publish(topic, message);
//                index++;
                log.info("Published message: {}", payload);
            } catch (Exception e) {
                log.error("Failed to publish temperature", e);
            }
        } else {
            executorService.submit(() -> {
                savedDataRepository.save(new SavedData(String.valueOf(temperature), LocalDateTime.now(), MessageStatus.PENDING));
                log.warn("MQTT client is not connected, cannot publish temperature");
            });
        }
    }

    @PreDestroy
    public void shutdown() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            log.info("Executor service has been shut down.");
        }

    }
}
