package edu.pet.cloudstorage.controllers;

import edu.pet.cloudstorage.dto.DirectoryDTO;
import edu.pet.cloudstorage.dto.FileDTO;
import edu.pet.cloudstorage.dto.NewFolderDto;
import edu.pet.cloudstorage.model.User;
import edu.pet.cloudstorage.services.StorageService;
import io.minio.errors.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

        List<String> allDirectories = allItems.get("dir");
        List<String> allFiles = allItems.get("file");


        model.addAttribute("breadcrumbs", storageService.getBreadcrumbs(path));
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
            return "redirect:/storage";
        }

        storageService.createDirectory(path, name, user);

        return "redirect:/storage?path="+URLEncoder.encode(path, StandardCharsets.UTF_8);

    }

    @GetMapping("/download")
    public void download(@AuthenticationPrincipal User user,
                           @RequestParam(required = false, defaultValue = "") String path,
                           @RequestParam String name,
                           HttpServletResponse response) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        MultipartFile file = null;

        try {
            file = storageService.downloadFile(path, name, user);
        } catch (ErrorResponseException e){
            response.sendRedirect("http://localhost:8080/storage?path=");
            return;
        }

        response.setContentType("multipart/form-data");
        response.setHeader("Content-disposition", "attachment;filename=" + name);
        try {
            IOUtils.copy(file.getInputStream(), response.getOutputStream());

            response.getOutputStream().flush();
        }catch (IOException e) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}
