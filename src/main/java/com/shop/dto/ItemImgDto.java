package com.shop.dto;

import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper(); // 의존성 추가한  modelMapper 객체 생성

    public static ItemImgDto of(ItemImg itemImg) {
        return modelMapper.map(itemImg,ItemImgDto.class);       // 2.
    }
}

/*
ItemImgDto 메소드 설명
ItemImg 엔티티 객체를 파라미터로 받아서 ItemImg 객체의 자료형과 멤버변수의 이름이 같을 때 ItemImgDto로 값을 복사해서 반환합니다.
static 메소드로 선언해 ItemImgDto 객체를 생성하지 않고도 호출할 수 있도록 하겠습니다.
 */