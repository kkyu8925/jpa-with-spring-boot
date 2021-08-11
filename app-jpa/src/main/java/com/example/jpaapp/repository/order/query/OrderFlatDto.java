package com.example.jpaapp.repository.order.query;

import com.example.jpaapp.domain.Address;
import com.example.jpaapp.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderFlatDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private Address address;
    private OrderStatus orderStatus;

    // orderItem
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
