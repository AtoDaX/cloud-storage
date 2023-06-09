package edu.pet.cloudstorage.controllers;

import edu.pet.cloudstorage.dto.RegistrationDTO;
import edu.pet.cloudstorage.model.User;
import edu.pet.cloudstorage.services.UserService;
import edu.pet.cloudstorage.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;
    @Autowired
    public RegistrationController(UserServiceImpl userService){
        this.userService = userService;
    }

    @GetMapping
    public String registrationPage(Model model){

        RegistrationDTO user = new RegistrationDTO();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping
    public String registerUser(@Valid @ModelAttribute("user") RegistrationDTO userDTO,
                               BindingResult result,
                               Model model){

        User existingUser = userService.findUserByUsername(userDTO.getUsername());
        if (!userDTO.getPassword().equals(userDTO.getPasswordRepeat())){
            result.rejectValue("passwordRepeat", null,
                    "Passswords are not equal!");

        }

        if(existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()){
            result.rejectValue("username", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDTO);
            return "registration";
        }

        userService.save(userDTO);
        return "redirect:/login";
    }
}
