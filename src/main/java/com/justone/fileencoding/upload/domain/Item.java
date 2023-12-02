package com.justone.fileencoding.upload.domain;

import com.justone.fileencoding.upload.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "item")
@Getter
@Setter
public class Item extends BaseEntity {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    private String originalName;
    private String originalPath;
    private String originalExtendType;

    private String saveName;
    private String savePath;
    private String saveExtendType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

}
