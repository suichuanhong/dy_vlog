package cn.schff.dyvlog.config;

import cn.schff.dyvlog.common.util.MinIOUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;


/**
 * @Author：眭传洪
 * @Create：2023/5/5 21:13
 * @Meet: 2022/02/25
 * @Start: 2022/6/24
 */
@SpringBootConfiguration
@Data
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endPoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.image-size}")
    private Integer imageSiz;

    @Value("${minio.file-size}")
    private Integer fileSize;

    @Bean
    public MinIOUtils minIOUtils() {
        return new MinIOUtils(endPoint, bucketName, accessKey, secretKey, imageSiz, fileSize);
    }

}
