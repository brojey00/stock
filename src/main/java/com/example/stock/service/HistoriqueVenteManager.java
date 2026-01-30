package com.example.stock.service;

import com.example.stock.dao.entities.HistoriqueVente;
import com.example.stock.dao.repositories.HistoriqueVenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoriqueVenteManager {
    @Autowired
    private HistoriqueVenteRepository historiqueVenteRepository;

    public List<HistoriqueVente> getAll() {
        return historiqueVenteRepository.findAll();
    }
}
