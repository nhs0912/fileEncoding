package com.justone.fileencoding.upload.domain;

import lombok.*;


@Builder
@Getter
@RequiredArgsConstructor
public class UploadFile {
    private static final String POINT = ".";
    private final String originalName;
    private final String saveName;
    private final String extendedName;

    public String fullOriginalFileName(){
        return originalName + POINT + extendedName;
    }

    public String fullSaveFileName(){
        return saveName + POINT + extendedName;
    }

    public String changeEucKrFileName() {
        return originalName + "_eucKr" + POINT + extendedName;
    }
}
