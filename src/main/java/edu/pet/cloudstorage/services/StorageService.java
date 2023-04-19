package edu.pet.cloudstorage.services;

import edu.pet.cloudstorage.dto.DirectoryDTO;
import edu.pet.cloudstorage.dto.FileDTO;

import edu.pet.cloudstorage.model.User;

import edu.pet.cloudstorage.repositories.FileRepository;
import edu.pet.cloudstorage.utils.Utils;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class StorageService {

    private final FileRepository fileRepository;
    @Autowired
    public StorageService(FileRepository fileRepository){
        this.fileRepository = fileRepository;

    }

    public void uploadFile(FileDTO fileDTO, @AuthenticationPrincipal User user) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try (InputStream inputStream = new BufferedInputStream(fileDTO.getFile().getInputStream())){
            fileRepository.upload(Utils.getUserDirectory(user)+fileDTO.getPath() + fileDTO.getFile().getOriginalFilename(), inputStream);
        }


    }
        //TODO вынести в отдельную функцию выгрузку файлов
    public void uploadDirectory(DirectoryDTO directoryDTO, @AuthenticationPrincipal User user) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        for (MultipartFile file :
                directoryDTO.getFiles()) {
            try(InputStream inputStream = new BufferedInputStream(file.getInputStream())) {
                fileRepository.upload(Utils.getUserDirectory(user)+directoryDTO.getPath() + file.getOriginalFilename(), inputStream);
            }


        }
    }
}
