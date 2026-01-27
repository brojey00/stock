package com.example.stock.service;

import com.example.stock.dao.entities.Product;
import com.example.stock.dao.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductManager {
    @Autowired
    private ProductRepository productRepository;
    public List<Product> getAll(){
        return productRepository.findAll();
    }
}
