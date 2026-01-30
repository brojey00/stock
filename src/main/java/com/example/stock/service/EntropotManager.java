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

    public List<Entrepot> getAll(){
        return entropotRepository.findAll();
    }

    public Optional<Entrepot> getById(Integer id){
        return entropotRepository.findById(id);
    }

    public Entrepot addEntrepot(Entrepot entrepot){
        return entropotRepository.save(entrepot);
    }

    public Entrepot updateEntrepot(Entrepot entrepot){
        Optional<Entrepot> existing = entropotRepository.findById(entrepot.getId());
        if (existing.isEmpty() ){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Entrepot does not exist"
            );
        }
        return entropotRepository.save(entrepot);
    }

    public boolean deleteEntrepot(Integer id){
        entropotRepository.deleteById(id);
        return entropotRepository.findById(id).isEmpty();
    }
}

