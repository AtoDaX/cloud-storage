package edu.pet.cloudstorage.dto;

import lombok.Data;

@Data
public class Breadcrumb {
    private String name;
    private String path;
    private boolean current;
}
