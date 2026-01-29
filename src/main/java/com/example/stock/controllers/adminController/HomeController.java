package com.example.stock.controllers.adminController;

import com.example.stock.dao.entities.Entrepot;
import com.example.stock.dao.entities.Product;
import com.example.stock.service.EntropotManager;
import com.example.stock.service.ProductManager;
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
    @GetMapping("/products")
    public List<Product> showProducts(){
        List<Product> list = productManager.getAll();
        if ((int) list.size() ==0){
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

}
