package com.example.stock.controllers.managerController;

import com.example.stock.dao.entities.HistoriqueVente;
import com.example.stock.dao.entities.Stock;
import com.example.stock.dto.ProductDto;
import com.example.stock.dto.VenteRequest;
import com.example.stock.dto.VenteResponse;
import com.example.stock.service.*;
import com.example.stock.service.Securityutils;
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
    @Autowired
    private VenteManager venteManager;
    @Autowired
    private Securityutils securityutils;

    @GetMapping("/stocks")
    public List<Stock> showstocks(){
        return stockManager.getStocksOfWarehouse(securityutils.getCurrentUser());
    }

    @PatchMapping("/stocks/{id}")
    public Stock updateStock(@PathVariable int id, @RequestBody Stock stock){
        stock.setId(id);
        return stockManager.updateStock(securityutils.getCurrentUser(), stock);
    }

    @GetMapping("/products")
    public List<ProductDto> showproducts(){
        return productManager.getProductsByWarehouse(securityutils.getCurrentUser());
    }

    @PostMapping("/ventes")
    public HistoriqueVente vendre(@RequestBody VenteRequest request) {
        return venteManager.vendre(securityutils.getCurrentUser(), request.getProductId(), null, request.getQuantity());
    }

    @GetMapping("/ventes")
    public List<VenteResponse> getVentes() {
        return venteManager.getHistorique(securityutils.getCurrentUser());
    }

}