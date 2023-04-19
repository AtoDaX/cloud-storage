package edu.pet.cloudstorage;

import edu.pet.cloudstorage.dto.RegistrationDTO;
import edu.pet.cloudstorage.model.Role;
import edu.pet.cloudstorage.model.User;
import edu.pet.cloudstorage.repositories.RoleRepository;
import edu.pet.cloudstorage.repositories.UserRepository;
import edu.pet.cloudstorage.services.UserServiceImpl;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

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
