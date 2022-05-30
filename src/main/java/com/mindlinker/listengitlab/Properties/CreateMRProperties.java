package com.mindlinker.listengitlab.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "createmr")
@Component
@PropertySource("application.yml")
public class CreateMRProperties {
    String title;
}
