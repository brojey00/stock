package com.example.stock.service;

import com.example.stock.dao.entities.Entrepot;
import com.example.stock.dao.entities.Product;
import com.example.stock.dao.entities.Stock;
import com.example.stock.dao.repositories.EntropotRepository;
import com.example.stock.dao.repositories.ProductRepository;
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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntropotRepository entrepotRepository;

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
        if (stock.getProduct() == null || stock.getProduct().getId() == null) {
            throw new RuntimeException("Product ID is required");
        }

        Product product = productRepository.findById(stock.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + stock.getProduct().getId()));

        // Validate that entrepot exists and fetch full entity
        if (stock.getEntrepot() == null || stock.getEntrepot().getId() == null) {
            throw new RuntimeException("Entrepot ID is required");
        }

        Entrepot entrepot = entrepotRepository.findById(stock.getEntrepot().getId())
                .orElseThrow(() -> new RuntimeException("Entrepot not found with ID: " + stock.getEntrepot().getId()));

        // Check if stock already exists for this product-entrepot combination
        Optional<Stock> existingStock = stockRepository.findByProductIdAndEntrepotId(
                product.getId(),
                entrepot.getId()
        );

        if (existingStock.isPresent()) {
            throw new RuntimeException("Stock already exists for this product in this entrepot. Use update instead.");
        }
        stock.setProduct(product);
        stock.setEntrepot(entrepot);

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
