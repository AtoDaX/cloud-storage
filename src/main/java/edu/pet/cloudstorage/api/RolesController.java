package edu.pet.cloudstorage.api;

import edu.pet.cloudstorage.model.Role;
import edu.pet.cloudstorage.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RolesController {
    RoleRepository roleRepository;
    @Autowired
    public RolesController(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public Iterable<Role> findAll(){
        return roleRepository.findAll();
    }
}
