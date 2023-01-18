package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="item_img")
@Getter @Setter
public class ItemImg extends BaseEntity{

    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName;     // 이미지 파일명

    private String oriImgName;  // 원본 이미지 파일명

    private String imgUrl;      // 이미지 조회 경로

    private String repimgYn;    // 대표 이미지 여부

    @ManyToOne(fetch = FetchType.LAZY)  // 지연로딩 사용, 상품 엔티티와 다대일 단방향 관계 매핑
    @JoinColumn(name = "item_id")
    private Item item;

    
    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {   // 원본이미지 파일명, 이미지 파일명, 이미지 경로를 파라미터로 입력 받아서 이미지 정보 업데이트 메소드
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
