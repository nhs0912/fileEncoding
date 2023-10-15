package com.justone.fileencoding.upload.controller.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SaveItemRequest {
    private Long id;
    private String saveName;
    private String originalName;
    private List<MultipartFile> files;
    private String clientIp;
}
