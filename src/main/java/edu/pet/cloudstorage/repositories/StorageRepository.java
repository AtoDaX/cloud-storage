package edu.pet.cloudstorage.repositories;

public interface StorageRepository {
    void upload();
    void remove();
    void rename();

    void get();
}
