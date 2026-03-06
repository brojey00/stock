package com.example.stock.controllers.managerController;

import com.example.stock.dao.entities.Stock;
import com.example.stock.dao.entities.User;
import com.example.stock.service.EntropotManager;
import com.example.stock.service.ProductManager;
import com.example.stock.service.StockManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/stocks")
    public List<Stock> showstocks(@AuthenticationPrincipal User user){
        System.out.println("Username: " + user.getUsername());
        System.out.println("Authorities: " + user.getAuthorities());
        return stockManager.getStocksOfWarehouse(user.getId());
    }

    @PatchMapping("/updatestock/{user_id}")
    public Stock updateStock(@PathVariable int user_id,@RequestBody Stock stock){
        return stockManager.updateStock(user_id,stock);

    }
}
