package com.example.stock.mapper;

import com.example.stock.dao.entities.Product;
import com.example.stock.dto.ProductDto;

import java.util.List;

public class ProductMapper {

    public static ProductDto toDto(Product product) {
        if (product == null) return null;

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getSell_price(),
                product.getUnit()
        );
    }

    // Bonus: list version
    public static List<ProductDto> toDtoList(List<Product> products) {
        return products == null ? List.of() : products.stream().map(ProductMapper::toDto).toList();
    }
}