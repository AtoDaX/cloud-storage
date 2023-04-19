package edu.pet.cloudstorage.configuration;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Bean
    public MinioClient minioClient(){
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://127.0.0.1:9000")
                //TODO подключать через json
                .credentials("0KMBweoaAXG0KrcV", "rTvuCpiXnFq9zU1z0oQQKRIieey7wS4V")
                .build();
        return minioClient;
    }

}
