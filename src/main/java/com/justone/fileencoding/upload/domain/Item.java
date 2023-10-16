package com.justone.fileencoding.upload.domain;

import lombok.Data;

import java.util.List;

@Data
public class Item {
    private Long id;
    private String saveName;
    private String originalName;
    private List<UploadFile> files;
    private List<UploadFile> eucKrFiles;
    private String clientIp;
}
