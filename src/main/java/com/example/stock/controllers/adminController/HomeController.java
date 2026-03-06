package com.example.stock.controllers.adminController;

import com.example.stock.dao.entities.Entrepot;
import com.example.stock.dao.entities.Product;
import com.example.stock.dao.entities.Stock;
import com.example.stock.dao.entities.User;
import com.example.stock.dto.AuthResponse;
import com.example.stock.dto.RegisterRequest;
import com.example.stock.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/")
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
    @GetMapping("/products")
    public List<Product> showProducts() {
        return productManager.getAll();
    }

    @GetMapping("/product/{id}")
    public Optional<Product> showProduct(@PathVariable Integer id) {
        return productManager.getById(id);
    }

    @PostMapping("/addproduct")
    public Product addProduct(@RequestBody Product product) {
        return productManager.addProduct(product);
    }

    @PatchMapping("/updateproduct")
    public Product updateProduct(@RequestBody Product product) {
        return productManager.updateProduct(product);
    }

    @DeleteMapping("/product/{id}")
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

    @GetMapping("/entrepot/{id}")
    public Optional<Entrepot> showEntrepot(@PathVariable Integer id) {
        return entropotManager.getById(id);
    }

    @PostMapping("/addentrepot")
    public Entrepot addEntrepot(@RequestBody Entrepot entrepot) {
        return entropotManager.addEntrepot(entrepot);
    }

    @PatchMapping("/updateentrepot")
    public Entrepot updateEntrepot(@RequestBody Entrepot entrepot) {

        return entropotManager.updateEntrepot(entrepot);
    }

    @DeleteMapping("/entrepot/{id}")
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

    @GetMapping("/stock/{id}")
    public Optional<Stock> showStock(@PathVariable Integer id) {
        return stockManager.getById(id);
    }

    @PostMapping("/addstock")
    public Stock addStock(@RequestBody Stock stock) {
        return stockManager.addStock(stock);
    }

    @PatchMapping("/updatestock/{user_id}")
    public Stock updateStock(@PathVariable int user_id,@RequestBody Stock stock) {
        return stockManager.updateStock(user_id,stock);
    }

    @DeleteMapping("/stock/{id}")
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
    @GetMapping("/user/{id}")
    public Optional<User> showUser(@PathVariable Integer id){
        return userManager.getById(id);
    }
    @PostMapping("/adduser")
    public AuthResponse addUser(@RequestBody RegisterRequest registerRequest){
        return authService.register(registerRequest);
    }
    @PatchMapping("/updateuser")
    public User updateUser(@RequestBody User user){
        return userManager.updateUser(user);
    }
    @DeleteMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable Integer id){
        if (userManager.deleteUser(id)){
            return "User deleted ";
        }
        return "User not deleted try again ";
    }
    @PutMapping("assign-warehouse/{user_id}/{warehouse_id}")
    public String assignWarehouseToUser(@PathVariable Integer user_id,@PathVariable Integer warehouse_id){
        if(userManager.assignUserToEntrepot(user_id,warehouse_id)){
            return "Warehouse added to the user ";
        }
        return "Not added try again";
    }
    @PutMapping("remove-warehouse/{user_id}")
    public String removeWarehouseFromUser(@PathVariable Integer user_id){
        if(userManager.removeUserFromWarehouse(user_id)){
            return "Warehouse removed from the manager";
        }
        return "Not removed try again";
    }

}