package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;          // 한명의 회원은 여러 번 주문을 할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑

    private LocalDateTime orderDate; // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    // 주문 상태

    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)      // Orderitem에 있는 Order에 의해 관리된다는 의미로 해석,  부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 CascadeTypeAll 옵션 설정
    private List<OrderItem> orderItems = new ArrayList<>();     // 하나의 주문이 여러 개의 상품을 갖으므로 List 자료형을 사용해서 매핑


    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);
        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

}

/*
    orphanRemoval = true : 고아 객체 제거

 */