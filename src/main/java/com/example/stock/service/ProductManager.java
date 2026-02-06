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
        if (list.isEmpty()) {
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
        Optional<Product> prod = productRepository.findById(product.getId());
        if (prod.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Product does not exist");
        }
        return productRepository.save(product);
    }

    public boolean deleteProduct(int id) {
        productRepository.deleteById(id);
        if (productRepository.findById(id).isPresent()) {
            return false;
        }
        return true;
    }
}
