package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")    // @Value 어노테이션을 통해 application.properties 파일에 등록한 itemImgLocation 값을 불려와서 itemImgLocation 변수에 넣어줍니다.
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)) {
            // 사용자가 상품의 이미지를 등록했다면 저장할 경로와 파일의 이름, 파일을 파일의 바이트 배열을 파일 업로드 파라미터로 uploadFile 메소드를 호출, 호출 결과 로컬에 저장된 파일의 이름을 imgName 변수에 저장
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            
            /** 저장한 상품 이미지를 불러올 경로를 설정, 외부 리소스를 불러오는 urlPatterns로 WebMvcConfig 클래스에서 '/images/**'를 설정, 
            또한 application.properties에서 설정한 uploadPath 프로퍼티 경로인 "C:/shop/" 아래 item 폴더에 이미지를 저장하므로 상품 이미지를 불러오는 경로로 "/images/item/"를 붙여줍니다. **/
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl); // 입력받은 상품 이미지 정보를 저장    
        // imgName : 실제 로컬에 저장된 상품 이미지 파일의 이름, 
        // oriImgName : 업로드헀던 상품 이미지 파일의 원래 이름,
        // imgUrl : 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if(!itemImgFile.isEmpty()) {    // 상품 이미지를 수정한 경우 상품 이미지를 업데이트합니다.
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)    // 상품 이미지 아이디를 이용하여 기존에 저장했던 상품 이미지 엔티티를 조회합니다.
                    .orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())) {   // 기존에 등록된 상품 이미지 파일이 있을 경우 해당 파일 삭제
                fileService.deleteFile(itemImgLocation+ "/" + savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            // 업데이트한 상품 이미지 파일을 업로드
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            // 변경된 상품 이미지 정보 세팅
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }
}
