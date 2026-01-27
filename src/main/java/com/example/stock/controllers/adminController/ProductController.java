package com.example.stock.controllers.adminController;

import com.example.stock.dao.entities.Entrepot;
import com.example.stock.dao.entities.Product;
import com.example.stock.service.EntropotManager;
import com.example.stock.service.ProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
public class ProductController {
    @Autowired
    private ProductManager productManager;
    @GetMapping("/products")
    public List<Product> showProducts(){
        List<Product> list = productManager.getAll();
        if ((int) list.size() ==0){
            return List.of();
        }
        return list;
    }
    @PostMapping("/addproduct")
    public Product addproduct(){

    }

}
