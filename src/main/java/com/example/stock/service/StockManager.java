package com.example.stock.service;

import com.example.stock.dao.entities.Stock;
import com.example.stock.dao.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockManager {
    @Autowired
    private StockRepository stockRepository;

    public List<Stock> getAll() {
        return stockRepository.findAll();
    }
}
