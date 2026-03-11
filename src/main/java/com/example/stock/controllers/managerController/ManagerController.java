package com.example.stock.controllers.managerController;

import com.example.stock.dao.entities.HistoriqueVente;
import com.example.stock.dao.entities.Stock;
import com.example.stock.dao.entities.User;
import com.example.stock.dto.ProductDto;
import com.example.stock.dto.VenteRequest;
import com.example.stock.dto.VenteResponse;
import com.example.stock.service.EntropotManager;
import com.example.stock.service.ProductManager;
import com.example.stock.service.StockManager;
import com.example.stock.service.VenteManager;
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
    @Autowired
    private VenteManager venteManager;

    @GetMapping("/stocks")
    public List<Stock> showstocks(@AuthenticationPrincipal User user){
        return stockManager.getStocksOfWarehouse(user);
    }

    @PatchMapping("/stocks/{id}")
    public Stock updateStock(@AuthenticationPrincipal User user, @PathVariable int id, @RequestBody Stock stock){
        stock.setId(id);
        return stockManager.updateStock(user, stock);
    }

    @GetMapping("/products")
    public List<ProductDto> showproducts(@AuthenticationPrincipal User user){
        return productManager.getProductsByWarehouse(user);
    }


    @PostMapping("/ventes")
    public HistoriqueVente vendre(@AuthenticationPrincipal User user, @RequestBody VenteRequest request) {
        return venteManager.vendre(user, request.getProductId(), null, request.getQuantity());
    }

    @GetMapping("/ventes")
    public List<VenteResponse> getVentes(@AuthenticationPrincipal User user) {
        return venteManager.getHistorique(user);
    }

}
