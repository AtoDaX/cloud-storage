package edu.pet.cloudstorage.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class DirectoryDTO {
    private String path;
    private List<MultipartFile> files;
}
