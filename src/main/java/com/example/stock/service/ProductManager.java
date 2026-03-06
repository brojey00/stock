package com.example.stock.service;

import com.example.stock.dao.entities.Product;
import com.example.stock.dao.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service

public class ProductManager {
    @Autowired
    private ProductRepository productRepository;
    public List<Product> getAll() {
        List<Product> list = productRepository.findAll();
        if (list.isEmpty() ) {
            return List.of();
        }
        return list;
    }

    public Optional<Product> getById(Integer id) {
        return productRepository.findById(id);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        Product existing = productRepository.findById(product.getId())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product does not exist"));
        
        // Partial update - only update provided fields
        if(product.getName() != null){
            existing.setName(product.getName());
        }
        if(product.getDescription() != null){
            existing.setDescription(product.getDescription());
        }
        if(product.getCategory() != null){
            existing.setCategory(product.getCategory());
        }
        if(product.getUnit() != null){
            existing.setUnit(product.getUnit());
        }
        if(product.getBuy_price() != null){
            existing.setBuy_price(product.getBuy_price());
        }
        if(product.getSell_price() != null){
            existing.setSell_price(product.getSell_price());
        }
        
        return productRepository.save(existing);
    }

    public boolean deleteProduct(int id) {
        productRepository.deleteById(id);
        return productRepository.findById(id).isEmpty();
    }
}
