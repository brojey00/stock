package com.example.stock.controllers.adminController;

import com.example.stock.dao.entities.Entrepot;
import com.example.stock.dao.entities.Product;
import com.example.stock.dao.entities.Stock;
import com.example.stock.dao.entities.User;
import com.example.stock.dto.AuthResponse;
import com.example.stock.dto.RegisterRequest;
import com.example.stock.dto.VenteRequest;
import com.example.stock.dao.entities.HistoriqueVente;
import com.example.stock.dto.VenteResponse;
import com.example.stock.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/admin")
public class HomeController {
    @Autowired
    private ProductManager productManager;
    @Autowired
    private EntropotManager entropotManager;
    @Autowired
    private StockManager stockManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private AuthService authService;
    @Autowired
    private VenteManager venteManager;
    @Autowired
    private Securityutils securityutils;
    @GetMapping("/products")
    public List<Product> showProducts() {
        return productManager.getAll();
    }
    @GetMapping("/products/{id}")
    public Optional<Product> showProduct(@PathVariable Integer id) {
        return productManager.getById(id);
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody Product product) {
        return productManager.addProduct(product);
    }

    @PatchMapping("/products/{id}")
    public Product updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        product.setId(id);
        return productManager.updateProduct(product);
    }

    @DeleteMapping("/products/{id}")
    public String deleteProduct(@PathVariable int id) {
        if (productManager.deleteProduct(id)) {
            return "Product successfully deleted";
        }
        return "Product not deleted Try again";
    }

    @GetMapping("/entrepots")
    public List<Entrepot> showEntrepots() {
        return entropotManager.getAll();
    }

    @GetMapping("/entrepots/{id}")
    public Optional<Entrepot> showEntrepot(@PathVariable Integer id) {
        return entropotManager.getById(id);
    }

    @PostMapping("/entrepots")
    public Entrepot addEntrepot(@RequestBody Entrepot entrepot) {
        return entropotManager.addEntrepot(entrepot);
    }

    @PatchMapping("/entrepots/{id}")
    public Entrepot updateEntrepot(@PathVariable Integer id, @RequestBody Entrepot entrepot) {
        entrepot.setId(id);
        return entropotManager.updateEntrepot(entrepot);
    }

    @DeleteMapping("/entrepots/{id}")
    public String deleteEntrepot(@PathVariable Integer id) {
        if (entropotManager.deleteEntrepot(id)) {
            return "Entrepot successfully deleted";
        }
        return "Entrepot not deleted Try again";
    }

    @GetMapping("/stocks")
    public List<Stock> showStocks() {
        return stockManager.getAll();
    }

    @GetMapping("/stocks/{id}")
    public Optional<Stock> showStock(@PathVariable Integer id) {
        return stockManager.getById(id);
    }

    @PostMapping("/stocks")
    public Stock addStock(@RequestBody Stock stock) {
        return stockManager.addStock(stock);
    }

    @PatchMapping("/stocks/{id}")
    public Stock updateStock(@PathVariable int id, @RequestBody Stock stock) {
        stock.setId(id);
        return stockManager.updateStock(securityutils.getCurrentUser(),stock);
    }

    @DeleteMapping("/stocks/{id}")
    public String deleteStock(@PathVariable Integer id) {
        if (stockManager.deleteStock(id)) {
            return "Stock successfully deleted";
        }
        return "Stock not deleted Try again";
    }
    @GetMapping("/users")
    public List<User> showUsers(){
        return userManager.getAll();
    }
    @GetMapping("/users/{id}")
    public Optional<User> showUser(@PathVariable Integer id){
        return userManager.getById(id);
    }
    @PostMapping("/users")
    public AuthResponse addUser(@RequestBody RegisterRequest registerRequest){
        return authService.register(registerRequest);
    }
    @PatchMapping("/users/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User user){
        user.setId(id);
        return userManager.updateUser(user);
    }
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Integer id){
        if (userManager.deleteUser(id)){
            return "User deleted ";
        }
        return "User not deleted try again ";
    }
    @PostMapping("/ventes")
    public HistoriqueVente vendre(@AuthenticationPrincipal User user, @RequestBody VenteRequest request) {
        return venteManager.vendre(user, request.getProductId(), request.getEntrepotId(), request.getQuantity());
    }

    @GetMapping("/ventes")
    public List<VenteResponse> getAllVentes(@AuthenticationPrincipal User user) {
        return venteManager.getHistorique(user);
    }

    @GetMapping("/ventes/entrepots/{id}")
    public List<VenteResponse> getVentesByEntrepot(@PathVariable Integer id) {
        return venteManager.getHistoriqueByEntrepot(id);
    }

    @PutMapping("/users/{user_id}/warehouse/{warehouse_id}")
    public String assignWarehouseToUser(@PathVariable Integer user_id, @PathVariable Integer warehouse_id){
        if(userManager.assignUserToEntrepot(user_id,warehouse_id)){
            return "Warehouse added to the user ";
        }
        return "Not added try again";
    }
    @DeleteMapping("/users/{user_id}/warehouse")
    public String removeWarehouseFromUser(@PathVariable Integer user_id){
        if(userManager.removeUserFromWarehouse(user_id)){
            return "Warehouse removed from the manager";
        }
        return "Not removed try again";
    }

}