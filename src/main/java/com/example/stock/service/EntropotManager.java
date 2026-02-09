package com.example.stock.service;

import com.example.stock.dao.entities.Entrepot;
import com.example.stock.dao.repositories.EntropotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EntropotManager {
    @Autowired
    private EntropotRepository entropotRepository;

    public List<Entrepot> getAll() {
        List<Entrepot> list = entropotRepository.findAll();
        if (list.isEmpty()) {
            return List.of();
        }
        return list;
    }

    public Optional<Entrepot> getById(Integer id) {
        return entropotRepository.findById(id);
    }

    public Entrepot addEntrepot(Entrepot entrepot) {
        return entropotRepository.save(entrepot);
    }

    public Entrepot updateEntrepot(Entrepot entrepot) {
        Entrepot existing = entropotRepository.findById(entrepot.getId()).orElseThrow(()->new RuntimeException("warehouse does not exist"));
        if(entrepot.getCity()!=null){
            existing.setCity(entrepot.getCity());
        }
        if(entrepot.getLocation()!=null){
            existing.setLocation(entrepot.getLocation());
        }
        if(entrepot.getName()!=null){
            existing.setName(entrepot.getName());
        }
        return entropotRepository.save(existing);
    }

    public boolean deleteEntrepot(Integer id) {
        entropotRepository.deleteById(id);
        return entropotRepository.findById(id).isEmpty();
    }
}
