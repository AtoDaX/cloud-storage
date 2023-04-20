package edu.pet.cloudstorage.controllers;

import edu.pet.cloudstorage.dto.DirectoryDTO;
import edu.pet.cloudstorage.dto.FileDTO;
import edu.pet.cloudstorage.dto.NewFolderDto;
import edu.pet.cloudstorage.model.User;
import edu.pet.cloudstorage.services.StorageService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
                              Model model,
                              @RequestParam(required = false, defaultValue = "") String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        FileDTO file = new FileDTO();
        DirectoryDTO directory = new DirectoryDTO();
        NewFolderDto newFolder= new NewFolderDto();
        path = URLDecoder.decode(path,StandardCharsets.UTF_8);
        if (path.equals("/")){
            path="";
        }
        file.setPath(path);
        directory.setPath(path);
        Map<String, List<String>> allItems = storageService.getDirectory(path, user);
        //TODO вынести dir+files в общие константы
        List<String> allDirectories = allItems.get("dir");
        List<String> allFiles = allItems.get("file");



        model.addAttribute("newFolder", newFolder);
        model.addAttribute("path", URLEncoder.encode(path, StandardCharsets.UTF_8));
        model.addAttribute("allDirectories", allDirectories);
        model.addAttribute("allFiles", allFiles);
        model.addAttribute("user",user);
        model.addAttribute("file", file);
        model.addAttribute("directory", directory);
        return "storage";
    }

    @PostMapping("/upload-file")
    public String uploadFile(@AuthenticationPrincipal User user,
                         FileDTO file,
                             @RequestParam(required = false, defaultValue = "") String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (file==null){
            return "redirect:/storage";
        }

        path = URLDecoder.decode(path,StandardCharsets.UTF_8);

        file.setPath(path);
        storageService.uploadFile(file, user);



        return "redirect:/storage?path=" + URLEncoder.encode(path, StandardCharsets.UTF_8);

    }

    @PostMapping("/upload-directory")
    public String uploadDirectory(@AuthenticationPrincipal User user,
                         DirectoryDTO directory,
                                  @RequestParam(required = false, defaultValue = "") String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (directory==null){
            return "redirect:/storage";
        }
        path = URLDecoder.decode(path,StandardCharsets.UTF_8);
        directory.setPath(path);
        storageService.uploadDirectory(directory, user);
        System.out.println(directory.getPath());


        return "redirect:/storage?path="+URLEncoder.encode(path,StandardCharsets.UTF_8);

    }

    @PostMapping("/new-directory")
    public String createDirectory(@AuthenticationPrincipal User user,
                                  @ModelAttribute("newFolderName") String name,
                                  @RequestParam(required = false, defaultValue = "") String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        path = URLDecoder.decode(path,StandardCharsets.UTF_8);
        if (name.isEmpty() || name==null){
            /*throw new RuntimeException("Incorrect directory name");*/
            return "redirect:/storage";
        }
        storageService.createDirectory(path, name, user);

        return "redirect:/storage?path="+URLEncoder.encode(path, StandardCharsets.UTF_8);

    }



}
