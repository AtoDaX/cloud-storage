package edu.pet.cloudstorage.controllers;

import edu.pet.cloudstorage.dto.DirectoryDTO;
import edu.pet.cloudstorage.dto.FileDTO;
import edu.pet.cloudstorage.model.User;
import edu.pet.cloudstorage.services.StorageService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/storage")
public class StorageController {
    private final StorageService storageService;
    @Autowired
    public StorageController(StorageService storageService){
        this.storageService = storageService;
    }
    @GetMapping
    public String showStorage(@AuthenticationPrincipal User user,
                              Model model) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        FileDTO file = new FileDTO();
        DirectoryDTO directory = new DirectoryDTO();
        String path = "";
        if (model.containsAttribute("path")){
            path = model.getAttribute("path").toString();
        }



        Map<String, List<String>> allItems = storageService.getDirectory(path, user);
        //TODO вынести dir+files в общие константы
        List<String> allDirectories = allItems.get("dir");
        List<String> allFiles = allItems.get("file");


        model.addAttribute("allDirectories", allDirectories);
        model.addAttribute("allFiles", allFiles);
        model.addAttribute("user",user);
        model.addAttribute("file", file);
        model.addAttribute("directory", directory);
        return "storage";
    }

    @PostMapping("/upload-file")
    public String uploadFile(@AuthenticationPrincipal User user,
                         FileDTO file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (file==null){
            return "redirect:/storage";
        }
        storageService.uploadFile(file, user);
        System.out.println(file.getFile().getOriginalFilename());


        return "redirect:/storage";

    }

    @PostMapping("/upload-directory")
    public String uploadDirectory(@AuthenticationPrincipal User user,
                         DirectoryDTO directory) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (directory==null){
            return "redirect:/storage";
        }
        storageService.uploadDirectory(directory, user);
        System.out.println(directory.getPath());


        return "redirect:/storage";

    }
}
