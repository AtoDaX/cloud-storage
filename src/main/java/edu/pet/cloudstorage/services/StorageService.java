package edu.pet.cloudstorage.services;

import edu.pet.cloudstorage.dto.DirectoryDTO;
import edu.pet.cloudstorage.dto.FileDTO;

import edu.pet.cloudstorage.model.User;

import edu.pet.cloudstorage.repositories.FileRepository;
import edu.pet.cloudstorage.utils.Utils;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, List<String>> getDirectory(String path, @AuthenticationPrincipal User user) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Iterable<Result<Item>> fileList = fileRepository.getFilesInDirectory(Utils.getUserDirectory(user)+path);
        List<String > directories = new ArrayList<>();
        List<String > files = new ArrayList<>();
        Map<String, List<String>> allItems = new HashMap<>();

        for (Result<Item> itemResult : fileList) {
            if (itemResult.get().isDir()){
                String [] fullPath = itemResult.get().objectName().split("/");
                String name = fullPath[fullPath.length-1];
                directories.add(name);
                continue;
            }
            String [] fullPath = itemResult.get().objectName().split("/");
            String name = fullPath[fullPath.length-1];
            files.add(name);

        }

        allItems.put("dir", directories);
        allItems.put("file", files);

        return allItems;



    }


}
