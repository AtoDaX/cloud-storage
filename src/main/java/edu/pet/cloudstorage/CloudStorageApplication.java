package edu.pet.cloudstorage;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CloudStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudStorageApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationRunner dataLoader(@Autowired MinioClient minioClient) {
        return args -> {
            boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket("user-files").build());
            if (!isBucketExists){
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket("user-files")
                                .build());
            }


        };
    }

}
