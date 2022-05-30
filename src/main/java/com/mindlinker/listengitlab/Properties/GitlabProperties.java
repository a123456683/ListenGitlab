package com.mindlinker.listengitlab.Properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Data
@ConfigurationProperties(prefix = "gitlab")
@Component
@PropertySource("application.yml")
public class GitlabProperties {
    String tokenKey;
    String tokenValue;
    String agreement;
    String address;
    String targetBranch;
}
