package com.example.stock.dao.repositories;

import com.example.stock.dao.entities.Entrepot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntropotRepository extends JpaRepository<Entrepot,Integer> {
    //Entrepot findEntrepotByUserId(Integer id);
}
