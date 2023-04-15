package edu.pet.cloudstorage.services;

import edu.pet.cloudstorage.dto.RegistrationDTO;
import edu.pet.cloudstorage.dto.UserDTO;
import edu.pet.cloudstorage.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    edu.pet.cloudstorage.model.User save(RegistrationDTO userDTO);
    List<User> getAll();

    User findUserByUsername(String username);
}
