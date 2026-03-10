package com.example.stock.service;

import com.example.stock.dao.entities.*;
import com.example.stock.dao.repositories.EntropotRepository;
import com.example.stock.dao.repositories.ProductRepository;
import com.example.stock.dao.repositories.StockRepository;
import com.example.stock.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;
//@PreAuthorize("hasRole('ADMIN') or (hasRole('MANAGER'))")
@Service
public class StockManager {
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntropotRepository entrepotRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Stock> getAll() {
        List<Stock> list = stockRepository.findAll();
        if (list.isEmpty()) {
            return List.of();
        }
        return list;
    }
    public List<Stock> getStocksOfWarehouse(Integer userId){
        User user=userRepository.findById(userId).orElseThrow(()->new RuntimeException("this user does not exist"));
        if(user.getEntrepotAssigne()!=null ){
            List<Stock> list = stockRepository.findStocksByEntrepot(user.getEntrepotAssigne());
            if (list.isEmpty() ) {
                return List.of();
            }
            return list;
        }
        throw new RuntimeException("the manager is not assigned to no warehouse");
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

    public Stock updateStock(int user_id,Stock stock) {
        User user = userRepository.findById(user_id).orElseThrow(()->new RuntimeException("this user does not exist"));
        Stock existing = stockRepository.findById(stock.getId()).orElseThrow(()->new RuntimeException("this stock does not exist"));
        
        // Check authorization
        if(user.getRole() == Role.ADMIN){
            // Admin can update any stock
            if(stock.getQuantity()!=null){
                existing.setQuantity(stock.getQuantity());
            }
            if(stock.getStockAlert()!=null){
                existing.setStockAlert(stock.getStockAlert());
            }
        } else if(user.getRole() == Role.MANAGER){
            // Manager can only update stock in their assigned warehouse
            if(user.getEntrepotAssigne() == null){
                throw new RuntimeException("Manager is not assigned to any warehouse");
            }
            if(!user.getEntrepotAssigne().getId().equals(existing.getEntrepot().getId())){
                throw new RuntimeException("Manager can only update stock in their assigned warehouse");
            }
            if(stock.getQuantity()!=null){
                existing.setQuantity(stock.getQuantity());
            }
            if(stock.getStockAlert()!=null){
                existing.setStockAlert(stock.getStockAlert());
            }
        } else {
            throw new RuntimeException("User does not have permission to update stock");
        }
        
        return stockRepository.save(existing);
    }

    public boolean deleteStock(Integer id) {
        stockRepository.deleteById(id);
        return stockRepository.findById(id).isEmpty();
    }

}
