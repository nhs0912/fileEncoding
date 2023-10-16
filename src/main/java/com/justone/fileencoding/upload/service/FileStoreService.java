package com.justone.fileencoding.upload.service;

import com.justone.fileencoding.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        String originalName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalName);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalName, storeFileName);
    }

    private String createStoreFileName(String originalName) {
        String extractedFileExtension = extractFileExtension(originalName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + extractedFileExtension;

    }

    private String extractFileExtension(String originalName) {
        int position = originalName.lastIndexOf(".");
        return originalName.substring(position + 1);
    }
}
