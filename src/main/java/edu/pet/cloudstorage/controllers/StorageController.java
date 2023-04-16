package edu.pet.cloudstorage.controllers;

import edu.pet.cloudstorage.dto.DirectoryDTO;
import edu.pet.cloudstorage.dto.FileDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/storage")
public class StorageController {
    @GetMapping
    public String showStorage(@AuthenticationPrincipal User user,
                              Model model){

        FileDTO file = new FileDTO();
        DirectoryDTO directory = new DirectoryDTO();

        model.addAttribute("user",user);
        model.addAttribute("file", file);
        model.addAttribute("directory", directory);
        return "storage";
    }

    @PostMapping("/upload-file")
    public String uploadFile(@AuthenticationPrincipal User user,
                         FileDTO file){
        if (file==null){
            return "redirect:/storage";
        }
        System.out.println(file.getFile().getOriginalFilename());


        return "redirect:/storage";

    }

    @PostMapping("/upload-directory")
    public String uploadDirectory(@AuthenticationPrincipal User user,
                         DirectoryDTO directory){
        if (directory==null){
            return "redirect:/storage";
        }
        System.out.println(directory.getFiles().get(0).getOriginalFilename());


        return "redirect:/storage";

    }
}
