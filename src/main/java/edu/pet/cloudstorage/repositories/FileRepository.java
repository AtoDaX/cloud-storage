package edu.pet.cloudstorage.repositories;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.apache.commons.compress.utils.IOUtils;
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
                .contentType(FILE_TYPE)
                .build());
    }

    public void create(String path, InputStream inputStream) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(path)
                .stream(inputStream, 0, -1)
                .build());
    }

    public Iterable<Result<Item>> getFilesInDirectory(String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(BUCKET_NAME)
                        .prefix(path)
                        .build());
        return results;

    }

    public void remove(String path, String name) {
        RemoveObjectArgs.builder().bucket(BUCKET_NAME)
                        .object(path+name).build();


    }

    public void rename() {

    }

    public byte[] getFile(String path, String name) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(path+name)
                        .build())) {
            return IOUtils.toByteArray(stream);
        }
    }


}
