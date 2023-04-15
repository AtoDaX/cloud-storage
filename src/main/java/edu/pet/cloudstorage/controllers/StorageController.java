package edu.pet.cloudstorage.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/storage")
public class StorageController {
    @GetMapping
    public String showStorage(@AuthenticationPrincipal User user,
                              Model model){
        model.addAttribute(user);
        return "storage";
    }
}
