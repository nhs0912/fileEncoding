package com.justone.fileencoding.upload.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "upload_items")
@Getter
@Setter
public class UploadItems extends BaseEntity {
    @Id
    @Column(name = "upload_items_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_order_id")
    private UploadOrder uploadOrder;

}
