package com.example.stock.service;

import com.example.stock.dao.repositories.HistoriqueVenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoriqueVenteManager {
    @Autowired
    private HistoriqueVenteRepository historiqueVenteRepository;
}
