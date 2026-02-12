package com.example.stock.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private String Description;
    private String Category;
    private Integer sell_price;
    private String unit;
}
