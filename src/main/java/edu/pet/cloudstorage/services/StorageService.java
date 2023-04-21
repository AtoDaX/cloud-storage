package edu.pet.cloudstorage.services;

import edu.pet.cloudstorage.dto.Breadcrumb;
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

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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

    public void createDirectory(String path, String name, User user) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        File hello = new File("hello.txt");

        fileRepository.create(Utils.getUserDirectory(user) + path + name + "/", new ByteArrayInputStream(new byte[]{}));


    }

    public MultipartFile downloadFile(String path, String fileName, User user) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        System.out.println(path);
        byte[] bytes = fileRepository.getFile(Utils.getUserDirectory(user)+path, fileName);
        return new MultipartFile() {
            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return "multipart/form-data";
            }

            @Override
            public boolean isEmpty() {
                return bytes==null || bytes.length==0;
            }

            @Override
            public long getSize() {
                return bytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return bytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(bytes);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                new FileOutputStream(dest).write(bytes);
            }
        };

    }

    public Deque<Breadcrumb> getBreadcrumbs(String path){
        Deque<Breadcrumb> breadCrumbs = new ArrayDeque<>();
        boolean currentBreadCrumb = false;
        if (path.length()!=0){
            List<String> names = List.of(path.split("/"));
            for (int i = names.size() - 1; i >= 0; i--) {
                Breadcrumb breadCrumb = new Breadcrumb();

                String encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8);
                if (i > 0) {
                    path = path
                            .substring(0, path.length() - names.get(i).length() - 1);
                }

                String name = names.get(i);
                String url = encodedPath;

                breadCrumb.setName(name);
                breadCrumb.setPath(url);

                if (!currentBreadCrumb) {
                    breadCrumb.setCurrent(true);
                    currentBreadCrumb = true;
                }
                breadCrumbs.push(breadCrumb);
            }

        }
        Breadcrumb breadCrumbMainPage = new Breadcrumb();

        breadCrumbMainPage.setName("/");
        breadCrumbMainPage.setPath("");

        if (!currentBreadCrumb) {
            breadCrumbMainPage.setCurrent(true);
        }
        breadCrumbs.push(breadCrumbMainPage);

        return breadCrumbs;

    }
    public Map<String, List<String>> getDirectory(String path, User user) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
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
