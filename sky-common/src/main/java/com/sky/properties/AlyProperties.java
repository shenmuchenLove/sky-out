package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "sky.aly.oss")
public class AlyProperties {

    private String endpoint;
    private String bucketName;
    private String region;

}
