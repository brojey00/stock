package com.example.stock.dao.repositories;

import com.example.stock.dao.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock,Integer> {
}
