package edu.pet.cloudstorage.controllers;

import edu.pet.cloudstorage.services.UserService;
import edu.pet.cloudstorage.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class AuthController {
    private final UserService userService;
    @Autowired
    public AuthController(UserServiceImpl userService){
        this.userService = userService;
    }
    @GetMapping
    public String loginPage(){
        return "login";
    }



}
