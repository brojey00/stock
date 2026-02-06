package com.example.stock.service;

import com.example.stock.dao.entities.Product;
import com.example.stock.dao.entities.Role;
import com.example.stock.dao.entities.User;
import com.example.stock.dao.entities.Entrepot;
import com.example.stock.dao.repositories.UserRepository;
import com.example.stock.dao.repositories.EntropotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManager {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntropotRepository entrepotRepository;

    public List<User> getAll() {

        List<User> list = userRepository.findAll();
        if (list.isEmpty()) {
            return List.of();
        }
        return list;
    }

    public Optional<User> getById(Integer id) {
        return userRepository.findById(id);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        User userToUpdate=userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        return userRepository.save(userToUpdate);
    }
    public boolean deleteUser(Integer id) {
        userRepository.deleteById(id);
        return userRepository.findById(id).isEmpty();
    }
    public boolean assignUserToEntrepot(Integer userId, Integer entrepotId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Entrepot entrepot = entrepotRepository.findById(entrepotId).orElseThrow(() -> new RuntimeException("Entrepot not found"));
        if (user.getEntrepotAssigne()!=null){
            throw new IllegalStateException("User already has a warehouse.");
        }
        user.setEntrepotAssigne(entrepot);
        userRepository.save(user);
        return true;
    }
    public boolean removeUserFromWarehouse(Integer userId){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("user does not exist"));
        user.setEntrepotAssigne(null);
        return user.getEntrepotAssigne() == null;
    }

}
