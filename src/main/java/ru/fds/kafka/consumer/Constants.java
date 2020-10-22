package ru.fds.kafka.consumer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "ru.fds.kafka.consumer")
public class Constants {

    private String bootstrapAddress;
    private String topicNameSimple;
    private String topicNameCallback;
    private String topicNameFilter;
    private String topicNameObject;
    private String topicNameStreamTable;
    private String topicNameFile;
    private String groupNameSimple;
    private String groupNameTwoPartitions;
    private String groupNameFilter;
    private String workDir;
}
