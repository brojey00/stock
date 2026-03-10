package com.example.stock.dao.repositories;

import com.example.stock.dao.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    //List<Product> findProductsByStockId(Integer id);
}
