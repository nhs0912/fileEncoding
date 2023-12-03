package com.justone.fileencoding.upload.domain;

import com.justone.fileencoding.upload.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "item")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {
    private static final String PERIOD = ".";

    @Value("${file.dir}")
    private static String fileDir;
    private static final String originalPath = fileDir + "original";
    private static final String savePath = fileDir + ;

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    private String originalName;
    private String originalExtendType;

    private String saveName;
    private String saveExtendType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    public Item of(MultipartFile multipartFile) {
        String originalFileNameAndExtendName = multipartFile.getOriginalFilename();
        String originalFileName =
    }

    private String extractPath() {

    }

    private int findIndexOfPeriod(String text) {
        return text.lastIndexOf(PERIOD);
    }


}
