package com.micro.subscriber.config;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "mqtt")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MqttProperties {
    @NotBlank(message = "MQTT broker URL must not be blank")
    String broker;
    @NotBlank(message = "MQTT client ID must not be blank")
    String clientId;
}
