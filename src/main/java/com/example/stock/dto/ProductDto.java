package com.example.stock.dto;

public record ProductDto(
        Integer id,
        String name,
        String Description,
        String Category,
        Integer sell_price,
        String unit
) {}
