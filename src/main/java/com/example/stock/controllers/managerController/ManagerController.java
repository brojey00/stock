package com.example.stock.controllers.managerController;

import com.example.stock.dao.entities.Stock;
import com.example.stock.service.EntropotManager;
import com.example.stock.service.ProductManager;
import com.example.stock.service.StockManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {
    @Autowired
    private ProductManager productManager;
    @Autowired
    private EntropotManager entropotManager;
    @Autowired
    private StockManager stockManager;

    @GetMapping("/stocks/{user_id}")
    public List<Stock> showstocks(@PathVariable Integer user_id){
        return stockManager.getStocksOfWarehouse(user_id);
    }

    @PatchMapping("/updatestock/{user_id}")
    public Stock updateStock(@PathVariable int user_id,@RequestBody Stock stock){
        return stockManager.updateStock(user_id,stock);

    }
}
