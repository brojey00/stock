package com.example.stock.controllers.adminController;

import com.example.stock.dao.entities.Entrepot;
import com.example.stock.dao.entities.HistoriqueVente;
import com.example.stock.dao.entities.Product;
import com.example.stock.dao.entities.Stock;
import com.example.stock.service.EntropotManager;
import com.example.stock.service.HistoriqueVenteManager;
import com.example.stock.service.ProductManager;
import com.example.stock.service.StockManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasRole('ADMIN')")
@RestController
public class HomeController {
    @Autowired
    private ProductManager productManager;
    @Autowired
    private EntropotManager entropotManager;
    @Autowired
    private StockManager stockManager;
    @Autowired
    private HistoriqueVenteManager historiqueVenteManager;

    // Product Management
    @GetMapping("/products")
    public List<Product> showProducts(){
        List<Product> list = productManager.getAll();
        if ((int) list.size() == 0){
            return List.of();
        }
        return list;
    }

    @GetMapping("/product/{id}")
    public Optional<Product> showProduct(@PathVariable int id){
        return productManager.getById(id);
    }

    @PostMapping("/addproduct")
    public Product addProduct(@RequestBody Product product){
        return productManager.addProduct(product);
    }

    @PutMapping("/updateproduct")
    public Product updateProduct(@RequestBody Product product){
        return productManager.updateProduct(product);
    }

    @DeleteMapping("/product/{id}")
    public String deleteProduct(@PathVariable int id){
        if(productManager.deleteProduct(id)){
            return "Product successfully deleted";
        }
        return "Product not deleted Try again";
    }

    @GetMapping("/entropots")
    public List<Entrepot> showEntrepots(){
        return entropotManager.getAll();
    }

    @GetMapping("/entrepot/{id}")
    public Optional<Entrepot> showEntrepot(@PathVariable Integer id){
        return entropotManager.getById(id);
    }

    @PostMapping("/addentrepot")
    public Entrepot addEntrepot(@RequestBody Entrepot entrepot){
        return entropotManager.addEntrepot(entrepot);
    }

    @PutMapping("/updateentrepot")
    public Entrepot updateEntrepot(@RequestBody Entrepot entrepot){
        return entropotManager.updateEntrepot(entrepot);
    }

    @DeleteMapping("/entrepot/{id}")
    public String deleteEntrepot(@PathVariable Integer id){
        if(entropotManager.deleteEntrepot(id)){
            return "Entrepot successfully deleted";
        }
        return "Entrepot not deleted Try again";
    }

    // View Everything (Stocks, Sales/Previsions)
    @GetMapping("/stocks")
    public List<Stock> showStocks(){
        return stockManager.getAll();
    }

    @GetMapping("/sales")
    public List<HistoriqueVente> showSales(){
        return historiqueVenteManager.getAll();
    }
}