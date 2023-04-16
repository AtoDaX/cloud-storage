package edu.pet.cloudstorage.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class MainPageController {
    @GetMapping
    public String show(@AuthenticationPrincipal User user){
        if (user==null){
            return "redirect:/login";
        }
        return "redirect:/storage";
    }
}
