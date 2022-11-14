package com.example.catalogservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResponseCatalog {
    private String productId;
    private String productName;

    private Integer unitPrice;
    private Integer stock;
    private LocalDate createdAt;
}
