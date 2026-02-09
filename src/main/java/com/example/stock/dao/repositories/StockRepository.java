package com.example.stock.dao.repositories;

import com.example.stock.dao.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock,Integer> {
    Optional<Stock> findByProductIdAndEntrepotId(Integer id, Integer id1);
}
