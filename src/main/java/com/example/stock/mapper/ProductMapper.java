package com.example.stock.mapper;

import com.example.stock.dao.entities.Product;
import com.example.stock.dto.ProductDTO;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(Product product);
    List<ProductDTO> toDtoList(List<Product> products);
}
