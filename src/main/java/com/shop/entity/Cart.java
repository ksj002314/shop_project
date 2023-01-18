package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="cart")
@Getter @Setter
@ToString
public class Cart extends BaseEntity{
    
    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.EAGER)  // 즉시 로딩 Default
    @JoinColumn(name="member_id")   // 매핑할 외래키 지정
    private Member member;

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}
