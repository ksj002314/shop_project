package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;              // 장바구니에 담을 상품의 정보를 알아야 하므로 상품 엔티티 매핑, 하나의 상품은 여러 장바구니의 장바구니 상품으로 담길 수 있으므로  다대일 관계 매핑

    private int count;              // 같은 상품을 장바구네 몇 개 담을지 지정


    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    public void addCount(int count) {   // 장바구니에 기존에 담겨 있는 상품인데, 해당 상품을 추가로 장바구니에 담을 때 기존 수량에 현재 담을 수량을 더 해줄 때 사용할 메소드
        this.count = count;
    }

    public void updateCount(int count) {
        this.count = count;
    }

}
