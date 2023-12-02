package com.justone.fileencoding.upload.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "upload_order")
@Getter
@Setter
public class UploadOrder extends BaseEntity {
    @Id
    @Column(name = "upload_order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
