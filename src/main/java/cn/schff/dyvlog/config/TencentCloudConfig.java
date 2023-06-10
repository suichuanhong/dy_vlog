package cn.schff.dyvlog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "tencent.cloud")
public class TencentCloudConfig {

    private String secretId;
    private String secretKey;

}
