package com.example.stock.service;

import com.example.stock.dao.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockManager {
    @Autowired
    private StockRepository stockRepository;
}
