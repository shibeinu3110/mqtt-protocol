package com.micro.publisher.service.impl;

import com.micro.publisher.document.SavedData;
import com.micro.publisher.enumarate.MessageStatus;
import com.micro.publisher.repository.SavedDataRepository;
import com.micro.publisher.service.RetryService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "RETRY-SERVICE")
public class RetryServiceImpl implements RetryService {
    private final MqttClient mqttClient;
    private final SavedDataRepository savedDataRepository;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Override
    public void sendPendingMessages() {
        if(!mqttClient.isConnected()) {
            log.warn("MQTT client is not connected, cannot send pending messages.");
            return;
        }
        List<SavedData> pendingMessages = savedDataRepository.findByStatus(MessageStatus.PENDING);
        if (pendingMessages.isEmpty()) {
            log.info("No pending messages to retry.");
            return;
        }
        for(SavedData savedData : pendingMessages) {
            
            executorService.submit(() -> {
                try {
                    send(savedData);
                } catch (Exception e) {
                    log.error("Failed to send pending message: {}", savedData, e);
                }
            });
        }
    }

    private void send(SavedData data) {
        try {
            String payload = String.format("{\"temperature\": %s}", data.getData());
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(2);
            mqttClient.publish("sensor/temperature", message);

            data.setStatus(MessageStatus.SENT);
            savedDataRepository.save(data);
            log.info("Retried and sent from DB: {}", payload);
        } catch (Exception e) {
            log.error("Failed to resend from DB", e);
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
