package com.mindlinker.listengitlab.Properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "assigneemap")
@Component
@PropertySource("application.yml")
public class AssigneeProperties {

    @Value("#{${assigneemap}}")
    private Map<String, String> assigneeMap;

}
