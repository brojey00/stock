package com.example.stock.dto;

import com.example.stock.dao.entities.Day;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record VenteResponse (
        String product_name,
        String Warehouse_name,
        int quantity,
        @Enumerated(EnumType.STRING)
        Day jour_semaine,
        Integer mois,
        Integer annee
){}
