package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    // 쿼리 메소드 (작성 양식 : find + (엔티티 이름) + By + 변수이름  *엔티티 이름은 생략 가능)
    List<Item> findByItemNm(String itemNm);

    // 상품을 상품명과 상품 상세 설명을 OR 조건을 이용하여 조회하는 쿼리 메소드
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
    
    // 파라미터로 넘어온 price 변수보다 값이 작은 상품 데이터를 조회하는 쿼리 메소드
    List<Item> findByPriceLessThan(Integer price);

    /*
    OrderBy + 속성명 + Asc   : 오름차순 키워드
    OrderBy + 속성명 + Desc  : 내림차순 키워드
     */
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);
    
    
    // @Query를 이용한 처리
    
    // @Param을 이용하여 파라미터로 넘어온 값을 JPQL에 들어갈 변수로 지정  itemDetail 변수를 "like % %" 로 값이 들어가도록 작성
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);


    @Query(value="select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

    
}
