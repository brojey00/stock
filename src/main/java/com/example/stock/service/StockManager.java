package com.example.stock.service;

import com.example.stock.dao.entities.Stock;
import com.example.stock.dao.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StockManager {
    @Autowired
    private StockRepository stockRepository;

    public List<Stock> getAll() {
        List<Stock> list = stockRepository.findAll();
        if (list.isEmpty()) {
            return List.of();
        }
        return list;
    }

    public Optional<Stock> getById(Integer id) {
        return stockRepository.findById(id);
    }

    public Stock addStock(Stock stock) {
        return stockRepository.save(stock);
    }

    public Stock updateStock(Stock stock) {
        Optional<Stock> existing = stockRepository.findById(stock.getId());
        if (existing.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Stock does not exist");
        }
        return stockRepository.save(stock);
    }

    public boolean deleteStock(Integer id) {
        stockRepository.deleteById(id);
        return stockRepository.findById(id).isEmpty();
    }
}
