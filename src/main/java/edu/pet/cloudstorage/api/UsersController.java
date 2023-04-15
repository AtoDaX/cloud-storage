package edu.pet.cloudstorage.api;

import edu.pet.cloudstorage.model.User;
import edu.pet.cloudstorage.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    UserRepository userRepository;
    @Autowired
    public UsersController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping
    public Iterable<User> findAll(){
        return userRepository.findAll();
    }
}
