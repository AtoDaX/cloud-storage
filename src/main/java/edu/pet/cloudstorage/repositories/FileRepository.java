package edu.pet.cloudstorage.repositories;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FileRepository implements StorageRepository{
    private final MinioClient minioClient;
    @Autowired
    public FileRepository(MinioClient minioClient){
        this.minioClient = minioClient;
    }
    @Override
    public void upload() {

    }

    @Override
    public void remove() {

    }

    @Override
    public void rename() {

    }

    @Override
    public void get() {

    }
}
