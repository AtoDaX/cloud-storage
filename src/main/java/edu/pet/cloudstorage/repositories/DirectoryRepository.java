package edu.pet.cloudstorage.repositories;

import io.minio.MinioClient;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DirectoryRepository implements StorageRepository{
    private final MinioClient minioClient;
    @Autowired
    public DirectoryRepository(MinioClient minioClient){
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
