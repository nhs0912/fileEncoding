package com.justone.fileencoding.upload.domain;

import lombok.Data;

@Data
public class UploadFile {
    private final String originalName;
    private final String saveName;
}
