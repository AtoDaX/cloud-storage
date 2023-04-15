package edu.pet.cloudstorage.dto;

import lombok.Data;

@Data
public class RegistrationDTO extends UserDTO {
    private String passwordRepeat;
}
