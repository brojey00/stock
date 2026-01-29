package com.example.stock.service;

import com.example.stock.dao.entities.Entrepot;
import com.example.stock.dao.repositories.EntropotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntropotManager {
    @Autowired
    private EntropotRepository entropotRepository;
    public List<Entrepot> getAll(){
        return entropotRepository.findAll();
    }
}
