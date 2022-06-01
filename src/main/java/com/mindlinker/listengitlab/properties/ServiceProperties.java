package com.mindlinker.listengitlab.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Data
@ConfigurationProperties(prefix = "serviceinfo")
@Component
@PropertySource("application.yml")
public class ServiceProperties {
    int checkTime;
    String agreement;
    String address;
    String port;
    String prefix;
    String suffix;
}
