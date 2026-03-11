package com.example.stock.dao.repositories;

import com.example.stock.dao.entities.Entrepot;
import com.example.stock.dao.entities.HistoriqueVente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueVenteRepository extends JpaRepository<HistoriqueVente, Integer> {
    List<HistoriqueVente> findByEntrepot(Entrepot entrepot);
}
