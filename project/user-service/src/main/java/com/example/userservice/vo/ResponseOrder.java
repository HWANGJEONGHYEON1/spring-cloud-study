package com.example.userservice.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseOrder {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private String createdAt;

    private String orderId;

}
