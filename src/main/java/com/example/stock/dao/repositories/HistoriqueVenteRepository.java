package com.example.stock.dao.repositories;

import com.example.stock.dao.entities.HistoriqueVente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoriqueVenteRepository extends JpaRepository<HistoriqueVente,Integer> {
}
