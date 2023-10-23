package com.justone.fileencoding.upload.service;

import com.justone.fileencoding.upload.domain.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileStoreService {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        if (StringUtils.isEmpty(multipartFile.getOriginalFilename())) {
            throw new IllegalArgumentException("파일이름이 없습니다.");
        }

        String originalFullName = multipartFile.getOriginalFilename();
        String originalName = extractOriginalFileName(originalFullName);
        String extractedFileExtension = extractFileExtension(originalFullName);
        String storeFileName = createStoreFileName();

        UploadFile uploadFile = UploadFile
                .builder()
                .originalName(originalName)
                .saveName(storeFileName)
                .extendedName(extractedFileExtension)
                .build();

        multipartFile.transferTo(new File(getFullPath(uploadFile.fullSaveFileName())));
        log.info("originalName=== {}", uploadFile.fullOriginalFileName());

        log.info("storeFileName=== {}", uploadFile.fullSaveFileName());
        log.info("extractedFileExtension=== {}", uploadFile.getExtendedName());
        return uploadFile;
    }

    private String createStoreFileName() {
        return UUID.randomUUID().toString();
    }

    private String extractFileExtension(String originalName) {
        int position = originalName.lastIndexOf(".");
        return originalName.substring(position + 1);
    }

    private String extractOriginalFileName(String originalName) {
        int position = originalName.lastIndexOf(".");
        return originalName.substring(0, position);
    }

}
