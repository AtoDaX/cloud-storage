package edu.pet.cloudstorage.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class FileDTO {
    private String path;
    private MultipartFile file;
}
