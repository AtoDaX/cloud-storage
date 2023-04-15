package edu.pet.cloudstorage.dto;

import lombok.Data;

@Data
public class RegistrationDTO {
    private String username;
    private String password;
    private String passwordRepeat;
}
