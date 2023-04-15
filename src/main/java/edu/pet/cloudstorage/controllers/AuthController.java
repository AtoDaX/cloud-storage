package edu.pet.cloudstorage.controllers;

import edu.pet.cloudstorage.dto.UserDTO;
import edu.pet.cloudstorage.model.User;
import edu.pet.cloudstorage.repositories.UserRepository;
import edu.pet.cloudstorage.services.UserService;
import edu.pet.cloudstorage.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
