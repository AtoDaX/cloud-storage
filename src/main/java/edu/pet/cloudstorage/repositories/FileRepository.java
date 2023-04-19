package edu.pet.cloudstorage.repositories;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Repository
public class FileRepository {
    private final String BUCKET_NAME = "user-files";
    private final String FILE_TYPE = "application/octet-stream";
    private final MinioClient minioClient;
    @Autowired
    public FileRepository(MinioClient minioClient){
        this.minioClient = minioClient;
    }

    public void upload(String objectName, InputStream inputStream) throws
            ServerException, InsufficientDataException, ErrorResponseException,
            IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(objectName)
                        .stream(inputStream, -1, 10485760)
                .contentType("application/octet-stream")
                .build());
    }




    public void remove() {

    }


    public void rename() {

    }


    public void get() {

    }
}
