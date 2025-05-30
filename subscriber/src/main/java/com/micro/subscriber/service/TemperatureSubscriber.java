package com.micro.subscriber.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j(topic = "MQTT-TEMPERATURE-SUBSCRIBER")
public class TemperatureSubscriber {

    private final MqttClient mqttClient;
}

