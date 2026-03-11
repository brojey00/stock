package com.example.stock.dto;

import lombok.Data;

@Data
public class VenteRequest {
    private Integer productId;
    private Integer entrepotId;
    private Integer quantity;
}
