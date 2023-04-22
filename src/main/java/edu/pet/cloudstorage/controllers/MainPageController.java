package edu.pet.cloudstorage.controllers;

import edu.pet.cloudstorage.model.Role;
import edu.pet.cloudstorage.model.User;
import edu.pet.cloudstorage.repositories.RoleRepository;
import edu.pet.cloudstorage.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/")
public class MainPageController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public MainPageController(RoleRepository roleRepository,
                              UserRepository userRepository,
                              PasswordEncoder passwordEncoder){
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }
    @GetMapping
    public String show(@AuthenticationPrincipal User user){




        if (user==null){
            return "redirect:/login";
        }
        return "redirect:/storage";
    }

    @GetMapping("fill")
    public String fill(){
        if (roleRepository.findByName("USER_ROLE") == null){
            roleRepository.save(new Role("USER_ROLE"));
        }
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("USER_ROLE"));
        User userq = new User();
        userq.setRoles(roles);
        userq.setUsername("qwe");
        userq.setPassword(passwordEncoder.encode("qwe"));
        userRepository.save(userq);
        return "redirect:/";
    }
}
